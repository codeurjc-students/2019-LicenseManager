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
public class GeneralController implements IGeneralController{
	
	public class Response{
		private String response;
		
		public String getResponse() {
			return this.response;
		}
		
		public void setResponse(String te) {
			this.response=te;
		}
	}
	
	@Value("${stripe.publicKey}")
	String publicKey;
	
	@Value("${appName}")
	String appName;
	
	@Value("${app.domain}")
	String appDomain;
	
	@Value("${licencheck.keys.private}")
	String privateKey;
	
	@GetMapping("/keys/stripe/public")
	public ResponseEntity<Response> getPublicStripeKey() {
		Response key= new Response();
		key.setResponse(publicKey);
		if(publicKey!=null) {
			return new ResponseEntity<>(key,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/appName")
	public ResponseEntity<Response> getAppName() {
		Response r= new Response();
		r.setResponse(this.appName);
		if(appName!=null) {
			return new ResponseEntity<>(r, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/appDomain")
	public ResponseEntity<Response> getAppDomain() {
		Response r= new Response();
		r.setResponse(this.appDomain);
		if(appDomain!=null) {
			return new ResponseEntity<>(r, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	/*
	@GetMapping("/privateKey")
	public ResponseEntity<Response> getPrivateKeyPath() {
		System.out.println("-----------------------ACTION---------------------------");
		Response r= new Response();
		r.setResponse(this.privateKey);
		if(privateKey!=null) {
			return new ResponseEntity<>(r, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}*/
}
