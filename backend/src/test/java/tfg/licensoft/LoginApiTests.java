package tfg.licensoft;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import tfg.licensoft.api.LoginController;
import tfg.licensoft.licenses.License;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserComponent;
import tfg.licensoft.users.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class LoginApiTests {
	
	
	@Autowired
	private MockMvc mvc;
    @Spy
	private final LoginController loginController = new LoginController();
    
    @MockBean
    private UserComponent userComponent;
    
    @MockBean
    private UserService userServ;
    
    
    @Before
    public void initialize() {
    	User user = new User("test@gmail.com","cus_id1","test","t","ROLE_ADMIN","ROLE_USER");
    	User user2 = new User("test2@gmail.com","cus_id2","test2","t","ROLE_USER");
    	List<User> l = new ArrayList<>();
    	l.add(user2);
    	l.add(user);
    	Page<User> usersPage = new PageImpl<User>(l);

    	given(this.userServ.findAll(any())).willReturn(usersPage);

    }
    
    
    @Test
    public void testLogIn() throws Exception{
    	User user = new User("test@gmail.com","cus_id1","test","t","ROLE_ADMIN","ROLE_USER");
    	given(this.userComponent.isLoggedUser()).willReturn(true);
    	given(this.userComponent.getLoggedUser()).willReturn(user);

    	mvc.perform(MockMvcRequestBuilders.get("/api/logIn")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("test")));
    }
    
    @Test
    public void testLogInUnauthorized() throws Exception{
    	given(this.userComponent.isLoggedUser()).willReturn(false);

    	mvc.perform(MockMvcRequestBuilders.get("/api/logIn")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testLogOut() throws Exception{
    	User user = new User("test@gmail.com","cus_id1","test","t","ROLE_ADMIN","ROLE_USER");
    	given(this.userComponent.isLoggedUser()).willReturn(true);
    	given(this.userComponent.getLoggedUser()).willReturn(user);

    	mvc.perform(MockMvcRequestBuilders.get("/api/logOut")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
    
    @Test
    public void testLogOutUnauthorized() throws Exception{
    	given(this.userComponent.isLoggedUser()).willReturn(false);

    	mvc.perform(MockMvcRequestBuilders.get("/api/logOut")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    
    @Test 
    public void testGetAllUSers() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements",is(2)));
    }
    
    @Test 
    public void testGetUserLogged() throws Exception{
    	User user = new User("test@gmail.com","cus_id1","test","t","ROLE_ADMIN","ROLE_USER");
    	given(this.userComponent.getLoggedUser()).willReturn(user);
    	mvc.perform(MockMvcRequestBuilders.get("/api/getUserLogged")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email",is("test@gmail.com")));
    }
    
    
}
