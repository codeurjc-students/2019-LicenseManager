package tfg.licensoft.api;

import tfg.licensoft.licenses.License;
import tfg.licensoft.licenses.LicenseService;
import tfg.licensoft.licenses.LicenseSubscription;
import tfg.licensoft.licenses.LicenseSubscriptionService;
import tfg.licensoft.products.Product;
import tfg.licensoft.products.ProductService;
import tfg.licensoft.users.User;
import tfg.licensoft.users.UserService;

import org.springframework.web.bind.annotation.*;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.model.Order;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.SetupIntent;
import com.stripe.model.Subscription;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceItemCreateParams;
import com.stripe.param.SubscriptionCreateParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

	@Autowired
	private LicenseService licenseServ;
	
	@Autowired
	private LicenseSubscriptionService licenseSubsServ;
	
	@Autowired
	private UserService userServ;
	
	
	@Autowired
	private ProductService productServ;
	
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
	
	@PostMapping("{userName}/addCard/{tokenId}")
	public ResponseEntity<SimpleResponse> addCardStripeElements(@PathVariable String userName,@PathVariable String tokenId){
		User user = this.userServ.findByName(userName);
		try {
			//Create PaymentMethod
			Map<String, Object> card = new HashMap<>();
		
			card.put("token", tokenId);

			Map<String, Object> params = new HashMap<>();
			params.put("type", "card");
			params.put("card", card);
	
			PaymentMethod paymentMethod =  PaymentMethod.create(params);
			
			params.clear();
			
			List<Object> paymentMethodTypes =  new ArrayList<>();
			paymentMethodTypes.add("card");
			params.put("payment_method_types", paymentMethodTypes);
			params.put("customer", user.getCustomerStripeId());
			params.put("payment_method", paymentMethod.getId());
			params.put("confirm", true);


			SetupIntent.create(params);
			
			params.clear();
			params.put("customer", user.getCustomerStripeId());
			paymentMethod.attach(params);
			
			this.setDefaultPaymentMethod(userName, paymentMethod.getId());
			
			SimpleResponse response = new SimpleResponse(paymentMethod.getId());
			return new ResponseEntity<SimpleResponse>(response,HttpStatus.OK);

		} catch (StripeException e) {
			e.printStackTrace();
			return new ResponseEntity<SimpleResponse>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@PutMapping("{userName}/setDefault/{paymentMethodId}")
	public void setDefaultPaymentMethod(@PathVariable String userName, @PathVariable String paymentMethodId) {
		try {
			User user = this.userServ.findByName(userName);
			if(user==null) {
				return;
			}
			Customer c = Customer.retrieve(user.getCustomerStripeId());
			
			Map<String, Object> params = new HashMap<>();
			params.put("default_payment_method", paymentMethodId);
			
			Map<String, Object> params2 = new HashMap<>();
			params2.put("invoice_settings", params);
			
			c.update(params2);
			
		}catch(StripeException e ) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("{userName}/getDefault")
	public ResponseEntity<SimpleResponse> getDefaultPaymentMethod(@PathVariable String userName) {
		User user = this.userServ.findByName(userName);
		if(user==null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Customer c;
		try {
			c = Customer.retrieve(user.getCustomerStripeId());
			SimpleResponse r = new SimpleResponse(c.getInvoiceSettings().getDefaultPaymentMethod());
			
			return new ResponseEntity<>(r,HttpStatus.OK);

		} catch (StripeException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	//Will be subscribed to a M subscription with x free trial days
	@PutMapping("/{productName}/{userName}/addTrial/{days}/card/{token}")
	public ResponseEntity<License> addTrial(@PathVariable String productName, @PathVariable String userName, @PathVariable long days,  @PathVariable String token){
		
		User user = this.userServ.findByName(userName);
		Product product = this.productServ.findOne(productName);
		
		if(user!=null && product!=null) {
			try {
				String pM = this.addCardStripeElements(userName,token).getBody().getResponse();

				SubscriptionCreateParams params =
						  SubscriptionCreateParams.builder()
						    .setCustomer(user.getCustomerStripeId())
						    .addItem(
						      SubscriptionCreateParams.Item.builder()
						        .setPlan(product.getPlans().get("M")) //Free trial is always monthly subscription
						        .build())
						    .setTrialPeriodDays(days)
						    .setDefaultPaymentMethod(pM)
						    .build();
				Subscription subscription = Subscription.create(params);
				
				LicenseSubscription license = new LicenseSubscription(true, "M", product, user.getName(),days);
				license.setCancelAtEnd(false);  //Trial Periods have automatic renewal
				license.setSubscriptionItemId(subscription.getItems().getData().get(0).getId());
				license.setSubscriptionId(subscription.getId());
				licenseServ.save(license);
				this.setTimerAndEndDate(license,days);
				return new ResponseEntity<License>(license,HttpStatus.OK);

			}catch(StripeException e) {
				e.printStackTrace();
				return new ResponseEntity<License>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			System.out.println("Nsda");
			return new ResponseEntity<License>(HttpStatus.NOT_FOUND);

		}
	}


	
	@PutMapping("/{productName}/{typeSubs}/{userName}/addSubscription/renewal/{automaticRenewal}")
	public ResponseEntity<License> addSubscription(@PathVariable String productName, @PathVariable String typeSubs, @PathVariable String userName, @PathVariable boolean automaticRenewal){
		Product product = this.productServ.findOne(productName);
		User user = this.userServ.findByName(userName);
		if(user==null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		Map<String,String> plans = product.getPlans();
				 
		String planId = plans.get(typeSubs);
		if (planId!=null && product!=null) {
				Map<String, Object> item = new HashMap<>();
				item.put("plan", planId);
				Map<String, Object> items = new HashMap<>();
				items.put("0", item);
				Map<String, Object> expand = new HashMap<>();
				expand.put("0", "latest_invoice.payment_intent");
				Map<String, Object> params = new HashMap<>();
				params.put("customer", user.getCustomerStripeId());
				params.put("items", items);
				params.put("expand", expand);
				params.put("cancel_at_period_end", !automaticRenewal);
				Subscription subscription;
				try {
					subscription = Subscription.create(params);
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
				if(automaticRenewal) {
					this.setTimerAndEndDate(license,0);
				}
				return new ResponseEntity<License>(license,HttpStatus.OK);
			
		}else {
			return new ResponseEntity<License>(HttpStatus.BAD_REQUEST);
		}
	}
	
	private void setTimerAndEndDate(LicenseSubscription license, long trialDays) {
		Timer time = new Timer();
		System.out.println(license.getEndDate());
		time.schedule(new TimerTask() {
			

			@Override
			public void run() {
				license.renew(trialDays);
				LicenseSubscription newL = licenseSubsServ.save(license);
				setTimerAndEndDate(newL,0);
				
			}
			
		}, 
				license.getEndDate()
				
		);
	}
	
	
	@GetMapping("/{user}/cards")
	private ResponseEntity<List<PaymentMethod>> getCardsFromUser(@PathVariable String user) {
		User u = this.userServ.findByName(user);
		if(u!=null) {
			try {
				Map<String, Object> params = new HashMap<>();
				params.put("customer", u.getCustomerStripeId());
				params.put("type", "card");

				PaymentMethodCollection paymentMethods =
				  PaymentMethod.list(params);
				List<PaymentMethod> list = new ArrayList<>();
				for(PaymentMethod pm : paymentMethods.getData()) {
						list.add(pm);
				}
				return new ResponseEntity<>(list,HttpStatus.OK);
			}catch (StripeException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
				
	}
	
	@DeleteMapping("/{user}/card/{paymentMethodId}")
	private ResponseEntity<SimpleResponse> deleteStripeCard(@PathVariable String user, @PathVariable String paymentMethodId) {
		User u = this.userServ.findByName(user);
		if(u!=null) {
			try {
				PaymentMethod paymentMethod =  PaymentMethod.retrieve(paymentMethodId);
				paymentMethod.detach();
				SimpleResponse res = new SimpleResponse("true");
				return new ResponseEntity<>(res,HttpStatus.OK);
			}catch(StripeException e) {
				
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	//TODO de momento no se usa, lo dejo por si necesitamos la invoice para imprimir o lo que sea
	@GetMapping("/{user}/invoice/subs/{subscriptionId}")
	private ResponseEntity<String> getInvoicePreview(@PathVariable String user, @PathVariable String subscriptionId){
		System.out.println("---");
		User u = this.userServ.findByName(user);
		//User uLogged = this.userComponent.getLoggedUser();
		if(u!=null) {
			try {
				Map<String, Object> invoiceParams = new HashMap<>();
				invoiceParams.put("customer", u.getCustomerStripeId());
				invoiceParams.put("subscription", subscriptionId);
				Invoice in = Invoice.upcoming(invoiceParams);
				
				return new ResponseEntity<>(in.toJson(),HttpStatus.OK);
			}catch(StripeException e) {
				
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	
	//SUBSCRIPTION METHODS
	
    @PostMapping("{userName}/paymentIntent/{tokenId}")
    public ResponseEntity<String> payment(@PathVariable String userName,@RequestBody Product product, @PathVariable String tokenId) throws StripeException {
    	
		User u = this.userServ.findByName(userName);
		if(u==null) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> card = new HashMap<>();
		card.put("token", tokenId);
		Map<String, Object> paramsPM = new HashMap<>();
		paramsPM.put("type", "card");
		paramsPM.put("card", card);

		PaymentMethod paymentMethod =  PaymentMethod.create(paramsPM);

		
    	Map<String, Object> params = new HashMap<>();
        params.put("amount", product.getPlansPrices().get("L").intValue()*100);
        params.put("payment_method", paymentMethod.getId());
        params.put("currency", "eur");
        params.put("description", product.getDescription());
        

		
        List<Object> payment_method_types = new ArrayList<>();
        payment_method_types.add("card");
        params.put("payment_method_types", payment_method_types);
        params.put("customer",u.getCustomerStripeId());
        params.put("receipt_email",u.getEmail());
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }

	
    
    @PostMapping("{userName}/confirm/{id}/product/{productName}")
    public ResponseEntity<License> confirm(@PathVariable String id, @PathVariable String userName, @PathVariable String productName) throws StripeException {
    	Product p = this.productServ.findOne(productName);
    	if(p==null) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    	PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
    	if(paymentIntent==null) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
        Map<String, Object> params = new HashMap<>();
        paymentIntent.confirm(params);
        
		License license = new License(true, p, userName);
		licenseServ.save(license);
				
		return new ResponseEntity<>(license,HttpStatus.OK);
    }
    

    private void sendInvoiceToPay (String customerId, Product product) {
    	
    	try {
			InvoiceItemCreateParams params2 =
					  InvoiceItemCreateParams.builder()
					    .setAmount(product.getPlansPrices().get("L").longValue())
					    .setCurrency("eur")
					    .setCustomer(customerId)
					    .setDescription(product.getName())
					    .build();

					InvoiceItem invoiceItem = InvoiceItem.create(params2);
					
					InvoiceCreateParams params =
							  InvoiceCreateParams.builder()
							    .setCustomer(customerId)
							    .setCollectionMethod(InvoiceCreateParams.CollectionMethod.SEND_INVOICE)
							    .setDaysUntilDue(1L)
							    .build();

							Invoice invoice = Invoice.create(params);
							invoice.sendInvoice();
		} catch (StripeException e) {
			e.printStackTrace();
		}
    	
    }
	

}
