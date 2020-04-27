package tfg.licensoft.products;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProductService {
	private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

	@Autowired
	private ProductRepository productRep;
	
	public Product findOne(String name) {
		return this.productRep.findByName(name);
	}
	
	public List<Product> findSearch(String search) {
		return productRep.findSearch(search);
	}
	
	public Product save (Product product) {
		return this.productRep.save(product);
	}
	
	public void delete (Product product) {
		this.productRep.delete(product);
	}
	
	public List<Product> findAll(){
		return this.productRep.findAll();
	}
	
	public List<Product> findAllActives(){
		return this.productRep.findByActive(true);
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
