# What's Licensoft?
Licensoft is an OpenSource online platform to allow software developers and software companies to manage paid licenses of their software. 
With Licensoft it will be easy to charge for a software: run the web where the customers will buy the licenses and use the Licencheck library in your software to check for the validity of the licenses that users introduce.

#### Roles 
In Licensoft there are 2 types of roles: **ADMIN** and **USER**. 
	**ADMIN** will be the Software seller. The main diference between them is that ADMIN can   
```Add/Edit/Delete Products (name, description, prices...). ```

### DOMAIN
#### Product
Name (String)
Description (String)
WebLink (String)
Licenses  (License[])
TypeSubs (String[]) 
PhotoAvailable (boolean)
PlansPrices (Map<String, Number>)
Sku (String)
Active (boolean)

### License
Serial (String)
Active (boolean)
Type (String)
Product [(Product)](README.md#product)
StartDate (Date)
EndDate (Date)
Owner (String)
CancelAtEnd (boolean)

### User
Name (String)
Roles (String[])
Password (String - hashed)
UserStripeId (String)

## ADMIN Habilities
### Add Product - Subscription type
![Add Subscription Product](docs/gifs/addProdSubs.gif)


### Add Product - Lifetime type
This flow is the same as Adding a subscription type but changing its type.

![Add Lifetime Product](docs/gifs/addProdLifetime.gif)

### Edit Product
Admin can change URL and description of a Product. To edit other parameters admin should add a new product.
![EditProduct](docs/gifs/editProd.gif)

### Delete Product
The delete will set the product to inactive and it will disappear from the catalog, but active licenses of it will still be active.
![DeleteProduct](docs/gifs/deleteProd.gif)


## USER normal Flow
### Register
![Register](docs/gifs/register.gif)

### Login
![Login](docs/gifs/logIn.gif)

### Attach payment source to User
![Add payment Source](docs/gifs/addPaymentSource.gif)

### Search in catalog
![Search](docs/gifs/search.gif)

### Subscribe to Product
![Subscribe to Product](docs/gifs/subsProd.gif)

### Buy Lifetime Product
![Buy a Product](docs/gifs/buyProd.gif)

### Introduce license Serial into the software purchased
![Introduce serial](docs/gifs/introduceSerial.gif)
