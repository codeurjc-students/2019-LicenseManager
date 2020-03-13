package tfg.licensoft.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import tfg.licensoft.usedCards.UsedCard;
import tfg.licensoft.usedCards.UsedCardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/usedcards")
public class ApiUsedCardController {
	
	@Autowired
	private UsedCardService usedCardServ;
	
	
	@GetMapping("/checkUsed/{last4}/{expMonth}/{expYear}/product/{product}")
	public ResponseEntity<Boolean> checkIfUsed(@PathVariable int last4, @PathVariable int expMonth, @PathVariable int expYear, @PathVariable String product){
		UsedCard c = this.usedCardServ.findByLast4AndExpMonthAndExpYearAndProductName(last4, expMonth, expYear,product);
		if(c!=null) {
			return new ResponseEntity<>(true,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(false,HttpStatus.OK);
		}
	}
	
	@PostMapping("/{last4}/{expMonth}/{expYear}/product/{product}")
	public ResponseEntity<UsedCard> postUsedCard(@PathVariable int last4, @PathVariable int expMonth, @PathVariable int expYear, @PathVariable String product){
		UsedCard card = new UsedCard(last4,expMonth,expYear,product);
		UsedCard cardN = this.usedCardServ.save(card);
		return new ResponseEntity<>(cardN,HttpStatus.OK);
	}


}
