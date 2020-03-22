package tfg.licensoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.stripe.exception.StripeException;

@SpringBootApplication
public class LicenSoftApp{
	

	
	
	public static void main(String[] args) throws StripeException {
		SpringApplication.run(LicenSoftApp.class, args);
	}


} 
