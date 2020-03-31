package tfg.licensoft;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import tfg.licensoft.licencheck.ApiLicencheckController;
import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.licenses.LicenseSubscription;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.statistics.LicenseStatistics;
import tfg.licensoft.statistics.LicenseStatisticsService;
import tfg.licensoft.stripe.StripeServices;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class LicencheckApiTests {

	
	@Autowired
	private MockMvc mvc;
    @Spy
	private final ApiLicencheckController licencheckController = new ApiLicencheckController();
    
    @MockBean
    private LicenseService licenseService;
    
    @MockBean
    private ProductService prodServ;
    
    @MockBean
    private LicenseStatisticsService licenseStatServ;
    
    @MockBean
    private StripeServices stripeServ;
    
    @Before
    public void initialize() {
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
    	LicenseSubscription lSubsMB2 = new LicenseSubscription(true,"MB",pS,"test",0);
    	lSubsMB2.setSerial("smb2");
    	lSubsMB2.setnUsage(5);
    	LicenseSubscription lSubsMFree = new LicenseSubscription(true,"M",pS,"test",10);
    	lSubsMFree.setSerial("smfree");

    	
    	List<License> licsSubs = new ArrayList<>();
    	licsSubs.add(lSubsMFree);
    	licsSubs.add(lSubsA);
    	licsSubs.add(lSubsD);
    	licsSubs.add(lSubsMB);
    	licsSubs.add(lSubsM);
    	Page<License> licsSubsPage = new PageImpl<License>(licsSubs);

    	
    	given(prodServ.findOne(p1.getName())).willReturn(p1);
    	given(prodServ.findOne("no")).willReturn(null); 
    	given(prodServ.findOne(pS.getName())).willReturn(pS);
    	
    	given(licenseService.findBySerialAndProductAndActive("smb2", pS, true)).willReturn(lSubsMB2);

    	given(licenseService.findBySerialAndProductAndActive("sm", pS, true)).willReturn(lSubsM);
    	given(licenseService.findBySerialAndProductAndActive("smb", pS, true)).willReturn(lSubsMB);
    	given(licenseService.findBySerialAndProductAndActive("smb", p1, true)).willReturn(null);

    }
    
    @Test
    public void testCheckLicense() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/licencheck/checkLicense/PS/smb")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serial",is("smb")));
    }
    
    @Test
    public void testCheckLicenseLicenseNull() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/licencheck/checkLicense/P1/smb")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void testCheckLicenseProductNull() throws Exception{
    	mvc.perform(MockMvcRequestBuilders.get("/licencheck/checkLicense/no/smb")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void testUpdateUsageFirstTime() throws Exception {
    	LicenseStatistics lStat = new LicenseStatistics();
    	given(this.licenseStatServ.findByLicenseAndIp(any(), any())).willReturn(null);

    	mvc.perform(MockMvcRequestBuilders.put("/licencheck/updateUsage/1/PS/smb")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string("1"));
    	mvc.perform(MockMvcRequestBuilders.put("/licencheck/updateUsage/1/PS/sm")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    	mvc.perform(MockMvcRequestBuilders.put("/licencheck/updateUsage/1/PS/no")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    
    @Test
    public void testUpdateUsageNotFirstTime() throws Exception {
    	LicenseStatistics lStat = new LicenseStatistics();
    	lStat.setnUsage(5);
    	List<LicenseStatistics> lStats = new ArrayList<>();
    	lStats.add(lStat);
    	given(this.licenseStatServ.findByLicenseAndIp(any(), any())).willReturn(lStats);

    	mvc.perform(MockMvcRequestBuilders.put("/licencheck/updateUsage/2/PS/smb2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string("7"));
    	
    }
    
}
