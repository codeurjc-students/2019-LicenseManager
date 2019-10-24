package tfg.licensoft;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;

@Component
public class DatabaseInitializer {



	@Autowired
	UserService userServ;



	@PostConstruct
	public void init() {

	
		// users
		userServ.save(new User("user", "12345", "ROLE_USER"));
		userServ.save(new User("student", "12345", "ROLE_USER"));
		userServ.save(new User("teacher", "12345", "ROLE_USER", "ROLE_ADMIN"));				
	}

}
