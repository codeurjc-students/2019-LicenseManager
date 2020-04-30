	package tfg.licensoft;

import org.junit.Before;
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
import org.junit.Test;

import com.google.gson.Gson;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Customer.InvoiceSettings;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntent.NextAction;
import com.stripe.model.PaymentIntent.NextActionRedirectToUrl;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.Plan;
import com.stripe.model.SetupIntent;
import com.stripe.model.StripeError;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.stripe.model.SubscriptionItem;
import com.stripe.model.SubscriptionItemCollection;
import com.stripe.param.SubscriptionCreateParams;

import tfg.licensoft.api.UserController;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.licenses.LicenseSubscription;
import tfg.licensoft.licenses.LicenseSubscriptionService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.stripe.StripeServices;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class UserApiTests {
	
	@Autowired
	private MockMvc mvc;
	
    @Spy
	private final UserController userController = new UserController();
    
    
    @MockBean
    private StripeServices stripeServ;
    
    @MockBean
    private UserService userServ;
    
    @MockBean
    private LicenseService licenseServ;
    
    @MockBean
    private LicenseSubscriptionService licenseSubsServ;
    
    @MockBean
    private ProductService productServ;
    
    private Gson jsonParser = new Gson();

    
    @Before
    public void initialize() throws StripeException {
    	User user = new User("test@gmail.com","cus_id1","user","t","ROLE_ADMIN","ROLE_USER");
    	user.setCustomerStripeId("c_stId");
    	User user2 = new User("test2@gmail.com","cus_id1","test","t","ROLE_ADMIN","ROLE_USER");
    	
    	Customer c1 = new Customer();
    	c1.setId("c_stId");
    	InvoiceSettings invoiceSettings = new InvoiceSettings();
    	invoiceSettings.setDefaultPaymentMethod("pm_id");
    	c1.setInvoiceSettings(invoiceSettings);
    	
    	PaymentMethod pM = new PaymentMethod();
    	pM.setId("pm_id");
    	
    	SetupIntent si = new SetupIntent();
    	
    	Subscription s1 = new Subscription();
    	s1.setId("subs_id");
    	s1.setStatus("active");
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
    	
    	SubscriptionItemCollection sic = new SubscriptionItemCollection();
    	SubscriptionItem sItem = new SubscriptionItem();
    	sItem.setId("sItem_m");
    	List<SubscriptionItem> data2 = new ArrayList<>();
    	data2.add(sItem);
    	sic.setData(data2);
    	s1.setItems(sic);
    	
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
    	HashMap<String,String> plans = new HashMap<>();
    	plans.put("M", "pm_id");
    	plans.put("D", "pd_id");
    	plans.put("A", "pa_id");
    	plans.put("MB", "pmb_id");

    	pS.setPlans(plans);
    	
    	
    	Product p1 = new Product();
    	p1.setActive(true);
    	p1.setDescription("Desc p1");
    	p1.setName("P1");
    	p1.setPhotoAvailable(false);
    	HashMap<String,Double> plansL = new HashMap<>();
    	plansL.put("L", 12.0);
    	p1.setPlansPrices(plansL);
    	p1.setProductStripeId("p1_stripeId");
    	p1.setSku("sku_p1");
    	p1.setTrialDays(0);
    	p1.setWebLink("p1.com");
    	
    	PaymentIntent piSucceeded = new PaymentIntent();
    	piSucceeded.setId("pi_id_succeeded");
    	piSucceeded.setStatus("succeeded");
    	
    	PaymentIntent piNotSucceeded = new PaymentIntent();
    	piNotSucceeded.setId("pi_id_not_succeeded");
    	piNotSucceeded.setStatus("not_succeeded");
    	
    	
    	
    	given(this.userServ.findByName("user")).willReturn(user);
    	given(this.userServ.findByName("test")).willReturn(user2);

    	given(this.stripeServ.createPaymentMethod(any())).willReturn(pM);
    	given(this.stripeServ.createSetupIntent(any())).willReturn(si);
    	
    	given(this.stripeServ.retrieveCustomer(any())).willReturn(c1);
    	
    	given(this.productServ.findOne("PS")).willReturn(pS);
    	given(this.productServ.findOne("P1")).willReturn(p1);

    	given(this.stripeServ.createSubscription((HashMap)any())).willReturn(s1);
    	given(this.stripeServ.createSubscription((SubscriptionCreateParams)any())).willReturn(s1);
    	
    	given(this.stripeServ.createPaymentIntent(any())).willReturn(piSucceeded);
    	
    	given(this.stripeServ.retrievePaymentIntent("pi_id_succeeded")).willReturn(piSucceeded);
    	given(this.stripeServ.retrievePaymentIntent("pi_id_not_succeeded")).willReturn(piNotSucceeded);

    	
    	LicenseSubscription licS = new LicenseSubscription(true,"D",pS,"user",0);
    	given(this.licenseSubsServ.findBySerialAndProduct(any(), any())).willReturn(licS);
    	
    	PaymentMethodCollection pmc = new PaymentMethodCollection();
    	PaymentMethod pm1 = new PaymentMethod();
    	pm1.setId("pm1_id");
    	PaymentMethod pm2 = new PaymentMethod();
    	pm2.setId("pm2_id");
    	List<PaymentMethod> data3 = new ArrayList<>();
    	data3.add(pm1);
    	data3.add(pm2);
    	pmc.setData(data3);
    	given(this.stripeServ.getPaymentMethodCollection(any())).willReturn(pmc);
    	
    	
    	s1.setDefaultPaymentMethod("pm1_id");
    	given(this.stripeServ.retrieveSubscription("subId")).willReturn(s1);
    	
    	
    	Subscription s2 = new Subscription();
    	s2.setId("s2_id");
    	s2.setDefaultPaymentMethod("pm2_id");
    	given(this.stripeServ.updateSubscription(any(), any())).willReturn(s2);


    }
    
    
    @Test
    public void testAddCardStripeElements() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/addCard/tok_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.response",is("pm_id")));
    }
    
    @Test
    public void testAddCardStripeElementsUnauthorized() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/no/addCard/tok_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testAddCardStripeElementsForbidden() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/test/addCard/tok_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    
    @Test
    public void testSetDefaultPaymentMethod() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/user/default-card/pm_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string("true"));
    }
    
    @Test
    public void testSetDefaultPaymentMethodUnauthorized() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/no/default-card/pm_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()).andExpect(content().string("false"));
    }
    
    @Test
    public void testSetDefaultPaymentMethodForbidden() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/test/default-card/pm_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andExpect(content().string("false"));
    }
    
    
    @Test
    public void testGetDefaultPaymentMethod() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/user/default-card")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.response",is("pm_id")));
    }
    
    @Test
    public void testGetDefaultPaymentMethodUnauthorized() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/no/default-card")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testGetDefaultPaymentMethodForbidden() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/test/default-card")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    
    @Test
    public void testAddTrial() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/user/products/PS/addTrial/cards/tok_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testAddTrialUnauthorized() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/no/products/PS/addTrial/cards/tok_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testAddTrialForbidden() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/test/products/PS/addTrial/cards/tok_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    
    @Test
    public void testAddTrialProductNotFound() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/user/products/no/addTrial/cards/tok_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testAddSubscriptionActive() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/user/products/PS/M/addSubscription/renewal/true/paymentMethods/pm_card_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testAddSubscriptionIncomplete() throws Exception{
    	Subscription s = new Subscription();
    	s.setStatus("incomplete");
    	s.setLatestInvoice("id_latInv");
    	given(this.stripeServ.createSubscription((HashMap)any())).willReturn(s);
    	Invoice inv = new Invoice();
    	inv.setPaymentIntent("id_pi");
    	given(this.stripeServ.getLatestInvoice(any())).willReturn(inv);
    	
    	PaymentIntent piRequires = new PaymentIntent();
    	piRequires.setId("piRequires");
    	piRequires.setStatus("requires_action"); //3DSecure
    	NextAction nextAction = new NextAction();
    	NextActionRedirectToUrl redirectToUrl = new NextActionRedirectToUrl();
    	redirectToUrl.setUrl("url_mocked");
    	nextAction.setRedirectToUrl(redirectToUrl);
    	piRequires.setNextAction(nextAction);
    	given(this.stripeServ.retrievePaymentIntent(any())).willReturn(piRequires);
    	given(this.stripeServ.confirmPaymentIntent(any(), any())).willReturn(piRequires);


    	mvc.perform(MockMvcRequestBuilders.put("/api/users/user/products/PS/M/addSubscription/renewal/true/paymentMethods/pm_card_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.type",is("RequiresAction")));
    	
    	//Testing if incomplete, not bc requires action
    	given(this.stripeServ.retrievePaymentIntent(any())).willReturn(null);
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/user/products/PS/M/addSubscription/renewal/true/paymentMethods/pm_card_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void testAddSubscriptionUnauthorized() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/no/products/PS/M/addSubscription/renewal/true/paymentMethods/pm_card_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testAddSubscriptionForbidden() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/test/products/PS/M/addSubscription/renewal/true/paymentMethods/pm_card_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    
    @Test
    public void testAddSubscriptionProductNotFound() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.put("/api/users/user/products/no/M/addSubscription/renewal/true/paymentMethods/pm_card_visa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetCardsFromUser() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/user/cards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id",is("pm1_id")));
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/test/cards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/no/cards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    	
    }
    
    @Test
    public void testDeleteStripeCard() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.delete("/api/users/user/card/pm1_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.response",is("true")));
    	mvc.perform(MockMvcRequestBuilders.delete("/api/users/test/card/pm1_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    	mvc.perform(MockMvcRequestBuilders.delete("/api/users/no/card/pm1_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testPayment() throws Exception {
    	Product p1 = new Product();
    	p1.setActive(true);
    	p1.setDescription("Desc p1");
    	p1.setName("P1");
    	p1.setPhotoAvailable(false);
    	HashMap<String,Double> plansL = new HashMap<>();
    	plansL.put("L", 12.0);
    	p1.setPlansPrices(plansL);
    	p1.setProductStripeId("p1_stripeId");
    	p1.setSku("sku_p1");
    	p1.setTrialDays(0);
    	p1.setWebLink("p1.com");
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/paymentIntent/tok_visa")
    			.content(jsonParser.toJson(p1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/test/paymentIntent/tok_visa")
    			.content(jsonParser.toJson(p1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/no/paymentIntent/tok_visa")
    			.content(jsonParser.toJson(p1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testConfirmPaymentSuccededAndUserAuth() throws Exception {
    	PaymentIntent piSucceed = new PaymentIntent();
    	piSucceed.setId("piSucceed");
    	piSucceed.setStatus("succeeded");
    	given(this.stripeServ.confirmPaymentIntent(any(), any())).willReturn(piSucceed);
    	given(this.stripeServ.retrievePaymentIntent(any())).willReturn(piSucceed);

    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/confirm/pi_id/products/P1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.product.name",is("P1")));
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/test/confirm/pi_id/products/P1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/no/confirm/pi_id/products/P1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());	
    }
    
    @Test
    public void testConfirmPaymentRequiresAction() throws Exception {
    	PaymentIntent piRequires = new PaymentIntent();
    	piRequires.setId("piRequires");
    	piRequires.setStatus("requires_action"); //3DSecure
    	NextAction nextAction = new NextAction();
    	NextActionRedirectToUrl redirectToUrl = new NextActionRedirectToUrl();
    	redirectToUrl.setUrl("url_mocked");
    	nextAction.setRedirectToUrl(redirectToUrl);
    	piRequires.setNextAction(nextAction);
    	given(this.stripeServ.confirmPaymentIntent(any(), any())).willReturn(piRequires);
    	given(this.stripeServ.retrievePaymentIntent(any())).willReturn(piRequires);

    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/confirm/pi_id/products/P1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.type",is("RequiresAction")));	
    }
    
    @Test
    public void testConfirmPaymentErrors() throws Exception {
    	PaymentIntent piRequires = new PaymentIntent();
    	piRequires.setId("piRequires");
    	piRequires.setStatus("requires_payment_method"); //Failed pm
    	
    	StripeError lastPaymentError = new StripeError();
    	lastPaymentError.setMessage("Failed PM_ Msg Mocked");
    	piRequires.setLastPaymentError(lastPaymentError);
    	given(this.stripeServ.confirmPaymentIntent(any(), any())).willReturn(piRequires);
    	given(this.stripeServ.retrievePaymentIntent("pi_id")).willReturn(piRequires);

    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/confirm/pi_id/products/P1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());	
    	
    	
    	PaymentIntent piError = new PaymentIntent();
    	piError.setId("piError");
    	piError.setStatus("other");
    	given(this.stripeServ.confirmPaymentIntent(any(), any())).willReturn(piError);
    	
    	//PaymentIntent not found
    	given(this.stripeServ.retrievePaymentIntent(any())).willReturn(null);

    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/confirm/pi_id/products/P1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());	
    }
    
    
    
    @Test
    public void testConfirmPaymentNotFound() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/confirm/pi_id/products/no")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/confirm/pi_notEx/products/P1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testConfirm3DsPaymentResponseLifetime() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/paymentIntents/pi_id_succeeded/products/PS")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.type",is("L")));
    }
    
    @Test
    public void testConfirm3DsPaymentResponseSubs() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/paymentIntents/pi_id_succeeded/products/PS")
                .contentType(MediaType.APPLICATION_JSON)
                .param("automaticRenewal", "true")
                .param("subscriptionId", "subId")
                .param("type", "D"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.type",is("D")));
    }
    
    @Test
    public void testConfirm3DsPaymentResponseUserCheckFails() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/test/paymentIntents/pi_id_succeeded/products/PS")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/no/paymentIntents/pi_id_succeeded/products/PS")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());	
    }
    
    
    @Test
    public void testConfirm3DsPaymentResponseNotSucceeded() throws Exception{
    	given(this.stripeServ.cancelPaymentIntent(any())).willReturn(null);
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/paymentIntents/pi_id_not_succeeded/products/PS")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed());
    }
    
    @Test 
    public void testGetPaymentMethodOfSubscription() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/user/subscriptions/subId/paymentMethod")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.response",is("pm1_id")));
    }
    
    @Test 
    public void testGetPaymentMethodOfSubscriptionNotFound() throws Exception{
    	given(this.stripeServ.retrieveSubscription(any())).willReturn(null);
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/user/subscriptions/subId/paymentMethod")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test 
    public void testGetPaymentMethodOfSubscriptionCheckUserFails() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/test/subscriptions/subId/paymentMethod")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/no/subscriptions/subId/paymentMethod")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());	
    }
    
    @Test
    public void testPostPaymentMethodOfSubscription() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/subscriptions/subId/paymentMethods/pm1_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.response",is("pm2_id")));
    }
    
    @Test
    public void testPostPaymentMethodOfSubscriptionNotFound() throws Exception{
    	given(this.stripeServ.retrieveSubscription(any())).willReturn(null);
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/user/subscriptions/subId/paymentMethods/pm1_id")
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isNotFound());
    }
    
    @Test
    public void testPostPaymentMethodOfSubscriptionCheckUserFails() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/test/subscriptions/subId/paymentMethods/pm1_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    	mvc.perform(MockMvcRequestBuilders.post("/api/users/no/subscriptions/subId/paymentMethods/pm1_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());	
    }
    
}
