package tfg.licensoft.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tfg.licensoft.licenses.*;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/licenses")
public class ApiLicenseController {

	@Autowired 
	private LicenseService licServ;
	
	@Autowired
	private ProductService productServ;
	

	@PostMapping(value = "/")
	public ResponseEntity<License> postLicense(@RequestBody License license){
		if (this.licServ.getSubTypes().contains(license.getType()) || this.productServ.findOne(license.getProduct().getName())!=null) {
			License l = this.licServ.findOne(license.getSerial());
			if (l==null || l.getProduct()!=license.getProduct()) {
				this.licServ.save(license);
				Product p =this.productServ.findOne(license.getProduct().getName());
				p.getLicenses().add(license);
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
		Product p = this.productServ.findOne(product);
		Page<License> licenses = this.licServ.findLicensesOfProduct(p,page);
		return licenses;
		
	}
	
	@GetMapping(value = "/{serial}/{product}")
	public ResponseEntity<License> getLicenseBySerialAndProduct(@PathVariable String serial, @PathVariable String product){
		Product p = this.productServ.findOne(product);
		License license = this.licServ.findBySerialAndProduct(serial, p);
		if (license!=null) {
			return new ResponseEntity<License>(license, HttpStatus.OK);
		}else {
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping(value = "/{serial}/{product}")
	public ResponseEntity<License> deleteLicenseBySerial(@PathVariable String serial, @PathVariable String product){
		Product p = this.productServ.findOne(product);
		License license = this.licServ.findBySerialAndProduct(serial,p);
		if (license!=null) {
			this.licServ.delete(license);
			return new ResponseEntity<License>(license, HttpStatus.OK);
		}else {
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value = "/{userName}")
	public ResponseEntity<Page<License>> getLicensesOfUser(@PathVariable String userName, Pageable page){
		Page<License> licenses = this.licServ.findByUsername(userName, page);
		if(licenses==null) {
			return new ResponseEntity<Page<License>>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<Page<License>>(licenses,HttpStatus.OK);
		}
	}
	
	
}
