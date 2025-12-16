package mx.com.leyva.order.controller;

import mx.com.leyva.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenValidCredentials_thenOk() throws Exception {
        String jsonBody = """
                      {
                        "orderId": "ORD-FUEL-0001",
                        "customerId": "CUST-ENERGY-01",
                        "canal": "WEB",
                        "items": [
                          {
                            "sku": "SKU-GAS-PLUS-91",
                            "cantidad": 50,
                            "precioUnitario": 25.50
                          }
                        ]
                      }
                """;
        mockMvc.perform(post("/orders/process")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated());
    }

    @Test
    void whenNoCredentials_thenUnauthorized_401() throws Exception {
        mockMvc.perform(post("/orders/process")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

}
