package tfg.licensoft;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;

@Component
public class DatabaseInitializer {



	@Autowired
	UserService userServ;

	@Autowired
	LicenseService licenseServ;
	
	@Autowired
	ProductService productServ;

	@PostConstruct
	public void init() throws StripeException {


	
		// users
		Customer c1 = Customer.retrieve("cus_GCfRnHS286gdq9");
		userServ.save(new User(c1.getId(),"customer", "c", "ROLE_USER"));
	

		Customer c2 = Customer.retrieve("cus_GCfRldTX850sXN");
		userServ.save(new User(c2.getId(),"user", "u", "ROLE_USER"));
		

		Customer c3 = Customer.retrieve("cus_GCfRu2cBr7Xkkh");
		userServ.save(new User(c3.getId(),"C1", "c1", "ROLE_USER"));

		

		Customer c4 = Customer.retrieve("cus_GCfRVJhDGwsjpi");
		userServ.save(new User(c4.getId(),"admin", "a", "ROLE_USER", "ROLE_ADMIN"));	
		
		
		//products
		Product p1 = this.productServ.save(new Product("sw"));
		Product p2 =  this.productServ.save(new Product("hw"));
		
		//licenses
		licenseServ.save(new License("XX-X1",false,"A",p1,null));
		licenseServ.save(new License("XX-X2",true,"A",p1,"Customer"));
		licenseServ.save(new License("XX-X3",true,"M",p1, "C1"));
		licenseServ.save(new License("XX-X4",true,"L",p1,"C1"));
		licenseServ.save(new License("XX-X1",false,"A",p2,null));
		licenseServ.save(new License("XX-X2",true,"M",p2,"C1"));
		licenseServ.save(new License("XX-X3",false,"A",p2,null));

	}

}
