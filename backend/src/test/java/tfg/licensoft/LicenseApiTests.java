package tfg.licensoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import tfg.licensoft.api.ApiLicenseController;
import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.licenses.LicenseSubscription;
import tfg.licensoft.licenses.LicenseSubscriptionService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.stripe.StripeServices;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

 

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class LicenseApiTests {
	
	@Autowired
	private MockMvc mvc;
    @Spy
	private final ApiLicenseController licenseController = new ApiLicenseController();
    
    @MockBean
    private LicenseService licenseService;

    @MockBean
    private LicenseSubscriptionService licenseSubsService;
    
    @MockBean
    private ProductService prodServ;
    
    @MockBean
    private UserService userServ;
    
    @MockBean
    private StripeServices stripeServ;
    
    @Before
    public void initialize() throws StripeException {
    	User user = new User("test@gmail.com","cus_id1","test","t","ROLE_ADMIN","ROLE_USER");
    	Product p1 = new Product();
    	p1.setActive(true);
    	p1.setDescription("Desc p1");
    	p1.setName("P1");
    	p1.setPhotoAvailable(false);
    	HashMap<String,Double> plans = new HashMap<>();
    	plans.put("L", 12.0);
    	p1.setPlansPrices(plans);
    	p1.setProductStripeId("p1_stripeId");
    	p1.setSku("sku_p1");
    	p1.setTrialDays(0);
    	p1.setWebLink("p1.com");
    	License licLife = new License(true,p1,"test");
    	licLife.setSerial("s1");
    	License licLife2 = new License(true,p1,"test");
    	licLife2.setSerial("s2");
    	
    	
    	Product pS= new Product();
    	pS.setActive(true);
    	pS.setDescription("Desc pS");
    	pS.setName("PS");
    	pS.setPhotoAvailable(false);
    	HashMap<String,Double> plans2 = new HashMap<>();
    	plans2.put("D", 1.0);
    	plans2.put("M", 10.0);
    	plans2.put("A", 100.0);
    	plans2.put("MB", 0.2);
    	pS.setPlansPrices(plans2);
    	pS.setProductStripeId("pS_stripeId");
    	pS.setTrialDays(10);
    	pS.setWebLink("pS.com");
    	
    	LicenseSubscription lSubsD = new LicenseSubscription(true,"D",pS,"test",0);
    	lSubsD.setSerial("sd");
    	lSubsD.setCancelAtEnd(false);
    	LicenseSubscription lSubsM = new LicenseSubscription(true,"M",pS,"test",0);
    	lSubsM.setSerial("sm");
    	LicenseSubscription lSubsA = new LicenseSubscription(true,"A",pS,"test",0);
    	lSubsA.setSerial("sa");
    	LicenseSubscription lSubsMB = new LicenseSubscription(true,"MB",pS,"test",0);
    	lSubsMB.setSerial("smb");
    	LicenseSubscription lSubsMFree = new LicenseSubscription(true,"M",pS,"test",10);
    	lSubsMFree.setSerial("smfree");
    	
    	
    	List<License> lics2 = new ArrayList<>();
    	lics2.add(licLife);
    	lics2.add(licLife2);
    	Page<License> lics = new PageImpl<License>(lics2);
    	
    	List<License> licsSubs = new ArrayList<>();
    	licsSubs.add(lSubsMFree);
    	licsSubs.add(lSubsA);
    	licsSubs.add(lSubsD);
    	licsSubs.add(lSubsMB);
    	licsSubs.add(lSubsM);

    	given(licenseService.findLicensesOfProduct(any())).willReturn(licsSubs);
    	given(licenseService.findBySerialAndProduct("s1",p1)).willReturn(licLife);
    	given(licenseSubsService.findBySerialAndProduct("sd",pS)).willReturn(lSubsD);
    	given(licenseService.findBySerialAndProduct("sxxx",p1)).willReturn(null);

    	given(prodServ.findOne(p1.getName())).willReturn(p1);
    	given(prodServ.findOne("no")).willReturn(null); 
    	given(prodServ.findOne(pS.getName())).willReturn(pS);

    	given(userServ.findByName("test")).willReturn(user);
    	given(userServ.findByName("no")).willReturn(null);
    	given(licenseService.findByUsername(any())).willReturn(licsSubs);
    	
    	
    	given(licenseService.findByProductAndOwner(any(), any())).willReturn(licsSubs);
    	//Mockear Customer, y todo lo que conlleva: Subscriptions to Products, that Products, etc.
    	com.stripe.model.Product p = new com.stripe.model.Product();
    	p.setName("PS");
    	Customer c1 = new Customer();
    	c1.setId("cus_id1");
    	SubscriptionCollection sc = new SubscriptionCollection();
    	List<Subscription> data = new ArrayList<>();
    	Subscription s = new Subscription();
    	Plan pl1 = new Plan();
    	pl1.setProduct("prd_id");
    	pl1.setNickname("D");
    	s.setPlan(pl1);
    	data.add(s);
    	sc.setData(data);
    	c1.setSubscriptions(sc);
    	
    	given(this.stripeServ.retrieveProduct(any())).willReturn(p);
    	given(this.stripeServ.retrieveCustomer("cus_id1")).willReturn(c1);
    	
    	LicenseSubscription lSubsDWrong = new LicenseSubscription(true,"D",pS,"wrong",0);
    	lSubsDWrong.setSerial("sdwrong");
    	lSubsDWrong.setCancelAtEnd(false);
    	given(licenseSubsService.findBySerialAndProduct("sdwrong",pS)).willReturn(lSubsDWrong);

    }
    
    
    @Test
    public void testGetLicensesOfProduct() throws Exception { 
       	mvc.perform(MockMvcRequestBuilders.get("/api/licenses/products/P1")
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$[0].serial",is("smfree")))
                 .andExpect(jsonPath("$[1].serial",is("sa")))
                 .andExpect(jsonPath("$",hasSize(5)))
     			.andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testGetLicensesOfProductNotFoundProduct() throws Exception { 
       	mvc.perform(MockMvcRequestBuilders.get("/api/licenses/products/no")
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isNotFound());
    }
    

    @Test 
    public void testGetLicenseBySerialAndProduct() throws Exception{
    	 mvc.perform(MockMvcRequestBuilders.get("/api/licenses/s1/products/P1")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
                .andExpect(jsonPath("$.type",is("L")));
    	
    }
    
    @Test  
    public void testGetLicenseBySerialAndProductNotFoundLicense() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/licenses/sxxx/products/P1")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetLicensesOfUser() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/licenses/users/test")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(5)))
    			.andExpect(jsonPath("$[0].owner",is("test")))
				.andExpect(jsonPath("$[1].owner",is("test")))
				.andExpect(jsonPath("$[2].owner",is("test")))
				.andExpect(jsonPath("$[3].owner",is("test")))
				.andExpect(jsonPath("$[4].owner",is("test")));

    }
    
    @Test
    public void testGetLicensesOfUserNotFoundUser() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/licenses/users/no")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isNotFound());

    } 
    
    @Test
    public void testGetLicensesOfUserAndProduct() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/licenses/users/test/products/PS")
    			.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
    			.andExpect(jsonPath("$[0].owner",is("test")))
				.andExpect(jsonPath("$[1].owner",is("test")))
				.andExpect(jsonPath("$[2].owner",is("test")))
				.andExpect(jsonPath("$[3].owner",is("test")))
    			.andExpect(jsonPath("$[0].product.name",is("PS")))
				.andExpect(jsonPath("$[1].product.name",is("PS")))
				.andExpect(jsonPath("$[2].product.name",is("PS")))
				.andExpect(jsonPath("$[3].product.name",is("PS")))
				.andExpect(jsonPath("$", hasSize(5)));
    }
    
    @Test
    public void testGetLicensesOfUserAndProductNotFoundProduct() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/licenses/users/test/products/no")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isNotFound());

    } 
    
    @Test
    public void testCancelAtEnd() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.put("/api/licenses/cancelAtEnd/sd/products/PS")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.cancelAtEnd",is(true)));

    } 
    
    @Test
    public void testCancelAtEndProductNull() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.put("/api/licenses/cancelAtEnd/sd/products/no")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isPreconditionRequired());

    }
    
     
}
