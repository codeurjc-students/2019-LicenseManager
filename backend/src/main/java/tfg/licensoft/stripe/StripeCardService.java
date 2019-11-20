package tfg.licensoft.stripe;

import org.springframework.stereotype.Service;

import tfg.licensoft.users.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class StripeCardService {
	
	@Autowired
	private StripeCardRepository stripeCardRep;
	
	public Page<StripeCard> findByUser(User user, Pageable page){
		return this.stripeCardRep.findByUser(page,user);
	}
	
	public StripeCard save(StripeCard card) {
		return this.stripeCardRep.save(card);
	}
}
