package tfg.licensoft;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import static org.hamcrest.Matchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tfg.licensoft.api.GeneralController;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
@TestPropertySource(properties = {"appName=Name!", "stripe.publicKey=pk_test", "app.domain=http://testdomain.com","licencheck.keys.private=path/to/key"})
public class GeneralApiTests {
	
    @Autowired
    private MockMvc mvc;
    
    
   
    @Spy
	private final GeneralController generalController = new GeneralController();
    

    @Before
    public void initialize() {

    }
	
	@Test
	public void testGetAppName() throws Exception {
	mvc.perform(MockMvcRequestBuilders.get("/api/appName")
				.contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response", is("Name!")));
	}
	
	@Test
	public void testGetPublicApiKey() throws Exception {
	mvc.perform(MockMvcRequestBuilders.get("/api/keys/stripe/public")
				.contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response", is("pk_test")));
	}
	
	@Test
	public void testGetAppDomain() throws Exception {
	mvc.perform(MockMvcRequestBuilders.get("/api/appDomain")
				.contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response", is("http://testdomain.com")));
	}
	
	/*
	@Test
	public void testGetPublicPrivateKeyPath() throws Exception {
	mvc.perform(MockMvcRequestBuilders.get("/api/privateKey")
				.contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.text", is("path/to/key")));
	}
	 */

}
