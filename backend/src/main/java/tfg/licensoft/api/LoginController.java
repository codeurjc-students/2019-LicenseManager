package tfg.licensoft.api;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserComponent;
import tfg.licensoft.users.UserService;

/**
 * This class is used to provide REST endpoints to logIn and logOut to the
 * service. These endpoints are used by Angular SPA client application.
 * 
 * NOTE: This class is not intended to be modified by app developer.
 */
@CrossOrigin 
@RestController
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);


	@Value("${spring.mail.host}")
	private String host;
	@Value("${spring.mail.port}")
	private int port;
	@Value("${spring.mail.username}")
	private String username;
	@Value("${spring.mail.password}")
	private String password;
	
	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private UserService userServ;
	

	
	@RequestMapping("/api/logIn")
	public ResponseEntity<User> logIn(HttpServletRequest req) {
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		System.out.println("??? ->  "+authorities + "  " + SecurityContextHolder.getContext().getAuthentication());

		if (!userComponent.isLoggedUser()) {
			log.info("Not user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			User loggedUser = userComponent.getLoggedUser();
			log.info("Logged as " + loggedUser.getName());
			//this.userServ.save(loggedUser);
			return new ResponseEntity<>(loggedUser, HttpStatus.OK);
		}
	}

	@RequestMapping("/api/logOut")
	public ResponseEntity<Boolean> logOut(HttpSession session) {
		if (!userComponent.isLoggedUser()) {
			log.info("No user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			session.invalidate();

			log.info("Logged out");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/api/register/{user}/{pass1}/{pass2}/{email}", method= RequestMethod.POST)
	public ResponseEntity<User> register(Model model, @PathVariable String user, @PathVariable String pass1,
			@PathVariable String pass2, @PathVariable String email,HttpServletRequest request, HttpServletResponse httpServletResponse) {
		User newUser =userServ.findByName(user);
		User newUserEmail =userServ.findByEmail(email);

		if ((pass1.equals(pass2)) && (newUser == null) && (newUserEmail == null)) {
			Map<String,Object> customerParameter = new HashMap<String,Object>();
			customerParameter.put("name", user);
			customerParameter.put("email",email);
			try {
				Customer customer = Customer.create(customerParameter);
				userServ.save(new User(email,customer.getId(),user, pass1,"ROLE_USER"));
			} catch (StripeException e) {
				e.printStackTrace();
			}
			try {
				request.login(user, pass1);
			} catch (ServletException e) {
				e.printStackTrace();
			}
			this.emailSender(email,user,pass1);
			return new ResponseEntity<User>(newUser, HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(newUser, HttpStatus.CONFLICT);
		}
		
		
	}
	
	private void emailSender(String email, String username,String pass) {
		try {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(this.host);
		mailSender.setPort(this.port);
		mailSender.setUsername(this.username);
		mailSender.setPassword(this.password);
		mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", "true");
		
		//Create mail instance
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("e.pina.2016@alumnos.urjc.es");
		mailMessage.setTo(email);
		mailMessage.setSubject("New account created on a Licensoft-Web");
		mailMessage.setText("Welcome to a Licensoft-Web! \n"
				+ "These are your credentials: \n" 
				+ "Username: " + username + "\n"
						+ "Password: "+ pass);
		
		mailSender.send(mailMessage);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value="/api/users", method=RequestMethod.GET)
	public ResponseEntity<Page<User>> getAllUsers(Pageable page){
		Page<User> users= userServ.findAll(page);
		return new ResponseEntity<Page<User>>(users,HttpStatus.OK);
	}
	
	@GetMapping("/api/getUserLogged")
	public ResponseEntity<User> getUserLogged( HttpServletRequest request) {
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		System.out.println("??? ->  "+authorities + "  " + SecurityContextHolder.getContext().getAuthentication());
		return new ResponseEntity<User>(this.userComponent.getLoggedUser(),HttpStatus.OK);

	}
	
}
