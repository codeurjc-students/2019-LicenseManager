package tfg.licensoft.licenses;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfg.licensoft.products.Product;
import tfg.licensoft.users.UserService;

@Service
 public class LicenseService {
	
	
	@Autowired
	private LicenseRepository licRep;
	
	@Autowired 
	private UserService userServ;
	 
	
	public License findOne(String serial) {
		return this.licRep.findBySerial(serial);
	}
	
	public License save (License license) {
		return this.licRep.save(license);
	}
	
	public void delete (License license) {
		this.licRep.delete(license);
	}
	
	public List<License> findLicensesOfProduct(Product product) {
		return this.licRep.findByProduct(product);
	}
	
	public License findBySerialAndProduct(String serial, Product product) {
		return this.licRep.findBySerialAndProduct(serial, product);
	}
	
	public List<License> findByProductAndOwnerAndType(Product product,String owner,String type) {
		return this.licRep.findByProductAndOwnerAndType(product, owner, type);
	}
	
	
	public List<License> findByUsername(String username){
		if (this.userServ.findByName(username)!=null) {
			return this.licRep.findByOwner(username);
		}else {
			return null;
		}
	}
	
	public List<License> findByProductAndActiveAndOwner(Product product, boolean active, String owner){
		return this.licRep.findByProductAndActiveAndOwner(product, active,owner);
	}
	
	public License findBySerialAndProductAndOwnerAndActive(String serial, Product product, String owner,boolean active) {
		return this.licRep.findBySerialAndProductAndOwnerAndActive(serial, product, owner,active);
	}
	
	public License findBySerialAndProductAndActive(String serial, Product product, boolean active) {
		return this.licRep.findBySerialAndProductAndActive(serial, product,active);
	}
	
	
	public List<License> findByProductAndOwner(Product product, String owner){
		return this.licRep.findByProductAndOwner(product, owner);
	}
	
}
