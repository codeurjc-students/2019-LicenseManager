package tfg.licensoft.stripe;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Invoice;
import com.stripe.model.StripeObject;

import tfg.licensoft.tools.EmailSenderTool;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;

@CrossOrigin
@RestController
@RequestMapping(value = "/licensoft/webhooks")
public class Webhook {
	
	@Autowired
	private EmailSenderTool emailSender;
	
	@Autowired
	private UserService userServ;
	
	private String signature = "whsec_fi6MewiJG5JY1Dx6CbXpFVEuR46IEdAD";
	
	@PostMapping()
	public ResponseEntity<StripeObject> handle(@RequestHeader("Stripe-Signature") String headers,HttpServletRequest request,@RequestBody String body) {		
		Event event = null;
		

		  try {
			    event = com.stripe.net.Webhook.constructEvent(
			    		body, headers, "whsec_fi6MewiJG5JYlDx6CbXpFVEuR46IEdAD"
			    );
		  }catch (SignatureVerificationException e) {
			    e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		  }
		  
		  EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
		  StripeObject stripeObject = null;
		  if (dataObjectDeserializer.getObject().isPresent()) {
		    stripeObject = dataObjectDeserializer.getObject().get();
		  } else {
		    try {
				stripeObject = 		  event.getDataObjectDeserializer().deserializeUnsafe();
			} catch (EventDataObjectDeserializationException e) {
				e.printStackTrace();
			}
		  }
		  System.out.println(stripeObject);
		  System.out.println(event.getType());
		  User user = null;
		  switch(event.getType()) {
		  		
		  		case "charge.failed":{
		  			Charge charge = (Charge)stripeObject;
		  			System.out.println(charge);
		  			user = this.userServ.findByCustomerStripeId(charge.getCustomer());
		  			this.emailSender.sendMsgEmail("A charge has failed", charge.toJson(), user.getEmail());
		  			break;
		  		}
		  		case "invoice.payment_failed":{
		  			Invoice invoice = (Invoice)stripeObject;
		  			System.out.println(invoice);
		  			user = this.userServ.findByCustomerStripeId(invoice.getCustomer());
		  			this.emailSender.sendMsgEmail("A charge has failed", invoice.toJson(), user.getEmail());
		  			break;
		  		}
		  		
		  }
		  
		  
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
