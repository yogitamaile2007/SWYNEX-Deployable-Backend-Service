package com.swynex.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:${random.uuid}", "spring.datasource.username=sa", "spring.datasource.password=", "spring.datasource.driver-class-name=org.h2.Driver", "app.api-key=secret"})
@AutoConfigureMockMvc
class ApiKeyTest {

    @Autowired
    MockMvc mvc;

    @Test
    void writesNeedKeyButReadsArePublic() throws Exception {
        String json = "{\"title\":\"x\"}";
        mvc.perform(get("/api/tasks")).andExpect(status().isOk());
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/tasks").header("X-API-Key", "secret")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }
}
