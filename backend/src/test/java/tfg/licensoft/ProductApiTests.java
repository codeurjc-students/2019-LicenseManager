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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.stripe.model.Plan;
import com.stripe.model.Sku;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.stripe.param.PlanCreateParams;

import tfg.licensoft.api.ApiProductController;
import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseSubscription;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.stripe.StripeServices;
import tfg.licensoft.users.User;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class ProductApiTests {

	private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

	
	@Autowired
	private MockMvc mvc;
	
    @Spy
	private final ApiProductController productController = new ApiProductController();
	
	@MockBean
	private ProductService productServ;
	
    
    @MockBean
    private StripeServices stripeServ;
	
    private Gson jsonParser = new Gson();

    
    @Before
    public void initialize() {
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
    	p1.setPhotoAvailable(true);
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
    	
    	List<Product> prods = new ArrayList<>();
    	prods.add(pS);
    	prods.add(p1);
    	
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
    	
    	given(productServ.findAllActives()).willReturn(prods);
    	given(productServ.findSearch(any())).willReturn(prods);
    	
    	given(productServ.findOne(p1.getName())).willReturn(p1);
    	given(productServ.findOne("no")).willReturn(null); 
    	given(productServ.findOne(pS.getName())).willReturn(pS);
    	
    	given(productServ.getImage(p1)).willReturn(FILES_FOLDER.resolve(p1.getName()+"_photo"));

    }
    
    @Test
    public void testGetProducts() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }
    
    @Test
    public void testGetProductsSearch() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/products?search=p")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }
    
    @Test
    public void testGetProduct() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/products/PS")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("PS")));
    }
    @Test
    public void testGetProduct2() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/products/P1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("P1")));
    }
    
    @Test
    public void testGetProductNotFoundProduct() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/products/no")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetImage() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/products/P1/image")
                .contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testGetImageProductNoPhoto() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/products/PS/image")
                .contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetImageProductNotFound() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/products/no/image")
                .contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testPostImage() throws Exception{
        MockMultipartFile image = new MockMultipartFile("file", "", "application/json", "{\"image\": \"test.jpg\"}".getBytes());
        mvc.perform(
                MockMvcRequestBuilders.multipart("/api/products/P1/image")
                        .file(image)).andExpect(status().isCreated());
    }
    
    @Test
    public void testPostImageNull() throws Exception{
        MockMultipartFile image = new MockMultipartFile("null", "", "application/json", "{\"image\": \"test.jpg\"}".getBytes());
    	 mvc.perform(
                 MockMvcRequestBuilders.multipart("/api/products/P1/image")
                         .file(image)).andExpect(status().isBadRequest());
    }
    
    
    @Test
    public void testPostImageProductNotFound() throws Exception{
        MockMultipartFile image = new MockMultipartFile("file", "", "application/json", "{\"image\": \"test.jpg\"}".getBytes());
        mvc.perform(
                MockMvcRequestBuilders.multipart("/api/products/no/image")
                        .file(image)).andExpect(status().isNotFound());
    }
    
    @Test
    public void testPostProductSubM() throws Exception {
    	com.stripe.model.Product p = new com.stripe.model.Product();
    	SubscriptionCollection sc = new SubscriptionCollection();
    	List<Subscription> data = new ArrayList<>();
    	Subscription s = new Subscription();
    	Plan pl1 = new Plan();
    	pl1.setNickname("M");
    	s.setPlan(pl1);
    	data.add(s);
    	sc.setData(data);
    	
    	Product pS= new Product();
    	pS.setName("PS");
    	HashMap<String,Double> plans2 = new HashMap<>();
    	plans2.put("M", 10.0);
    	pS.setPlansPrices(plans2);

    	
    	given(productServ.findOne(pS.getName())).willReturn(null);
    	given(this.stripeServ.createProduct(any())).willReturn(p);
    	given(this.stripeServ.createPlan((HashMap<String,Object>)any())).willReturn(pl1);

    	mvc.perform(MockMvcRequestBuilders.post("/api/products/")
    			.content(jsonParser.toJson(pS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.plansPrices.M",is(10.0)));
    }
    
    @Test
    public void testPostProductSubD() throws Exception {
    	com.stripe.model.Product p = new com.stripe.model.Product();
    	SubscriptionCollection sc = new SubscriptionCollection();
    	List<Subscription> data = new ArrayList<>();
    	Subscription s = new Subscription();
    	Plan pl1 = new Plan();
    	pl1.setNickname("D");
    	s.setPlan(pl1);
    	data.add(s);
    	sc.setData(data);
    	
    	Product pS= new Product();
    	pS.setName("PS");
    	HashMap<String,Double> plans2 = new HashMap<>();
    	plans2.put("D", 10.0);
    	pS.setPlansPrices(plans2);
    	
    	given(productServ.findOne(pS.getName())).willReturn(null);
    	given(this.stripeServ.createProduct(any())).willReturn(p);
    	given(this.stripeServ.createPlan((HashMap<String,Object>)any())).willReturn(pl1);
 
    	mvc.perform(MockMvcRequestBuilders.post("/api/products/")
    			.content(jsonParser.toJson(pS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.plansPrices.D",is(10.0)));
    }
    
    @Test
    public void testPostProductSubA() throws Exception {
    	com.stripe.model.Product p = new com.stripe.model.Product();
    	SubscriptionCollection sc = new SubscriptionCollection();
    	List<Subscription> data = new ArrayList<>();
    	Subscription s = new Subscription();
    	Plan pl1 = new Plan();
    	pl1.setNickname("A");
    	s.setPlan(pl1);
    	data.add(s);
    	sc.setData(data);
    	
    	Product pS= new Product();
    	pS.setName("PS");
    	HashMap<String,Double> plans2 = new HashMap<>();
    	plans2.put("A", 10.0);
    	pS.setPlansPrices(plans2);

    	
    	given(productServ.findOne(pS.getName())).willReturn(null);
    	given(this.stripeServ.createProduct(any())).willReturn(p);
    	given(this.stripeServ.createPlan((HashMap<String,Object>)any())).willReturn(pl1);
 
    	mvc.perform(MockMvcRequestBuilders.post("/api/products/")
    			.content(jsonParser.toJson(pS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.plansPrices.A",is(10.0)));
    }
     
    @Test
    public void testPostProductSubMB() throws Exception {
    	com.stripe.model.Product p = new com.stripe.model.Product();
    	SubscriptionCollection sc = new SubscriptionCollection();
    	List<Subscription> data = new ArrayList<>();
    	Subscription s = new Subscription();
    	Plan pl1 = new Plan();
    	pl1.setNickname("MB");
    	s.setPlan(pl1);
    	data.add(s);
    	sc.setData(data);
    	
    	Product pS= new Product();
    	pS.setName("PS");
    	HashMap<String,Double> plans2 = new HashMap<>();
    	plans2.put("MB", 10.0);
    	pS.setPlansPrices(plans2);
    	
    	given(productServ.findOne(pS.getName())).willReturn(null);
    	given(this.stripeServ.createProduct(any())).willReturn(p);
    	given(this.stripeServ.createPlan((PlanCreateParams)any())).willReturn(pl1);
 
    	mvc.perform(MockMvcRequestBuilders.post("/api/products/")
    			.content(jsonParser.toJson(pS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.plansPrices.MB",is(10.0)));
    }
    
    @Test
    public void testPostProductLifetime() throws Exception {
    	com.stripe.model.Product p = new com.stripe.model.Product();
    	Product pS= new Product();
    	pS.setName("PL");
    	HashMap<String,Double> plans2 = new HashMap<>();
    	plans2.put("L", 10.0);
    	pS.setPlansPrices(plans2);    	
		Sku sku = new Sku();
 
     
    	given(productServ.findOne(pS.getName())).willReturn(null);
    	given(this.stripeServ.createProduct(any())).willReturn(p);
    	given(this.stripeServ.createSku(any())).willReturn(sku);
 
    	mvc.perform(MockMvcRequestBuilders.post("/api/products/")
    			.content(jsonParser.toJson(pS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name",is("PL")));
    }
    
    @Test
    public void testPostProductSubAlreadyExistingProduct() throws Exception {
    	Product pS= new Product();
    	pS.setName("PS");

    	mvc.perform(MockMvcRequestBuilders.post("/api/products/")
    			.content(jsonParser.toJson(pS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
    
    @Test
    public void testEditProduct () throws Exception{
    	com.stripe.model.Product p = new com.stripe.model.Product();

    	Product pPreChanged= new Product();
    	pPreChanged.setDescription("last Desc");
    	pPreChanged.setName("PS");
    	
    	Product pS= new Product();
    	pS.setName("PS");
    	pS.setProductStripeId("p_id");
    	pS.setDescription("new Desc");
    	HashMap<String,Double> plans2 = new HashMap<>();
    	plans2.put("MB", 10.0);
    	pS.setPlansPrices(plans2);
    	
    	given(this.productServ.findOne("PS")).willReturn(pPreChanged);
    	given(this.stripeServ.retrieveProduct("p_id")).willReturn(p);
    	given(this.stripeServ.updateProduct(any(), any())).willReturn(p);
    	given(this.productServ.save(pS)).willReturn(pS);
    	mvc.perform(MockMvcRequestBuilders.put("/api/products/")
    			.content(jsonParser.toJson(pS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.description",is("new Desc")));
    }
    
    @Test
    public void testEditProductNoContent () throws Exception{
    	Product pS= new Product();
    	pS.setName("no");

    	mvc.perform(MockMvcRequestBuilders.put("/api/products/")
    			.content(jsonParser.toJson(pS))
    			.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testDeleteProduct () throws Exception{
    	Product pS= new Product();
    	pS.setName("PS");
    	pS.setProductStripeId("p_id");
    	pS.setDescription("new Desc");
    	pS.setActive(true);
    	HashMap<String,Double> plans2 = new HashMap<>();
    	plans2.put("MB", 10.0);
    	pS.setPlansPrices(plans2);
    	
    	mvc.perform(MockMvcRequestBuilders.delete("/api/products/PS")
    			.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.active",is(false)));
    	
    }
    
    @Test
    public void testDeleteNoContent() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.delete("/api/products/no")
    			.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
}
