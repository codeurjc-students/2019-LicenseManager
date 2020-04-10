package tfg.licensoft.api;

import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.licenses.LicenseSubscription;
import tfg.licensoft.licenses.LicenseSubscriptionService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.stripe.StripeServices;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;

import org.springframework.web.bind.annotation.*;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.SetupIntent;
import com.stripe.model.Subscription;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceItemCreateParams;
import com.stripe.param.SubscriptionCreateParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

	@Autowired
	private LicenseService licenseServ;
	
	@Autowired
	private LicenseSubscriptionService licenseSubsServ;
	
	@Autowired
	private UserService userServ;
	
	@Autowired
	private ProductService productServ;
	
	@Autowired
	private StripeServices stripeServ;
	
	class SimpleResponse{
		private String response;
		
		

		public SimpleResponse(String response) {
			super();
			this.response = response;
		}

		public String getResponse() {
			return response;
		}

		public void setResponse(String response) {
			this.response = response;
		}
		
	}
	
	public User getRequestUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return this.userServ.findByName(auth.getName());
	}
	
	@PostMapping("{userName}/addCard/{tokenId}")
	public ResponseEntity<SimpleResponse> addCardStripeElements(@PathVariable String userName,@PathVariable String tokenId){
		User user = this.userServ.findByName(userName);
		
		// We don't know which user wants to affect -> Unauthorized
		if(user==null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// User to be affected != User that made the request
		if(!user.equals(this.getRequestUser())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		try {
			//Create PaymentMethod
			Map<String, Object> card = new HashMap<>();
		
			card.put("token", tokenId);

			Map<String, Object> params = new HashMap<>();
			params.put("type", "card");
			params.put("card", card);
	
			PaymentMethod paymentMethod = this.stripeServ.createPaymentMethod(params);
			
			params.clear();
			
			List<Object> paymentMethodTypes =  new ArrayList<>();
			paymentMethodTypes.add("card");
			params.put("payment_method_types", paymentMethodTypes);
			params.put("customer", user.getCustomerStripeId());
			params.put("payment_method", paymentMethod.getId());
			params.put("confirm", true);

			this.stripeServ.createSetupIntent(params);
			
			params.clear();
			params.put("customer", user.getCustomerStripeId());
			this.stripeServ.attachPaymentMethod(paymentMethod, params);
			
			this.setDefaultPaymentMethod(userName, paymentMethod.getId());
			
			SimpleResponse response = new SimpleResponse(paymentMethod.getId());
			return new ResponseEntity<SimpleResponse>(response,HttpStatus.OK);

		} catch (StripeException e) {
			e.printStackTrace();
			return new ResponseEntity<SimpleResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("{userName}/default-card/{paymentMethodId}")
	public ResponseEntity<Boolean> setDefaultPaymentMethod(@PathVariable String userName, @PathVariable String paymentMethodId) {
		try {
			User user = this.userServ.findByName(userName);
			// We don't know which user wants to affect -> Unauthorized
			if(user==null) {
				return new ResponseEntity<Boolean>(false,HttpStatus.UNAUTHORIZED);
			}
			// User to be affected != User that made the request
			if(!user.equals(this.getRequestUser())) {
				return new ResponseEntity<>(false,HttpStatus.FORBIDDEN);
			}
			
			Customer c =this.stripeServ.retrieveCustomer(user.getCustomerStripeId());
			
			Map<String, Object> params = new HashMap<>();
			params.put("default_payment_method", paymentMethodId);
			
			Map<String, Object> params2 = new HashMap<>();
			params2.put("invoice_settings", params);
			this.stripeServ.updateCustomer(c, params2);

			return new ResponseEntity<Boolean>(true,HttpStatus.OK);

		}catch(StripeException e ) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("{userName}/default-card")
	public ResponseEntity<SimpleResponse> getDefaultPaymentMethod(@PathVariable String userName) {
		User user = this.userServ.findByName(userName);
		
		// We don't know which user wants to affect -> Unauthorized
		if(user==null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// User to be affected != User that made the request
		if(!user.equals(this.getRequestUser())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		Customer c;
		try {
			c = this.stripeServ.retrieveCustomer(user.getCustomerStripeId());
			SimpleResponse r = new SimpleResponse(c.getInvoiceSettings().getDefaultPaymentMethod());
			
			return new ResponseEntity<>(r,HttpStatus.OK);

		} catch (StripeException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	//Will be subscribed to a M subscription with x free trial days
	@PutMapping("/{userName}/products/{productName}/addTrial/cards/{token}")
	public ResponseEntity<License> addTrial(@PathVariable String productName, @PathVariable String userName,  @PathVariable String token){
		
		User user = this.userServ.findByName(userName);
		Product product = this.productServ.findOne(productName);
		
		
		// We don't know which user wants to affect -> Unauthorized
		if(user==null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// User to be affected != User that made the request
		if(!user.equals(this.getRequestUser())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if(product!=null) {
			try {
				String pM = this.addCardStripeElements(userName,token).getBody().getResponse();

				SubscriptionCreateParams params =
						  SubscriptionCreateParams.builder()
						    .setCustomer(user.getCustomerStripeId())
						    .addItem(
						      SubscriptionCreateParams.Item.builder()
						        .setPlan(product.getPlans().get("M")) //Free trial is always monthly subscription
						        .build())
						    .setTrialPeriodDays((long)product.getTrialDays())
						    .setDefaultPaymentMethod(pM)
						    .build();
				Subscription subscription = this.stripeServ.createSubscription(params);
				
				LicenseSubscription license = new LicenseSubscription(true, "M", product, user.getName(),product.getTrialDays());
				license.setCancelAtEnd(false);  //Trial Periods have automatic renewal
				license.setSubscriptionItemId(subscription.getItems().getData().get(0).getId());
				license.setSubscriptionId(subscription.getId());
				licenseServ.save(license);
				this.setTimerAndEndDate(license.getSerial(),license.getProduct(),product.getTrialDays());
				return new ResponseEntity<License>(license,HttpStatus.OK);

			}catch(StripeException e) {
				e.printStackTrace();
				return new ResponseEntity<License>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);

		}
	}


	
	@PutMapping("{userName}/products/{productName}/{typeSubs}/addSubscription/renewal/{automaticRenewal}/paymentMethods/{pmId}")
	public ResponseEntity<License> addSubscription(@PathVariable String productName, @PathVariable String typeSubs, @PathVariable String userName, @PathVariable boolean automaticRenewal, @PathVariable String pmId){

		
		Product product = this.productServ.findOne(productName);
		User user = this.userServ.findByName(userName);
		
		// We don't know which user wants to affect -> Unauthorized
		if(user==null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// User to be affected != User that made the request
		if(!user.equals(this.getRequestUser())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		//Check for MB subs if user has a payment method attached
		List<PaymentMethod> lPayments = this.getCardsFromUser(userName).getBody();
		if(typeSubs.equals("MB") && lPayments!=null && lPayments.size()==0) {
			return new ResponseEntity<License>(HttpStatus.PRECONDITION_REQUIRED); //The precondition is to have an attached payment source
		}
		
		String planId=null;
		if(product!=null) {
			Map<String,String> plans = product.getPlans();		 
			planId = plans.get(typeSubs);
		}
		if (planId!=null || product!=null) {
				Map<String, Object> item = new HashMap<>();
				item.put("plan", planId);
				Map<String, Object> items = new HashMap<>();
				items.put("0", item);
				Map<String, Object> expand = new HashMap<>();
				expand.put("0", "latest_invoice.payment_intent");
				Map<String, Object> params = new HashMap<>();
				params.put("customer", user.getCustomerStripeId());
				params.put("items", items);
				if(!pmId.equals("default")) {
					params.put("default_payment_method", pmId);
				}
				params.put("expand", expand);
				params.put("cancel_at_period_end", !automaticRenewal);
				Subscription subscription;
				try {
					subscription = this.stripeServ.createSubscription(params);
				} catch (StripeException e) { 
					e.printStackTrace();
					if(e.getCode().equals("resource_missing") && e.getMessage().contains("This customer has no attached payment source")) {
						return new ResponseEntity<License>(HttpStatus.PRECONDITION_REQUIRED); //The precondition is to have an attached payment source
					}else {
						return new ResponseEntity<License>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				this.productServ.save(product);			
				LicenseSubscription license = new LicenseSubscription(true, typeSubs, product, user.getName(),0);
				license.setCancelAtEnd(!automaticRenewal);
				license.setSubscriptionItemId(subscription.getItems().getData().get(0).getId());
				license.setSubscriptionId(subscription.getId());
				licenseServ.save(license);
				this.setTimerAndEndDate(license.getSerial(),license.getProduct(),0);
				
				return new ResponseEntity<License>(license,HttpStatus.OK);
			
		}else {
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);
		}
	}
	
	private void setTimerAndEndDate(String licenseSerial, Product product,long trialDays) {	
		LicenseSubscription license = licenseSubsServ.findBySerialAndProduct(licenseSerial, product);
		System.out.println("Setting " + license.getSerial() + " timer");
		
		Timer time = new Timer();
			time.schedule(new TimerTask() {
				
				@Override
				public void run() {
					LicenseSubscription license = licenseSubsServ.findBySerialAndProduct(licenseSerial, product);
					if(license==null) {
						return;
					}else {
						if(license.getCancelAtEnd()) {
							System.out.println("Setting license inactive...");
							license.setActive(false);
							licenseSubsServ.save(license);
						}else {
							System.out.println("Renewal doing on " + license);
							license.renew(trialDays);
							licenseSubsServ.save(license);
							setTimerAndEndDate(licenseSerial,product,0);
						}
					}
					
				}
				
			}, 
					
					license.getEndDate()
					
			);
	}
	
	
	@GetMapping("/{user}/cards")
	private ResponseEntity<List<PaymentMethod>> getCardsFromUser(@PathVariable String user) {
		User u = this.userServ.findByName(user);
		
		
		// We don't know which user wants to affect -> Unauthorized
		if(u==null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// User to be affected != User that made the request
		if(!u.equals(this.getRequestUser())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("customer", u.getCustomerStripeId());
			params.put("type", "card");
	
			PaymentMethodCollection paymentMethods = this.stripeServ.getPaymentMethodCollection(params);
			List<PaymentMethod> list = new ArrayList<>();
			for(PaymentMethod pm : paymentMethods.getData()) {
					list.add(pm);
			}
			return new ResponseEntity<>(list,HttpStatus.OK);
		}catch (StripeException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{user}/card/{paymentMethodId}")
	private ResponseEntity<SimpleResponse> deleteStripeCard(@PathVariable String user, @PathVariable String paymentMethodId) {
		User u = this.userServ.findByName(user);
		
		// We don't know which user wants to affect -> Unauthorized
		if(u==null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// User to be affected != User that made the request
		if(!u.equals(this.getRequestUser())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		try {
			PaymentMethod paymentMethod =  this.stripeServ.retrievePaymentMethod(paymentMethodId);
			this.stripeServ.detachPaymentMethod(paymentMethod);
			SimpleResponse res = new SimpleResponse("true");
			return new ResponseEntity<>(res,HttpStatus.OK);
		}catch(StripeException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	
	//LIFETIME BUY METHODS
	
    @PostMapping("{userName}/paymentIntent/{tokenId}")
    public ResponseEntity<String> payment(@PathVariable String userName,@RequestBody Product product, @PathVariable String tokenId) throws StripeException {
    	
		User u = this.userServ.findByName(userName);
		
		// We don't know which user wants to affect -> Unauthorized
		if(u==null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// User to be affected != User that made the request
		if(!u.equals(this.getRequestUser())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		Map<String, Object> card = new HashMap<>();
		card.put("token", tokenId);
		Map<String, Object> paramsPM = new HashMap<>();
		paramsPM.put("type", "card");
		paramsPM.put("card", card);

		PaymentMethod paymentMethod =  this.stripeServ.createPaymentMethod(paramsPM);

		
    	Map<String, Object> params = new HashMap<>();
        params.put("amount", product.getPlansPrices().get("L").intValue()*100);
        params.put("payment_method", paymentMethod.getId());
        params.put("currency", "eur");
        params.put("description", "Product bought: " + product.getName());
        

		
        List<Object> payment_method_types = new ArrayList<>();
        payment_method_types.add("card");
        params.put("payment_method_types", payment_method_types);
        params.put("customer",u.getCustomerStripeId());
        params.put("receipt_email",u.getEmail());
        PaymentIntent paymentIntent = this.stripeServ.createPaymentIntent(params);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }

	
    
    @PostMapping("{userName}/confirm/{id}/products/{productName}")
    public ResponseEntity<License> confirm(@PathVariable String id, @PathVariable String userName, @PathVariable String productName) throws StripeException {
    	
    	Product p = this.productServ.findOne(productName);
		User user = this.userServ.findByName(userName);

		// We don't know which user wants to affect -> Unauthorized
		if(user==null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		// User to be affected != User that made the request
		if(!user.equals(this.getRequestUser())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
    	
    	if(p==null) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    	PaymentIntent paymentIntent = this.stripeServ.retrievePaymentIntent(id);
    	if(paymentIntent==null) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
        Map<String, Object> params = new HashMap<>();
        this.stripeServ.confirmPaymentIntent(paymentIntent, params);
        
		License license = new License(true, p, userName);
		licenseServ.save(license);
				
		return new ResponseEntity<>(license,HttpStatus.OK);
    }
}
