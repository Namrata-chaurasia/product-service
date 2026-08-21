package service;

import com.namrata.microservices.productservice.dto.ProductRequest;
import com.namrata.microservices.productservice.dto.ProductResponse;
import com.namrata.microservices.productservice.entity.Product;
import com.namrata.microservices.productservice.exception.ProductNotFoundException;
import com.namrata.microservices.productservice.repository.ProductRepository;
import com.namrata.microservices.productservice.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void shouldAddProductSuccessfully() {
        //Arrange Request/Preparing Test Data
        ProductRequest request = new ProductRequest();

        request.setProductName("Laptop");
        request.setPrice(75000.0);

        Product product = new Product();

        product.setId(1L);
        product.setProductName("Laptop");
        product.setPrice(75000.0);

        when(repository.save(any(Product.class)))
                .thenReturn(product);

        //Act
        ProductResponse response = service.addProduct(request);

        //Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Laptop", response.getProductName());
        assertEquals(75000.0, response.getPrice(), 0.01);

        verify(repository, times(1))
                .save(any(Product.class));
    }

    @Test
    void shouldReturnProductWhenProductExists() {
        Product product = new Product();
        product.setId(1L);
        product.setProductName("Mobile");
        product.setPrice(35000.0);

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductResponse response = service.getProduct(1L);

        assertEquals(1L, response.getId());
        assertEquals("Mobile", response.getProductName());
        assertEquals(35000.0, response.getPrice());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> service.getProduct(1L)
        );

        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(repository, times(1))
                .findById(1L);
    }

    @Test
    void shouldReturnAllProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setProductName("Keyboard");
        product1.setPrice(2500.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setProductName("Mouse");
        product2.setPrice(500.0);

        when(repository.findAll())
                .thenReturn(List.of(product1, product2));

        List<ProductResponse> response = service.getAllProducts();

        assertEquals(2, response.size());
        assertEquals("Keyboard", response.get(0).getProductName());

        assertEquals("Mouse", response.get(1).getProductName());

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        ProductRequest request = new ProductRequest();
        request.setProductName("Laptop");
        request.setPrice(80000.0);

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setProductName("Speaker");
        existingProduct.setPrice(3400.0);

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setProductName("Laptop");
        updatedProduct.setPrice(80000.0);

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingProduct));

        when(repository.save(any(Product.class)))
                .thenReturn(updatedProduct);

        ProductResponse response = service.updateProduct(1L, request);
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Laptop", response.getProductName());
        assertEquals(80000.0, response.getPrice());

        verify(repository, times(1))
                .findById(1L);
        verify(repository, times(1))
                .save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingProduct() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception =
                assertThrows(
                        ProductNotFoundException.class,
                        () -> service.updateProduct(
                                1L,
                                new ProductRequest()
                        )
                );

        assertEquals(
                "Product not found with id: 1",
                exception.getMessage()
        );

        verify(repository, times(1))
                .findById(1L);
    }
}
