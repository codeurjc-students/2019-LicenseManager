package tfg.licensoft.licencheck;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.users.UserService;
import org.springframework.web.bind.annotation.*;

//This controller will serve an external API, which will serve an external program that wants to use LicenSoft

@CrossOrigin
@RestController
@RequestMapping(value = "/licencheck/")
public class ApiLicencheckController {
	
	
	@Autowired
	private LicenseService licenseService;
	
	@Autowired
	private ProductService productService;
	
	/*
	@RequestMapping("checkAccount")
	public ResponseEntity checkLicensoftAccount(@RequestBody BasicUser basicUser) {
		System.out.println(basicUser.getUserName() + " - " + basicUser.getPassword());
		User user = userService.findByName(basicUser.getUserName());
		if (user == null) {
			System.out.println("Null user");
			return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(new BCryptPasswordEncoder().matches(basicUser.getPassword(), user.getPasswordHash())) {
			System.out.println("Good pass");
			return new ResponseEntity(HttpStatus.OK);
		}else {
			System.out.println("Wrong pass");
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
	}
	
	*/
	//TODO Remove the password, not necessary on this method. 
	@RequestMapping("checkLicense/{productName}/{licenseSerial}")
	public ResponseEntity<License> checkLicense(@PathVariable String licenseSerial, @PathVariable String productName ) {
		Product product = this.productService.findOne(productName);

		if (product==null) {
			System.out.println("Product is null");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		License license = this.licenseService.findBySerialAndProductAndActive(licenseSerial, product,true);

		if (license==null ) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<>(license,HttpStatus.OK);
		}
	}

	
}
