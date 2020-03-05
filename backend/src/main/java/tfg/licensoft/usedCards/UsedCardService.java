package tfg.licensoft.usedCards;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UsedCardService {
	
	@Autowired
	private UsedCardRepository usedCardRep;
	
	public UsedCard save(UsedCard usedCard) {
		return this.usedCardRep.save(usedCard);
	}
	
	public UsedCard findByLast4AndExpMonthAndExpYearAndProductName(int last4, int expMonth, int expYear, String product) {
		return this.usedCardRep.findByLast4AndExpMonthAndExpYearAndProductName(last4, expMonth, expYear,product);
	}
	
	public UsedCard delete(UsedCard usedCard) {
		return this.delete(usedCard);
	}
	
	

}
