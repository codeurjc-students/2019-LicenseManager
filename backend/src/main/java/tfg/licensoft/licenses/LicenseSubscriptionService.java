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
public class LicenseSubscriptionService {
	@Autowired
	private LicenseSubscriptionRepository licRep;
	
	@Autowired 
	private UserService userServ;
	
	
	public LicenseSubscription findOne(String serial) {
		return this.licRep.findBySerial(serial);
	}
	
	public LicenseSubscription save (LicenseSubscription license) {
		return this.licRep.save(license);
	}
	
	public void delete (LicenseSubscription license) {
		this.licRep.delete(license);
	}
	
	public Page<LicenseSubscription> findLicensesOfProduct(Product product, Pageable page) {
		return this.licRep.findByProduct(product,page);
	}
	
	public LicenseSubscription findBySerialAndProduct(String serial, Product product) {
		return this.licRep.findBySerialAndProduct(serial, product);
	}
	
	public List<LicenseSubscription> findByProductAndOwnerAndType(Product product,String owner,String type) {
		return this.licRep.findByProductAndOwnerAndType(product, owner, type);
	}
	
	
	public Page<LicenseSubscription> findByUsername(String username, Pageable page){
		if (this.userServ.findByName(username)!=null) {
			return this.licRep.findByOwner(username, page);
		}else {
			return null;
		}
	}
	
	public List<LicenseSubscription> findByProductAndActiveAndOwner(Product product, boolean active, String owner){
		return this.licRep.findByProductAndActiveAndOwner(product, active,owner);
	}
	
	public LicenseSubscription findBySerialAndProductAndOwnerAndActive(String serial, Product product, String owner,boolean active) {
		return this.licRep.findBySerialAndProductAndOwnerAndActive(serial, product, owner,active);
	}
	
	public LicenseSubscription findBySerialAndProductAndActive(String serial, Product product, boolean active) {
		return this.licRep.findBySerialAndProductAndActive(serial, product,active);
	}
	
	
	public Page<LicenseSubscription> findByProductAndOwner(Product product, String owner, Pageable page){
		return this.licRep.findByProductAndOwner(product, owner, page);
	}
	
}
