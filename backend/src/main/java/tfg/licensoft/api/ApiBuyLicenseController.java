package tfg.licensoft.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/buy")
public class ApiBuyLicenseController {
	
	
	@RequestMapping("/{productName}")
	public void buyProduct(@PathVariable String productName) {
		
	}

}
