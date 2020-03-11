package tfg.licensoft.api;


import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.licensoft.licenses.*;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserComponent;
import tfg.licensoft.users.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
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
	private UserService userServ;

	@GetMapping(value = "/products/{product}")
	public Page<License> getLicensesOfProduct(@PathVariable String product, Pageable page){
		Product p = this.productServ.findOne(product);
		Page<License> licenses = this.licServ.findLicensesOfProduct(p,page);
		return licenses;
		
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
	public ResponseEntity<Page<License>> getLicensesOfUser(@PathVariable String userName, Pageable page){
		Page<License> licenses = this.licServ.findByUsername(userName, page);
		if(licenses==null) {
			return new ResponseEntity<Page<License>>(HttpStatus.NOT_FOUND);
		}else { 
			return new ResponseEntity<Page<License>>(licenses,HttpStatus.OK);
		}
	}
	
	@PutMapping(value="/cancelAtEnd/{serial}/products/{product}")
	public ResponseEntity<License> cancelAtEndLicense(@PathVariable String serial, @PathVariable String product){
		Product p = this.productServ.findOne(product);
		LicenseSubscription l = this.licenseSubsServ.findBySerialAndProduct(serial,p);
		boolean newCancelAtEnd = !l.getCancelAtEnd();
		if(l!=null && p!=null) {
			User u = this.userServ.findByName(l.getOwner());
			try {
				Customer c = Customer.retrieve(u.getCustomerStripeId());
				
				for(Subscription s:c.getSubscriptions().getData()) {
					System.out.println(s.getPlan().getNickname() + " ->");
					if(s.getPlan().getNickname().equals(l.getType())) {
							com.stripe.model.Product productStripe = com.stripe.model.Product.retrieve(s.getPlan().getProduct());
							if(productStripe.getName().equals(p.getName())){
								Map<String, Object> params = new HashMap<>();
								params.put("cancel_at_period_end", newCancelAtEnd);
								//s.cancel(params);
								s.update(params);
								l.setCancelAtEnd(newCancelAtEnd);
								this.licServ.save(l);
							}

					}
					

				}
				
			} catch (StripeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new ResponseEntity<License>(l,HttpStatus.OK);
		}else {
			return new ResponseEntity<License>(HttpStatus.PRECONDITION_REQUIRED);
		}
	}

	
	@GetMapping(value="/users/{userName}/products/{productName}")
	public ResponseEntity<Page<License>> getLicensesOfUserAndProduct(@PathVariable String productName, @PathVariable String userName, Pageable page){
		Product p = this.productServ.findOne(productName);
		User user = this.userServ.findByName(userName);
		
		
		if(p==null) { 
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		Page<License> licenses=this.licServ.findByProductAndOwner(p, userName, page);
		return new ResponseEntity<>(licenses,HttpStatus.OK);		
	}
	
}
