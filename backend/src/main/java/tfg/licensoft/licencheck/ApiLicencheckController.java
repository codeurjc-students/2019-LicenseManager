package tfg.licensoft.licencheck;

import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.UsageRecord;
import com.stripe.net.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.licenses.LicenseSubscription;
import tfg.licensoft.licenses.LicenseSubscriptionService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.statistics.LicenseStatistics;
import tfg.licensoft.statistics.LicenseStatisticsService;
import tfg.licensoft.stripe.StripeServices;
import tfg.licensoft.users.UserService;
import org.springframework.web.bind.annotation.*;

//This controller will serve an external API, which will serve an external program that wants to use LicenSoft

@CrossOrigin
@RestController
@RequestMapping(value = "/licencheck/")
public class ApiLicencheckController {
	
	
	@Autowired
	private LicenseService licenseService;
	
	@Autowired
	private LicenseStatisticsService licenseStatService;
	
	@Autowired
	private StripeServices stripeServ;
	
	@Autowired
	private ProductService productService;

	
	@RequestMapping("checkLicense/{productName}/{licenseSerial}")
	public ResponseEntity<License> checkLicense(@PathVariable String licenseSerial, @PathVariable String productName ) {
		Product product = this.productService.findOne(productName);

		if (product==null) {
			System.out.println("Product is null");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		License license = this.licenseService.findBySerialAndProductAndActive(licenseSerial, product,true);

		if (license==null ) {
			System.out.println("License is null: " +license);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<>(license,HttpStatus.OK);
		}
	}
	
	//It checks the license too
	@PutMapping("updateUsage/{usage}/{productName}/{licenseSerial}")
	public ResponseEntity<Integer> updateUsage(@PathVariable int usage,@PathVariable String licenseSerial, @PathVariable String productName , HttpServletRequest request,  @RequestParam Optional<String> userName) {
		LicenseSubscription l ;
		try {
			l=(LicenseSubscription) this.checkLicense(licenseSerial, productName).getBody();
		}catch (ClassCastException c) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(l==null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		
		//Only permitted for MB subscribe (for now)
		/*if(!l.getType().equals("MB")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}*/
		
		if(l.getType().equals("MB")) {

			long unixTime = System.currentTimeMillis() / 1000L; 
	
			Map<String, Object> usageRecordParams = new HashMap<String, Object>();
			usageRecordParams.put("quantity", usage);
			usageRecordParams.put("timestamp", unixTime);
			usageRecordParams.put("action", "increment");
			
			
	
			//To avoid problems if Connection Errors (duplications, etc)
			RequestOptions options = RequestOptions
					  .builder()
					  .setIdempotencyKey(UUID.randomUUID().toString())
					  .build();
	
			
			try {
	
				this.stripeServ.createOnSubscriptionItem(l.getSubscriptionItemId(), usageRecordParams, options);
			} catch (StripeException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	
			} 
		}
		
		l.setnUsage(l.getnUsage()+usage);
		License newL = this.licenseService.save(l);
		
		String user;
		//Statistics for UserName&IP&License
		if(userName.isPresent()) {
			user = userName.get();
		}else {
			user = null;
		}
		LicenseStatistics lStats = this.licenseStatService.findByLicenseAndIpAndUserName(l, request.getRemoteAddr(), user);
		if(lStats==null) {
			lStats = new LicenseStatistics(newL);
			System.out.println("Creating new");
		}
		
		lStats.setnUsage(lStats.getnUsage()+usage);
		lStats.setIp(request.getRemoteAddr());
		lStats.getUsages().add(new Date());					
		lStats.setUserName(user);	
		
		String formatedDate =lStats.getFormattedDate(new Date());
		
		//String formatedDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		if (lStats.getUsagePerTime().containsKey(formatedDate)) {
			lStats.getUsagePerTime().put(formatedDate, lStats.getUsagePerTime().get(formatedDate)+usage);
		}else {
			lStats.getUsagePerTime().put(formatedDate, usage);
		}
		this.licenseStatService.save(lStats);
				
		return new ResponseEntity<>(l.getnUsage(),HttpStatus.OK);

	} 
	
	

	
}
