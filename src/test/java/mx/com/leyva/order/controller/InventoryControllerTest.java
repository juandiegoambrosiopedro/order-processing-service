package mx.com.leyva.order.controller;

import mx.com.leyva.order.service.InventoryService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Test
    void whenNoCredentials_thenUnauthorized_401() throws Exception {
        mockMvc.perform(get("/inventory/sku/SKU-DIESEL"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenValidCredentials_thenOk() throws Exception {
        mockMvc.perform(get("/inventory/sku/SKU-DIESEL"))
                .andExpect(status().isOk());
    }

}