package tfg.licensoft.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tfg.licensoft.licenses.*;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/license")
public class ApiLicenseController {

	@Autowired 
	private LicenseService licServ;
	

	@PostMapping(value = "/")
	public ResponseEntity<License> postLicense(@RequestBody License license){
		if (this.licServ.findOne(license.getSerial())==null) {
			this.licServ.save(license);
			return new ResponseEntity<>(license, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping(value = "/product/{product}")
	public Page<License> getLicensesOfProduct(@PathVariable String product, Pageable page){
		Page<License> licenses = this.licServ.findLicensesOfProduct(product,page);
		return licenses;
		
	}
	
	@GetMapping(value = "/serial/{serial}")
	public ResponseEntity<License> getLicenseBySerial(@PathVariable String serial){
		License license = this.licServ.findOne(serial);
		if (license!=null) {
			return new ResponseEntity<License>(license, HttpStatus.OK);
		}else {
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping(value = "/{serial}")
	public ResponseEntity<License> deleteLicenseBySerial(@PathVariable String serial){
		License license = this.licServ.findOne(serial);
		if (license!=null) {
			this.licServ.delete(license);
			return new ResponseEntity<License>(license, HttpStatus.OK);
		}else {
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);
		}
	}
	
	
}
