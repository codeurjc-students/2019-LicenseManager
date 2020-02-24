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
@RequestMapping(value = "/api/keys")
public class GeneralController {
	
	private class Key{
		private String key;
		
		public String getKey() {
			return this.key;
		}
		
		public void setKey(String k) {
			this.key=k;
		}
	}
	
	@Value("${stripe.publicKey}")
	String publicKey;
	
	@GetMapping("/stripe/public")
	private ResponseEntity<Key> getPublicStripeKey() {
		Key key= new Key();
		key.setKey(publicKey);
		if(publicKey!=null) {
			return new ResponseEntity<>(key,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
