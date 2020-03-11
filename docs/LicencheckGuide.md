# What's Licencheck?
Licencheck is a Java Library that allows 3rd programs to check the validity of a Product License Serial acquired in a Licensoft-web with a simple method.

## How to use Licencheck 
1. Import Licencheck Maven dependency on your **pom.xml**.
```
<dependency>
<groupId>com.github.kikeajani</groupId>
<artifactId>licencheck</artifactId>
<version>1.2.0</version>
</dependency>
```
2. Instantiate Licencheck as follows:
`Licencheck licencheck = new Licencheck(url, isSelfisgned);`
Where "url" must be the endpoint of your Licensoft web API.
Boolean IsSelfsigned => Indicate if your endpoint url has a selfsigned certificate or not.
Example : 
	`Licencheck licencheck = new Licencheck("https://localhost:8443", true);`

3. Call [licencheck.checkLicense(String serial, String productName)](#check) whenever you want to check if a license introduced by the user is valid or not.
	- If it's valid, it will return the **License** object (see [README](../README.md) doc).
	- If it's not valid, it will return **null**.
4. Call [licencheck.updateUsage(String licenseSerial, String productName, long usage](#update) whenever you want to inform of an usage of the software (functionality, program started, etc).
	- If the product and the serial exists, and the subscription is Mettered-Billing type, will return the actual usage of the license (with the new usage updated).
	- If not, it will return null. 


## API Controller
Apart from the Licencheck library, there is a APIController on the Licensoft-web Java code that receives the requests made by the library.
You can check it on this repository code, inside "backend" folder --> ApiLicencheckController
This Controller just has 2 methods, explained below.

 ## How does Licencheck works?
 1. ### Constructor
 Licencheck constructor makes a new URL with the param passed adding */licencheck/* at the end (endpoint of [Licensoft-web API Controller](#api-controller))
 With an instance made like following:
 `Licencheck l = new Licencheck("https://localhost:8443",true); `
 Licencheck would construct its final endpoint like:
 `baseEndpoint = https://localhost:8443/licencheck/`
 
 2. ### <a name= "check"></a> CheckLickense(String licenseSerial, String productName) 
 Licencheck will do a HTTP GET request to:
  `baseEndpoint + "checkLicense/"  + productName + "/"  + licenseSerial` 
  , path of the API Controller method.

3. ### <a name= "update"></a>  UpdateUsage(String  licenseSerial, String  productName, long  usage)
Licencheck will do a HTTP PUT request to: `baseEndpoint"updateUsage/"+usage+"/"+productName+"/"+licenseSerial`
  , path of the API Controller method.
  
4. ### API Controller method `checkLicense(@PathVariable String licenseSerial, @PathVariable String productName )`
This method will search in the Database if there is a License of the Product (which name has been passed) with that serial and if it's active.
Then, it will return;
*  **null** (HttpStatus 204 NO CONTENT) 
*  **License** (HttpStatus 200 OK).
5. ### API Controller method `updateUsage(@PathVariable int usage,@PathVariable String licenseSerial, @PathVariable String productName , HttpServletRequest request)`
This method will try to update the usage of a product's MB license type.
If the license introduced exists, it will be updated adding the usage introduced to the actual usage. Also, it will be saved the usage of the license by different IPs. 

That means that a MB-License with usage = 10,  could have been used by:
	- 111.111.111.111, usage =3.
	- 222.222.222.222, usage =7.
Total usage = 10.

So, the possible returns are:
* **null**(HttpStatus.NOT_ACCEPTABLE) = The license introduced doesn't belongs to a MB-license type.
* **null** (HttpStatus.NOT_FOUND) = The license doesnt belongs to the product introduced.
* **int actualUsage** (HttpStatus.OK) 