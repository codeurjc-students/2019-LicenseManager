package tfg.licensoft.api;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.licensoft.licenses.*;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.stripe.StripeServices;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/licenses")
public class ApiLicenseController {

	@Autowired 
	private LicenseService licServ;
	
	@Autowired
	private LicenseSubscriptionService licenseSubsServ;
	@Autowired
	private ProductService productServ;
	@Autowired
	private StripeServices stripeServ;
	
	
	@Autowired 
	private UserService userServ; 

	@GetMapping(value = "/products/{product}")
	public ResponseEntity<List<License>> getLicensesOfProduct(@PathVariable String product){
		Product p = this.productServ.findOne(product);
		if(p==null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<License> licenses = this.licServ.findLicensesOfProduct(p);
		return new ResponseEntity<>(licenses, HttpStatus.OK);
		 
	}
	
	@GetMapping(value = "/{serial}/products/{product}")
	public ResponseEntity<License> getLicenseBySerialAndProduct(@PathVariable String serial, @PathVariable String product){
		Product p = this.productServ.findOne(product);
		License license = this.licServ.findBySerialAndProduct(serial, p);
		if (license!=null) {
			return new ResponseEntity<License>(license, HttpStatus.OK);
		}else {
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);
		}
	} 
	

	@GetMapping(value = "/users/{userName}")
	public ResponseEntity<List<License>> getLicensesOfUser(@PathVariable String userName){
		User user = this.userServ.findByName(userName);
		
		if(user==null) {
			return new ResponseEntity<List<License>>(HttpStatus.NOT_FOUND);
		}else { 
			List<License> licenses = this.licServ.findByUsername(userName); 
			return new ResponseEntity<List<License>>(licenses,HttpStatus.OK);
		}
	} 
	 
	@PutMapping(value="/cancelAtEnd/{serial}/products/{product}")
	public ResponseEntity<License> cancelAtEndLicense(@PathVariable String serial, @PathVariable String product){
		Product p = this.productServ.findOne(product);
		LicenseSubscription l = this.licenseSubsServ.findBySerialAndProduct(serial,p);
		if(l!=null && p!=null) {
			boolean newCancelAtEnd = !l.getCancelAtEnd();
			try {
				Map<String, Object> params = new HashMap<>();
				params.put("cancel_at_period_end", newCancelAtEnd);
				Subscription s = this.stripeServ.retrieveSubscription(l.getSubscriptionId());
				this.stripeServ.updateSubscription(s, params);
				l.setCancelAtEnd(newCancelAtEnd);
				this.licServ.save(l);
				
				
			} catch (StripeException e) {
				e.printStackTrace();
				return new ResponseEntity<License>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<License>(l,HttpStatus.OK);
		}else {
			return new ResponseEntity<License>(HttpStatus.PRECONDITION_REQUIRED);
		}
	}

	
	@GetMapping(value="/users/{userName}/products/{productName}")
	public ResponseEntity<List<License>> getLicensesOfUserAndProduct(@PathVariable String productName, @PathVariable String userName){
		Product p = this.productServ.findOne(productName);
		User user = this.userServ.findByName(userName);
		
		
		if(p==null || user==null) { 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		List<License> licenses=this.licServ.findByProductAndOwner(p, userName);
		return new ResponseEntity<>(licenses,HttpStatus.OK);		
	}
	
}
