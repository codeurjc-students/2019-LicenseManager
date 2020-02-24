package tfg.licensoft;


import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

@Component
public class DatabaseInitializer {


	@Value("${stripe.privateKey}")
	String privateKey;
	

	
	@PostConstruct
	public void init() throws StripeException {
		//Set up stripe key 
		Stripe.apiKey=privateKey;
		
		//You can set here default values to be charged when the server starts running
	}


}
