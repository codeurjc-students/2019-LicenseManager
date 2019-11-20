package tfg.licensoft;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

@SpringBootApplication
public class LicenSoftApp {

	public static void main(String[] args) throws StripeException {
		Stripe.apiKey = "sk_test_HXX6s2mN8JqMUytYfvkwB0dr00CfDzVlYA";
		SpringApplication.run(LicenSoftApp.class, args);
	}

} 
