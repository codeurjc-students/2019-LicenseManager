package tfg.licensoft;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import tfg.licensoft.api.ApiUsedCardController;
import tfg.licensoft.usedCards.UsedCard;
import tfg.licensoft.usedCards.UsedCardService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class UsedCardApiTests {
	
	@Autowired
	private MockMvc mvc;
    @Spy
	private final ApiUsedCardController usedCardController = new ApiUsedCardController();
    
    @MockBean
	private UsedCardService usedCardServ;
    
    @Before
    public void initialize() {
    	UsedCard usedC = new UsedCard(4442,12,32,"PS");
    	given(this.usedCardServ.findByLast4AndExpMonthAndExpYearAndProductName(4442, 12, 32, "PS")).willReturn(usedC);
    	given(this.usedCardServ.findByLast4AndExpMonthAndExpYearAndProductName(4442, 12, 32, "P1")).willReturn(null);
    	given(this.usedCardServ.save(any())).willReturn(usedC);
    }
    
    @Test
    public void testCheckIfUsed() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/usedcards/checkUsed/4442/12/32/product/PS")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("true"))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testCheckIfUsedNotUsed() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/usedcards/checkUsed/4442/12/32/product/P1")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("false"))
                .andExpect(status().isOk());
    }
    
    @Test
    public void postUsedCard() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.post("/api/usedcards/4442/12/32/product/P1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.last4",is(4442)));
    }
	
	

}
