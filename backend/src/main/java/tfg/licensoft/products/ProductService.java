package tfg.licensoft.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfg.licensoft.licenses.License;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRep;
	
	public Product findOne(String name) {
		return this.productRep.findByName(name);
	}
	
	public Product save (Product product) {
		return this.productRep.save(product);
	}
	
	public void delete (Product product) {
		this.productRep.delete(product);
	}

}
