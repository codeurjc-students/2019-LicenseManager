package tfg.licensoft.usedCards;


import org.springframework.data.jpa.repository.JpaRepository;



public interface UsedCardRepository extends JpaRepository<UsedCard, Long> {
	
	UsedCard findByLast4AndExpMonthAndExpYearAndProductName(int last4, int expMonth, int expYear, String product); 


}
