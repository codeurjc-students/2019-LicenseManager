package tfg.licensoft.licenses;

import java.util.ArrayList;

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
	
	public ArrayList<String> getSubTypes(){
		ArrayList<String> list = new ArrayList();
		list.add("A");
		list.add("M");
		list.add("L");
		return list;
	}
	
	public Page<License> findByUsername(String username, Pageable page){
		if (this.userServ.findByName(username)!=null) {
			return this.licRep.findByOwner(username, page);
		}else {
			return null;
		}
	}
	
}
