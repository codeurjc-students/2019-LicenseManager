package tfg.licensoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

@SpringBootApplication
public class LicenSoftApp {
	
	public static void main(String[] args) throws StripeException {
		/*System.out.println(args[1]);
		if(args[1].contains("sk_")) {
			Stripe.apiKey = args[1];  //Secret API Key passed by arguments
			SpringApplication.run(LicenSoftApp.class, args);
		}else {
			System.out.println("Secret Stripe key must be passed by params (args[1])");
			return;
		}*/
		
		Stripe.apiKey = "sk_test_HXX6s2mN8JqMUytYfvkwB0dr00CfDzVlYA"; 
		SpringApplication.run(LicenSoftApp.class, args);
	}

} 
