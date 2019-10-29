package tfg.licensoft.licenses;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
 public class LicenseService {
	
	
	@Autowired
	private LicenseRepository licRep;
	
	
	public License findOne(String serial) {
		return this.licRep.findBySerial(serial);
	}
	
	public License save (License license) {
		return this.licRep.save(license);
	}
	
	public void delete (License license) {
		this.licRep.delete(license);
	}
	
	public Page<License> findLicensesOfProduct(String product, Pageable page) {
		return this.licRep.findByProduct(product,page);
	}
	
	public License findBySerialAndProduct(String serial, String product) {
		return this.licRep.findBySerialAndProduct(serial, product);
	}
	
	public ArrayList<String> getSubTypes(){
		ArrayList<String> list = new ArrayList();
		list.add("A");
		list.add("M");
		list.add("L");
		return list;
	}
	
}
