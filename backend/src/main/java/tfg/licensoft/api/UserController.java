package tfg.licensoft.api;

import tfg.licensoft.stripe.StripeCard;
import tfg.licensoft.stripe.StripeCardService;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserComponent;
import tfg.licensoft.users.UserService;

import org.springframework.web.bind.annotation.*;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

	@Autowired
	private UserService userServ;
	
	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private StripeCardService stripeCardServ;
	
	@PutMapping("/")
	private ResponseEntity<User> updateStripeCards(@RequestBody User updatedUser) {
		User user = userServ.findByName(updatedUser.getName());
		User userLogged = userComponent.getLoggedUser(); 
		if (user!=null && user.getId()==userLogged.getId()) {
			System.out.println("++++ " + user);
			user.getStripeCards().addAll(updatedUser.getStripeCards());
			userServ.save(user);
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}else {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/addCard")
	public ResponseEntity<StripeCard> addCard(@RequestBody StripeCard stripeCard) {
		User user = userComponent.getLoggedUser();
		try {
			Customer c = Customer.retrieve(user.getCustomerStripeId());
			Map<String,Object> cardParam = new HashMap<String,Object>();
			cardParam.put("number", stripeCard.getNumber());
			cardParam.put("cvc",stripeCard.getCvv());
			cardParam.put("exp_month",stripeCard.getExp_month());
			cardParam.put("exp_year",stripeCard.getExp_year());
			
			Map<String,Object> tokenParam = new HashMap<String,Object>();
			tokenParam.put("card", cardParam);
			
			Token token = Token.create(tokenParam);
			
			Map<String,Object> source = new HashMap<String,Object>();
			source.put("source", token.getId());
			
			c.getSources().create(source);
			
			stripeCard.setUser(user);
			this.stripeCardServ.save(stripeCard);
			
			return new ResponseEntity<StripeCard>(stripeCard,HttpStatus.OK);
		} catch (StripeException e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		
	}
	
	@GetMapping("/stripeCards")
	public Page<StripeCard> getCards(Pageable page){
		User user = userComponent.getLoggedUser();
		return this.stripeCardServ.findByUser(user, page);
	}
}
