package tfg.licensoft.licenses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	public Page<License> findLicensesOfProduct(Product product, Pageable page) {
		return this.licRep.findByProduct(product,page);
	}
	
	public License findBySerialAndProduct(String serial, Product product) {
		return this.licRep.findBySerialAndProduct(serial, product);
	}
	
	public List<License> findByProductAndOwnerAndType(Product product,String owner,String type) {
		return this.licRep.findByProductAndOwnerAndType(product, owner, type);
	}
	
	public ArrayList<String> getSubTypes(){
		ArrayList<String> list = new ArrayList();
		list.add("A");
		list.add("M");
		list.add("L");
		list.add("D");
		return list;
	}
	
	public Page<License> findByUsername(String username, Pageable page){
		if (this.userServ.findByName(username)!=null) {
			return this.licRep.findByOwner(username, page);
		}else {
			return null;
		}
	}
	
	public List<License> findByProductAndActiveAndOwner(Product product, boolean active, String owner){
		return this.licRep.findByProductAndActiveAndOwner(product, active,owner);
	}
	
	public License findBySerialAndProductAndOwner(String serial, Product product, String owner) {
		return this.licRep.findBySerialAndProductAndOwner(serial, product, owner);
	}
	
}
