package tfg.licensoft.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import tfg.licensoft.licenses.License;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRep;
	
	public Product findOne(String name) {
		return this.productRep.findByName(name);
	}
	
	public Page<Product> findSearch(Pageable page, String search) {
		return productRep.findSearch(page, search);
	}
	
	public Product save (Product product) {
		return this.productRep.save(product);
	}
	
	public void delete (Product product) {
		this.productRep.delete(product);
	}
	
	public Page<Product> findAll(Pageable page){
		return this.productRep.findAll(page);
	}
	
	public Page<Product> findAllActives(Pageable page){
		return this.productRep.findByActive(true,page);
	}

}
