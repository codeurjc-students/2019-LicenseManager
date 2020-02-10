package tfg.licensoft.products;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tfg.licensoft.licenses.License;

@Service
public class ProductService {
	private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

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
	
	public void saveImage(MultipartFile file, Product product) throws Exception{
		File uploadedFile = new File(FILES_FOLDER.toFile(), product.getName()+"_photo");
		file.transferTo(uploadedFile);
		product.setPhotoAvailable(true);
		this.save(product);
	}
	
	public Path getImage(Product product) {
		return FILES_FOLDER.resolve(product.getName()+"_photo");
	}

}
