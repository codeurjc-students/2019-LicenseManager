package tfg.licensoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Sku;
import com.stripe.net.StripeResponse;

import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;

@Component
public class DatabaseInitializer {

	
	@Value("${stripe.privateKey}")
	String privateKey;
	
	@Value("${adminName}")
	String adminName;

	
	@Value("${adminPass}")
	String adminPass;

	
	@Value("${adminEmail}")
	String adminEmail;


	@Autowired
	UserService userServ;

	@Autowired
	LicenseService licenseServ;
	
	@Autowired
	ProductService productServ;

	@PostConstruct
	public void init() throws StripeException {

		//Set up stripe key  
		Stripe.apiKey=privateKey;
		
		//User ADMIN creator
		if(this.userServ.findByName(this.adminName)==null) {
			Map<String,Object> customerParameter = new HashMap<String,Object>();
			customerParameter.put("name", this.adminName);
			customerParameter.put("email",this.adminEmail);
			Customer customer = Customer.create(customerParameter);
			userServ.save(new User(this.adminEmail,customer.getId(),this.adminName, this.adminPass,"ROLE_USER","ROLE_ADMIN"));
		}
		
	}

}