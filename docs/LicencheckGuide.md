# What's Licencheck?
Licencheck is a Java Library that allows 3rd programs to check the validity of a Product License Serial acquired in a Licensoft-web with a simple method.

## How to use Licencheck 
1. Import Licencheck Maven dependency on your **pom.xml**.
```
<dependency>
<groupId>com.licensoft.licencheck</groupId>
<artifactId>Licencheck</artifactId>
<version>1.0.0</version>
</dependency>
```
2. Instantiate Licencheck as follows:
`Licencheck licencheck = new Licencheck(url);`
Where "url" must be the endpoint of your Licensoft web API.
Example : 
	`Licencheck licencheck = new Licencheck("http://localhost:8080");`

3. Call [licencheck.checkLicense(String serial, String productName)](#checkLicense) whenever you want to check if a license introduced by the user is valid or not.
	- If it's valid, it will return the **License** object (see [README](../README.md) doc).
	- If it's not valid, it will return **null**.


## API Controller
Apart from the Licencheck library, there is a APIController on the Licensoft-web Java code that receives the requests made by the library.
You can check it on this repository code, inside "backend" folder --> ApiLicencheckController
This Controller just has a method, explained below, to check the existence of a product serial on the Database.

 ## How does Licencheck works?
 1. ### Constructor
 Licencheck constructor makes a new URL with the param passed adding */licencheck/* at the end (endpoint of [Licensoft-web API Controller](#api-controller))
 With an instance made like following:
 `Licencheck l = new Licencheck("http://localhost:8080"); `
 Licencheck would construct its final endpoint like:
 `baseEndpoint = http://localhost:8080/licencheck/`
 
 2. ### CheckLickense(String licenseSerial, String productName)
 Licencheck will do a HTTP GET request to: `baseEndpoint + "checkLicense/"  + productName + "/"  + licenseSerial` , path of the API Controller method.

3. ### API Controller method `checkLicense(@PathVariable String licenseSerial, @PathVariable String productName )`
This method will search in the Database if there is a License of the Product (which name has been passed) with that serial and if it's active.
Then, it will return;
*  **null** (HttpStatus 204 NO CONTENT) 
*  **License** (HttpStatus 200 OK).

