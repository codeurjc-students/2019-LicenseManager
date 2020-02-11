package tfg.licensoft.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stripe.exception.StripeException;
import com.stripe.model.Plan;
import com.stripe.model.Sku;

import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/product")
public class ApiProductController {
	private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

	@Autowired
	private ProductService productServ;
	
	@Autowired
	private LicenseService licenseServ;
	
	
	@GetMapping("/all")
	public Page<Product> getProducts(HttpServletRequest req,Pageable page, @RequestParam Optional<String> search){
		if (!search.isPresent()) {
			return productServ.findAllActives(page);
		}else {
			return this.productServ.findSearch(page,search.get());
		}
		
	}

	@GetMapping("/{productName}") 
	public ResponseEntity<Product> getProduct(@PathVariable String productName) {
		Product p = this.productServ.findOne(productName);
		if (p != null) {
			System.out.println(p.getPlansPrices());
			return new ResponseEntity<Product>(p,HttpStatus.OK);
		}else {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@PostMapping("/")
	public ResponseEntity<Product> postProduct(@RequestBody Product product){
		if (this.productServ.findOne(product.getName())==null) {
			int count=0;
			HashMap<String,String> plans = new HashMap<>();
			product.setPlans(plans);
			String productId="";
			Map<String, Object> params = new HashMap<String, Object>();
			com.stripe.model.Product productStripe;
			for (Map.Entry<String, Double> plan : product.getPlansPrices().entrySet()) {
				if(count==0) {
					if (plan.getKey().equals("L")) {
						params.put("name", product.getName());
						params.put("type", "good");
						params.put("shippable", false);
						/*if(product.getDescription()!=null || product.getDescription()!="") {
							params.put("description",product.getDescription());
						}*/
						params.put("url", product.getWebLink());
					}else {
						params.put("name", product.getName());
						params.put("type", "service");
					}
					try {
						productStripe = com.stripe.model.Product.create(params);
						productId = productStripe.getId();
						product.setProductStripeId(productId);
						product.setPhotoAvailable(false);
						product.setPhotoSrc("");
						
					} catch (StripeException e) {
						e.printStackTrace();
					}
				}
			    switch(plan.getKey()) {
				    case "L":{
				    	this.createLproduct(product, plan.getValue(),productId);
				    	break;
				    }
				    case "M":{ 
				    	this.createMproduct(product, plan.getValue(),productId);
				    	break;
				    }
				    case "D":{
				    	this.createDproduct(product, plan.getValue(),productId);
				    	break;
				    }
				    case "A":{
				    	this.createAproduct(product, plan.getValue(), productId);
				    	break;
				    }
			    }
			    count++;
			}
			return new ResponseEntity<Product>(product,HttpStatus.OK);
		}else {
			return new ResponseEntity<Product>(HttpStatus.CONFLICT);
		}
	}
	
	
	@PutMapping("/")
	public ResponseEntity<Product> editProduct(@RequestBody Product product){
		Product p = this.productServ.findOne(product.getName());
		if (p==null) {
			return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
		}else{
			p.setDescription(product.getDescription());
			try {
				com.stripe.model.Product pStripe = com.stripe.model.Product.retrieve(p.getProductStripeId());
				Map<String, Object> params = new HashMap<>();
				params.put("description", product.getDescription());
				pStripe.update(params);
			} catch (StripeException e) {
				e.printStackTrace();
			}
			p.setPhotoAvailable(product.isPhotoAvailable());
			p.setPhotoSrc(product.getPhotoSrc());
			p.setWebLink(product.getWebLink());
			Product newP = this.productServ.save(p);
			return new ResponseEntity<Product>(newP,HttpStatus.OK); 
		}	
	}
	
	@DeleteMapping("/{productName}")
	public ResponseEntity<Product> deleteProduct(@PathVariable String productName){
		Product p = this.productServ.findOne(productName);
		System.out.println(productName);
		if(p==null) {
			return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
		}else {
			try {
				com.stripe.model.Product product =
						com.stripe.model.Product.retrieve(p.getProductStripeId());
						Map<String, Object> params = new HashMap<>();
						params.put("active", false);
						product.update(params);
						p.setActive(false);
						Path path = FILES_FOLDER.resolve(product.getName()+"_photo");
						Files.delete(path);
						p.setPhotoAvailable(false);
						this.productServ.save(p);
						return new ResponseEntity<Product>(p,HttpStatus.OK); 
			}catch(StripeException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}

	}


	
	
	
	private void createLproduct(Product product, double price, String productId ) {
		try {
			Map<String, Object> inventory = new HashMap<>();
			inventory.put("type", "infinite");
			Map<String, Object> paramsSku = new HashMap<>();
			paramsSku.put("price",(int)(price*100) );  //Hay que ponerlo en centimos (y en entero)
			paramsSku.put("inventory", inventory);
			paramsSku.put("currency", "eur");
			paramsSku.put("product",productId);
	 
			Sku sku = Sku.create(paramsSku);
			product.setSku(sku.getId());
			product.setProductStripeId(productId);
		}catch(StripeException e) {
			e.printStackTrace();
		}
		this.productServ.save(product);
	}
	
	
	//private methods to create plans
	private void createMproduct(Product product, double price, String productId) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("currency", "eur");
			params.put("interval", "month");
			params.put("product", productId);
			params.put("nickname", "M");
			params.put("amount", (int)(price*100));
			Plan plan1M = Plan.create(params);
			product.getPlans().put("M",plan1M.getId());
			
		}catch(StripeException e) {
			e.printStackTrace();
		}
		this.productServ.save(product);
	}
	
	private void createAproduct(Product product, double price, String productId) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("currency", "eur");
			params.put("interval", "year");
			params.put("product",productId);
			params.put("nickname", "A");
			params.put("amount", (int)(price*100));
			Plan plan1M = Plan.create(params);
			product.getPlans().put("A",plan1M.getId());
			
		}catch(StripeException e) {
			e.printStackTrace();
		}
		this.productServ.save(product);
	}
	
	private void createDproduct(Product product, double price, String productId) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("currency", "eur");
			params.put("interval", "day");
			params.put("product", productId);
			params.put("nickname", "D");
			params.put("amount", (int)(price*100));
			Plan plan1M = Plan.create(params);
			product.getPlans().put("D",plan1M.getId());
			
		}catch(StripeException e) {
			e.printStackTrace();
		}
		this.productServ.save(product);
	}
	
	
	
	
	//TODO
	@PutMapping("/{productName}")
	public ResponseEntity<Product> addLicenseToProduct(@PathVariable String productName, @RequestBody License license){
		Product p = this.productServ.findOne(productName);
		
		if (this.licenseServ.findBySerialAndProduct(license.getSerial(), p)!=null) {
			return new ResponseEntity<Product>(HttpStatus.CONFLICT);
		}else {		
			if (license.getProduct().equals(p) || p!=null) {
				license.setProduct(p);
			}else {
				return new ResponseEntity<Product>(HttpStatus.NOT_MODIFIED);
			}
			if (p!=null) {
				List<License> l = p.getLicenses();
				l.add(license);
				p.setLicenses(l);
				this.productServ.save(p);
				return new ResponseEntity<Product>(p,HttpStatus.OK);
			}else {
				return new ResponseEntity<Product>(HttpStatus.NOT_MODIFIED);
			}
		}
	}
	
	@PutMapping("/{productName}/license/{serial}")
	public ResponseEntity<Product> removeLicenseOfProduct(@PathVariable String productName, @PathVariable String serial){
		Product p = this.productServ.findOne(productName);
		License license = this.licenseServ.findBySerialAndProduct(serial, p);
		if (p!=null && license!=null) {
			List<License> l = p.getLicenses();
			l.remove(license);
			p.setLicenses(l);
			this.productServ.save(p);
			return new ResponseEntity<Product>(p,HttpStatus.OK);
		}else {
			return new ResponseEntity<Product>(HttpStatus.NOT_MODIFIED);
		}
	}
	
	@GetMapping(value = "/{productName}/image")
	public ResponseEntity<byte[]> getImage(@PathVariable String productName)throws IOException {
		Product p = this.productServ.findOne(productName);
		if (p != null) {
			if (!p.isPhotoAvailable()){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			byte[] bytes = Files.readAllBytes(productServ.getImage(p));
			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value = "/{productName}/image")
	public ResponseEntity<byte[]> postImage(@RequestBody MultipartFile file, @PathVariable String productName) throws Exception {
		if(file == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Product p = this.productServ.findOne(productName);

		if(p == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		this.productServ.saveImage(file,p);
		byte[] bytes = Files.readAllBytes(productServ.getImage(p));
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
	}
 
}
