package tfg.licensoft;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import tfg.licensoft.api.GeneralController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.junit.Assert.assertThat;
import org.springframework.security.test.context.support.WithMockUser;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class GeneralApiTests {
	
    @Autowired
    private MockMvc mvc;
    
    @Spy
	private final GeneralController generalController = new GeneralController();
    
    @Before
    public void initialize() {
		ReflectionTestUtils.setField(generalController, "appName", "TestName");

       /*
        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(unitService.findOne(2)).willReturn(Optional.empty());
        given(cardService.findOne(1)).willReturn(Optional.of(card1));
        given(cardService.findOne(2)).willReturn(Optional.empty());*/
    }
	
	@Test
	public void testGetAppName() throws Exception {
		/*mvc.perform(MockMvcRequestBuilders.get("/api/appName")
				.contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.text", is("TestName")));*/
	}

}
