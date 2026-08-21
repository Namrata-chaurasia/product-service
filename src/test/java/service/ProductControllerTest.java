package service;

import com.namrata.microservices.productservice.ProductServiceApplication;
import com.namrata.microservices.productservice.controller.ProductController;
import com.namrata.microservices.productservice.dto.ProductRequest;
import com.namrata.microservices.productservice.dto.ProductResponse;
import com.namrata.microservices.productservice.exception.ProductNotFoundException;
import com.namrata.microservices.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = ProductServiceApplication.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @Test
    void shouldReturnProductById() throws Exception {
        ProductResponse response =
                ProductResponse.builder()
                        .id(1L)
                        .productName("Laptop")
                        .price(75000.0)
                        .build();

        when(service.getProduct(1L))
                .thenReturn(response);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.productName").value("Laptop")
                );
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        List<ProductResponse> products = List.of(
                ProductResponse.builder()
                        .id(1L)
                        .productName("Laptop")
                        .price(75000.0)
                        .build(),

                ProductResponse.builder()
                        .id(2L)
                        .productName("Mouse")
                        .price(500.0)
                        .build()
        );

        when(service.getAllProducts())
                .thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRecords").value(2)
                );
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception{
        when(service.getProduct(1L))
                .thenThrow(new ProductNotFoundException(
                        "Product not found with id: 1"
                ));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception{
        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .productName("Camera")
                .price(25000.0)
                .build();
        when(service.addProduct(any(ProductRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                 "productName": "Camera",
                                 "price": 25000.0
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product created successfully"))
                .andExpect(jsonPath("$.data.productName").value("Camera"));

    }

    @Test
    void shouldUpdateProductSuccessfully() throws Exception{
        ProductResponse updatedProduct = ProductResponse.builder()
                .id(1L)
                .productName("Mouse")
                .price(400.0)
                .build();

        when(service.updateProduct(eq(1L), any(ProductRequest.class)))
                .thenReturn(updatedProduct);

        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "productName": "Mouse",
                            "price": 400.0
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product updated successfully"))
                .andExpect(jsonPath("$.data.productName").value("Mouse"));

        verify(service, times(1))
                .updateProduct(eq(1L), any(ProductRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidProductRequest() throws Exception{
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "productName": "",
                            "price": -2400
                        }
                        """))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .addProduct(any(ProductRequest.class));
    }
}