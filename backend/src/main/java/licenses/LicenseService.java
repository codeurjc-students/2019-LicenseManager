package licenses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
 class LicenseService {
	
	@Autowired
	private LicenseRepository licRep;
	
	
	public License findOne(String serial) {
		return this.licRep.findBySerial(serial);
	}
}
