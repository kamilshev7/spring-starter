package guard.passer.core.integration.http.controller;

import guard.passer.core.database.entity.UserRole;
import guard.passer.core.dto.UserCreateEditDto;
import guard.passer.core.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static guard.passer.core.dto.UserCreateEditDto.Fields.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest extends IntegrationTestBase {

    private final MockMvc mockMvc;

//    @BeforeEach
//    void init(){
//        List<GrantedAuthority> roles = Arrays.asList(UserRole.USER, UserRole.ADMIN);
//        User testUser = new User("test@gmail.com", "test", roles);
//        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(testUser, testUser.getPassword(), roles);
//
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(authenticationToken);
//        SecurityContextHolder.setContext(securityContext);
//    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "test", authorities = {"ADMIN", "USER"})
    void findAll() throws Exception {
        mockMvc.perform(get("/users/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"));
        System.out.println();
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users/")
                        .param(username, "test@")
                        .param(firstname, "test")
                        .param(lastname, "testov")
                        .param(role, "ADMIN")
                        .param(birthDate, "2000-01-01")
                        .param(companyId, "1")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/{\\d+}")
                );

    }
}