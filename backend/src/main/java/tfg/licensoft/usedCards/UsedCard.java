package tfg.licensoft.usedCards;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class UsedCard {
	public int last4;
	public int expMonth;
	public int expYear;
	public String productName;
	
	public UsedCard() {}
	
	public UsedCard(int last4, int expMonth, int expYear, String productName) {
		super();
		this.last4 = last4;
		this.expMonth = expMonth;
		this.expYear = expYear;
		this.productName = productName;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	public int getLast4() {
		return last4;
	}
	public void setLast4(int last4) {
		this.last4 = last4;
	}
	public int getExpMonth() {
		return expMonth;
	}
	public void setExpMonth(int expMonth) {
		this.expMonth = expMonth;
	}
	public int getExpYear() {
		return expYear;
	}
	public void setExpYear(int expYear) {
		this.expYear = expYear;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	

	

}

