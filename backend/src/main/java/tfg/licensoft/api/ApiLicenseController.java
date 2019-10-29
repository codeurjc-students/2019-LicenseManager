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
		if (this.licServ.getSubTypes().contains(license.getType())) {
			License l = this.licServ.findOne(license.getSerial());
			if (l==null || l.getProduct()!=license.getProduct()) {
				this.licServ.save(license);
				return new ResponseEntity<>(license, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@GetMapping(value = "/product/{product}")
	public Page<License> getLicensesOfProduct(@PathVariable String product, Pageable page){
		Page<License> licenses = this.licServ.findLicensesOfProduct(product,page);
		return licenses;
		
	}
	
	@GetMapping(value = "/{serial}/{product}")
	public ResponseEntity<License> getLicenseBySerialAndProduct(@PathVariable String serial, @PathVariable String product){
		License license = this.licServ.findBySerialAndProduct(serial, product);
		if (license!=null) {
			return new ResponseEntity<License>(license, HttpStatus.OK);
		}else {
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping(value = "/{serial}/{product}")
	public ResponseEntity<License> deleteLicenseBySerial(@PathVariable String serial, @PathVariable String product){
		License license = this.licServ.findBySerialAndProduct(serial,product);
		if (license!=null) {
			this.licServ.delete(license);
			return new ResponseEntity<License>(license, HttpStatus.OK);
		}else {
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);
		}
	}
	
	
}
