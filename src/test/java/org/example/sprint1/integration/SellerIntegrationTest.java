package org.example.sprint1.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.sprint1.dto.ProductDTO;
import org.example.sprint1.dto.RequestPostDTO;
import org.example.sprint1.entity.Seller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SellerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    public void testAddPost() throws Exception {
        ProductDTO product = new ProductDTO(
                1, // product_id
                "Laptop", // product_name
                "Electronics", // type
                "Dell", // brand
                "Black", // color
                "Latest model with 16GB RAM" // notes
        );
        RequestPostDTO postDTO = new RequestPostDTO(
                1,
                LocalDate.now(), // date
                product, // product DTO
                1, // category
                789.00 // price
        );

        ObjectWriter writer = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE,false)
                .writer().withDefaultPrettyPrinter();
        String jsonPostDTO = writer.writeValueAsString(postDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/products/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPostDTO))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @Test
    public void testGetAllSellers() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    public void testGetPostsFromFollowingWithTwoWeeksOld() throws Exception {
        int userId = 234;
        String order = "date_asc";

        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/followed/{userId}/list", userId)
                        .param("order", order))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value(userId));
    }
}
