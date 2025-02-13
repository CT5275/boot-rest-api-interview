package posmy.interview.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("Users must be authorized in order to perform actions")
    void contextLoads() throws Exception {
        mvc.perform(get("/lib"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    @WithMockUser(username = "test", roles={"LIBRARIAN"})
    void contextLoadst_lib_sucess() throws Exception {
        mvc.perform(get("/lib/book"))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "test", roles={"LIBRARIAN"})
    void contextLoadst_lib_fail() throws Exception {
        mvc.perform(get("/mem/book"))
                .andExpect(status().isForbidden());
        
    }
    
    @Test
    @WithMockUser(username = "test2", roles={"MEMBER"})
    void contextLoadsTest_member_sucess() throws Exception {
        mvc.perform(get("/mem/book/available"))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "test2", roles={"MEMBER"})
    void contextLoadsTest_member_fail() throws Exception {
        mvc.perform(get("/lib/book"))
                .andExpect(status().isForbidden());
    }

}
