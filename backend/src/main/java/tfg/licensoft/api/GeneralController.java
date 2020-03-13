package tfg.licensoft.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class GeneralController {
	
	private class Response{
		private String text;
		
		public String getText() {
			return this.text;
		}
		
		public void setText(String te) {
			this.text=te;
		}
	}
	
	@Value("${stripe.publicKey}")
	String publicKey;
	
	@Value("${appName}")
	String appName;
	
	
	
	@GetMapping("keys/stripe/public")
	private ResponseEntity<Response> getPublicStripeKey() {
		Response key= new Response();
		key.setText(publicKey);
		if(publicKey!=null) {
			return new ResponseEntity<>(key,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/appName")
	private ResponseEntity<Response> getAppName() {
		Response r= new Response();
		r.setText(this.appName);
		if(appName!=null) {
			return new ResponseEntity<>(r, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
