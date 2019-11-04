package tfg.licensoft;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	public void init() {

	
		// users
		userServ.save(new User("customer", "c", "ROLE_USER"));
		userServ.save(new User("user", "u", "ROLE_USER"));
		userServ.save(new User("C1", "c1", "ROLE_USER"));

		userServ.save(new User("admin", "a", "ROLE_USER", "ROLE_ADMIN"));	
		
		
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
