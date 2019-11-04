package tfg.licensoft.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tfg.licensoft.licenses.License;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/product")
public class ApiProductController {
	
	@Autowired
	private ProductService productServ;

	@GetMapping("/{productName}")
	public ResponseEntity<Product> getProduct(@PathVariable String productName) {
		Product p = this.productServ.findOne(productName);
		if (p != null) {
			return new ResponseEntity<Product>(p,HttpStatus.OK);
		}else {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@PostMapping("/")
	public ResponseEntity<Product> postProduct(@RequestBody Product product){
		if (this.productServ.findOne(product.getName())==null) {
			this.productServ.save(product);
			return new ResponseEntity<Product>(product,HttpStatus.OK);
		}else {
			return new ResponseEntity<Product>(HttpStatus.CONFLICT);
		}
	}
	
	@PutMapping("/{productName}")
	public ResponseEntity<Product> addLicenseToProduct(@PathVariable String productName, @RequestBody License license){
		Product p = this.productServ.findOne(productName);
		if (p!=null) {
			List<License> l = p.getLicenses();
			l.add(license);
			p.setLicenses(l);
			this.productServ.save(license.getProduct());
			this.productServ.save(p);
			return new ResponseEntity<Product>(p,HttpStatus.OK);
		}else {
			return new ResponseEntity<Product>(HttpStatus.NOT_MODIFIED);
		}
		
	}
 
}
