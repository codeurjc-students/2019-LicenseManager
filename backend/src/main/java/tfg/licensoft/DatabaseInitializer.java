package tfg.licensoft;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;

@Component
public class DatabaseInitializer {



	@Autowired
	UserService userServ;

	@Autowired
	LicenseService licenseServ;

	@PostConstruct
	public void init() {

	
		// users
		userServ.save(new User("customer", "c", "ROLE_USER"));
		userServ.save(new User("user", "u", "ROLE_USER"));
		userServ.save(new User("admin", "a", "ROLE_USER", "ROLE_ADMIN"));	
		
		//licenses
		licenseServ.save(new License("XX-X1",false,"A","sw"));
		licenseServ.save(new License("XX-X2",true,"A","sw"));
		licenseServ.save(new License("XX-X3",true,"M","sw"));
		licenseServ.save(new License("XX-X4",true,"L","sw"));
		licenseServ.save(new License("XX-X1",false,"A","hw"));
		licenseServ.save(new License("XX-X2",true,"M","hw"));
		licenseServ.save(new License("XX-X3",false,"A","hw"));

	}

}
