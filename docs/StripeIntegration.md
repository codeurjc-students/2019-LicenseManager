# Stripe Integration with Licensoft
[Stripe](https://stripe.com/) is a software that allows individuals and businesses to make and receive payments over the Internet. 
It offers an [API](https://stripe.com/docs/api) in many programming languages to allow people creating **Customers, Products and Plans** and charge for them.
Also, Stripe give us a [checkout](https://stripe.com/docs/payments/checkout) to make easier the payments.

## First Steps : Creating a Stripe Account

1.  [Register into Stripe](https://dashboard.stripe.com/register?redirect=%2Ftest%2Fdashboard) 
2.  [Go to the Dashboard](https://dashboard.stripe.com/test/dashboard)
3.  Activate your account.
4.  Get your API Keys = Dashboard > Developers >  Api Keys
5. Set up [Stripe&JavaBackend](#backSetup) and [Stripe&AngularFrontend](#frontSetup) 
## Stripe => Licensoft Domain
* Customer => User
* Product => Product
	* Plans => Product attribute
	* Sku => Product attribute

## Java Backend & Stripe - Set Up <a name="backSetup"></a>
adas

## Java Backend & Stripe - Implementations
The following explanations of implementations with Stripe in the back do not include the complete code, only the key lines. For more information, see the Stripe API or the full code available in this repository,
### Creating a Customer
[Stripe Customers API](https://stripe.com/docs/api/customers)
Whenever someone registers, Customer (Stripe) and User (Licensoft) are created. Both are linked by stripeCustomerId attribute in User.
`... `
`Customer customer = Customer.create(customerParameter);`
`userServ.save(new User(customer.getId(),user, pass1, "ROLE_USER"));`

### Creating a Product
[Stripe Products API](https://stripe.com/docs/api/products)
Stripe offers many options to create a Product. Licensoft has 2 types of products: **Good (Stripe) | Lifetime (Licensoft)** & **Service (Stripe) | Subscription (Licensoft)**. 
`...`
`params.put("type", "good");` OR `params.put("type", "service");`
Creating the Product in stripe:
`productStripe = com.stripe.model.Product.create(params);`

### Setting Plans to a Subscription Product
[Stripe Plans API](https://stripe.com/docs/api/plans)
Each Subscription Product can have many different Plans (with different prices and intervals). When a Plan is created, it is linked with the Product (Licensoft) by the plans Map attribute.
`...`
`params.put("interval", "month");`
`params.put("product", productId);`
`params.put("amount", (int)(price*100));`
`Plan plan1M = Plan.create(params);`
`product.getPlans().put("M",plan1M.getId());`

### Setting SKUs to a Lifetime Product
[Stripe SKU API](https://stripe.com/docs/api/skus)
Lifetime Products hava associated a SKU which identifies the Product with its properties (price, availability ,etc). Each Lifetime Product (Lifensoft) has a SKU attribute whick links it with the SKU (Stripe). 
`...`
`paramsSku.put("price",(int)(price*100) );`
`paramsSku.put("product",productId);`

`Sku sku = Sku.create(paramsSku);`
`product.setSku(sku.getId());`
`product.setProductStripeId(productId);`

### Subscribing to a Product
[Stripe Subscription API](https://stripe.com/docs/api/subscriptions)
User can choose which product plan to subscribe. 
**Prerequisite**: Customer (Stripe) must have a payment source attached previously.
`...`
`params.put("customer", user.getCustomerStripeId());`
`Subscription subscription = Subscription.create(params);`

### Purchasing a Product
This process is divided into 2 parts: 1. Frontend to collect card info in a **token**. 2. Make the **Order** with the token.
Purchasing a Lifetime Product consists in ordering it. 
[Stripe Order API](https://stripe.com/docs/api/orders)
It's not necessary to have a payment source attached into it, because Stripe Elements (frontend)  will collect the card information to do the Order.
**1st part:** [Frontend card info recopilation](StripeIntegration.md#collection-card-info)
**2nd part:**

			...
			item1.put("type", "sku");
			item1.put("parent", product.getSku());
			...
			params.put("customer", user.getCustomerStripeId());
			...
			Order order = Order.create(params);
			...
			paramsNew.put("source", token);
			order = order.pay(paramsNew)

### Attach card to Customer
As Purchasing a Product, attaching a card to a Customer (Stripe) is divided in the same two parts.
**1st part:** [Frontend Get card info](StripeIntegration.md#attach-card-to-info)
**2nd part:**
```
			Customer c = Customer.retrieve(user.getCustomerStripeId());
			...
			source.put("source", tokenId); //Token caught from the front
			c.getSources().create(source);
```
## Angular Frontend & Stripe - Set Up <a name="frontSetup"></a>
...


## Angular Frontend & Stripe - Implementations
All the Stripe implementations in Angular consists of Stripe Elements.

### Collecting Card Info 
With Stripe Elements we can easily get card info and transform it to a token to pass it to the backend.
```
... (previous actions)
var  handler = (<any>window).StripeCheckout.configure({
	key:  '***',
	currency:'eur',
	token: (token: any) =>{
		if(token!=null){
			... send token to the back...
		}
	}
});`
 ```

### Attach card to Customer
After setting up StripeElements in Angular and making the html form, logic in typescript follows this main steps:
```
const  elements = stripe.elements();
const  card = elements.create('card');
card.mount('#card-element');
```
...
```
const  paymentForm = document.getElementById('payment-form');
paymentForm.addEventListener('submit', event  => {
	//Send info to the back
	this.userProfileServ.addCardStripeElements(result.token.id).subscribe(...)...
}
```
