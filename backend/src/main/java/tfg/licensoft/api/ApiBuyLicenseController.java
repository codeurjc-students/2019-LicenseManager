package tfg.licensoft.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.PaymentIntentCreateParams;

import tfg.licensoft.users.User;
import tfg.licensoft.users.UserComponent;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/stripe")
public class ApiBuyLicenseController {
	@Autowired
	private UserComponent userComponent;
/* DE MOMENTO LO DEJAMOS EN USER CONTROLLER
	@RequestMapping("/addCard")
	public ResponseEntity addCard() {
		User user = userComponent.getLoggedUser();
		System.out.println(user.getCustomerStripeId());
		try {
			Customer c = Customer.retrieve(user.getCustomerStripeId());
			Map<String,Object> cardParam = new HashMap<String,Object>();
			cardParam.put("number", "4242424242424242");
			cardParam.put("cvc","999");
			cardParam.put("exp_month","12");
			cardParam.put("exp_year","2020");
			
			Map<String,Object> tokenParam = new HashMap<String,Object>();
			tokenParam.put("card", cardParam);
			
			Token token = Token.create(tokenParam);
			
			Map<String,Object> source = new HashMap<String,Object>();
			source.put("source", token.getId());
			
			c.getSources().create(source);
			
			return new ResponseEntity(HttpStatus.OK);
		} catch (StripeException e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		
	}*/
 
}
