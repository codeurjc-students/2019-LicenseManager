# What's Licensoft?
Licensoft is an OpenSource online platform to allow software developers and software companies to manage paid licenses of their software. 
With Licensoft it will be easy to charge for a software: run the web where the customers will buy the licenses and use the Licencheck library in your software to check for the validity of the licenses that users introduce.

## INDEX
* [Roles](#roles)
* [Domain](#domain)
    * [Product](#product)
    * [License](#license)
    * [User](#user)
* [ADMIN Habilities](#admin-habilities)
    * [Add Product - Subscription](#add-product-subs) 
    * [Add Product - Lifetime](#add-product-lifetime)
    * [Edit Product](#edit-product)
    * [Delete Product](#delete-product)
* [USER normal flow](#user-normal-flow)
	1. [Register](#register)
	2. [Login](#login)
	3. [Attach payment source to user](#attach-payment-source-to-user)
	4. [Search in catalog](#search-in-catalog)
	5. [Subscribe to Product](#subscribe-to-product)
	6. [Buy Lifetime Product](#buy-lifetime-product)
	7. [Introduce license Serial into purchased software ](#introduce-license-serial-into-the-purchased-software)
* [Other Docs](#other-documentation)


 


## ROLES
In Licensoft there are 2 types of roles: **ADMIN** and **USER**. 
	**ADMIN** will be the Software seller. The main diference between them is that ADMIN can   
```Add/Edit/Delete Products (name, description, prices...). ```

## DOMAIN
### Product
* Name (String)
* Description (String)
* WebLink (String)
* Licenses  (License[])
* TypeSubs (String[]) 
* PhotoAvailable (boolean)
* PlansPrices (Map<String, Number>)
* Sku (String)
* Active (boolean)

### License
* Serial (String)
* Active (boolean)
* Type (String)
* Product [(Product)](README.md#product)
* StartDate (Date)
* EndDate (Date)
* Owner (String)
* CancelAtEnd (boolean)

### User
* Name (String)
* Roles (String[])
* Password (String - hashed)
* UserStripeId (String)


## ADMIN Habilities
### Add Product - Subscription type <a name="add-product-subs"></a>
![Add Subscription Product](docs/gifs/addProdSubs.gif)

---

### Add Product - Lifetime type <a name="add-product-lifetime"></a>
This flow is the same as Adding a subscription type but changing its type.

![Add Lifetime Product](docs/gifs/addProdLifetime.gif)

---

### Edit Product
Admin can change URL and description of a Product. To edit other parameters admin should add a new product.
![EditProduct](docs/gifs/editProd.gif)

---

### Delete Product
The delete will set the product to inactive and it will disappear from the catalog, but active licenses of it will still be active.
![DeleteProduct](docs/gifs/deleteProd.gif)

---
---

## USER normal Flow
### Register
![Register](docs/gifs/register.gif)

---

### Login
![Login](docs/gifs/logIn.gif)

---

### Attach payment source to User
![Add payment Source](docs/gifs/addPaymentSource.gif)

---

### Search in catalog
![Search](docs/gifs/search.gif)

---

### Subscribe to Product
![Subscribe to Product](docs/gifs/subsProd.gif)

---

### Buy Lifetime Product
![Buy a Product](docs/gifs/buyProd.gif)

---

### Introduce license Serial into the purchased software 
![Introduce serial](docs/gifs/introduceSerial.gif)

---
---


## OTHER DOCUMENTATION
* [Stripe Integration](docs/StripeIntegration.md)
* [Licencheck Guide](docs/LicencheckGuide.md)
* [API](docs/API.md)
* [Installation Guide](docks/InstallationGuide.md)


