# What's Licencheck?
Licencheck is a Java Library that allows 3rd programs to check the validity of a Product License acquired in a Licensoft-web with a simple method.

These Licenses can be delivered to the customers to *unlock* the software in two forms:
- **Online mode: Serial-->** A unique alphanumeric code.
- **Offline mode: License File-->** A txt file with the License attributes signed by the web application with a RSA  cryptographic algorithm so it can't work if it's modified by an external. **MB** Licenses **not** allowed to use this type.






## How to use Licencheck  <a name="how-to-use"></a>
**1**. Import Licencheck Maven dependency on your **pom.xml**.
```
<dependency>
<groupId>com.github.kikeajani</groupId>
<artifactId>licencheck</artifactId>
<version>2.0.0</version>
</dependency>
```
**2**. Choose an option to check : Online or Offline (depending your product)
	 
2.1. **ONLINE CHECK**
For an online checking, Licencheck will do REST requests to the web-application to check if the serial introduced is valid and active for this product.
For that reason, it's necessary to instantiate Licencheck with 2 params: 
	- **URL Domain of the web-app (String)**
	- **Indication if the web-app is deployed under a self-signed certificate for SSL (boolean)**
```
Licencheck l = new Licencheck("http://mylicensoftwebpage.com",false);
```
 Then, there are 2 options to check a License :
  - **Immediate** checking: Calling a method returns a response.
  - **Periodically** checking: Calling a method checks the license, and if it's valid, sets a 24h timer that will check everyday if the license is still valid.
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	**IMMEDIATE**
```
l.checkLicenseOnce("ebc2d716-d666-4eef-8e3d-50c435e1d3e4","productName")
``` 
- If it’s **valid**, it will return a String indicating the **pseudonym** of the License (D, M, A, MB, L). 
- If it's **not valid** will return **null**, 
*The offline mode doesn't return the pseudonym because it can be read from the License File.*

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	**PERIODICALLY**

As this method does NOT return anything immediatly, it is necessary to add previously a Listener that will execute some code when the method has checked the license (**non-blocking**) and returns a response code.
This listener must be implemented by the Licencheck's user, describing how the Software should act when it receives the response.
The **response codes** must be obtained by calling "**checkInfo.getReason()**" :

| CODE | EXPLANATION| SUGGESTED LISTENER ACT
| -- | -- | -- |
| **VALID**   |  The license is valid.   | Allow user to use the software
|**VALID_R** | The license is valid.|  Allow user to use the software
|**NOT_VALID** | The license is not valid: unpaid, not active, modified... | Not allow user to use the software. <br> Ask for a new valid serial.
|**NOT_VALID_R** | License has been valid previously, but not now = expired  | Not allow user to use the software.<br> Ask for a new valid serial.
|**UNKNOWN_ERROR** |  Something unexpected has happened | Ask for a new valid serial.
|**UNKNOWN_ERROR_R** |  Something unexpected has happened | Ask for a new valid serial.
|**INTERNET_CON_ERROR** | Checking has not been posible because server backend is busy/disconnected. Licencheck will retry every 5 seconds to connect to server and check the license | Set a max number of retries, and if it is overcome, stop the execution of the software.
|**INTERNET_CON_ERROR_R** | Checking has not been posible because server backend is busy/disconnected. Licencheck will retry every 5 seconds to connect to server and check the license | Set a max number of retries, and if it is overcome, stop the execution of the software.

Response codes with **_R** are responses received by a periodical checking. Sometimes it is needed a different action if the checking failed on first call, that if it has failed in scheduled checks.
Example of listener skeleton:
```
licencheck.addLicencheckListener(checkInfo -> {  
    switch (checkInfo.getReason()) {  
        case("VALID_R"):{  
           ...
           break;
        }  
        case ("VALID"): {  
           ...
		   break;
        }  
        case ("NOT_VALID_R"): {  
           ...
           break;
		}  
  
        case ("NOT_VALID"): {  
           ...
		   break;  
		}  
        case ("INTERNET_CON_ERROR_R"):  
        case ("INTERNET_CON_ERROR"): {  
           ...
		   break;  
		}  
        case ("UNKNOWN_ERROR_R"):  
        case ("UNKNOWN_ERROR"): {  
           ... 
		   break;  
		}  
  
    }  
});
```

Now, call the method and keep with your program functionality.
```
l.checkLicensePeriodically("ebc2d716-d666-4eef-8e3d-50c435e1d3e4","productName");
...
``` 
	
	
2.2. **OFFLINE CHECK**  
Licencheck Offline checking works with a RSA Cryptography Algorithm. For that reason, Private and Public keys are needed:
- Private key: Needed on the backend of Licensoft web application to sign the Licenses that will be served to customers.
- Public key: Needed on Licencheck to validate the sign of the License served on the Licensoft web app.

A default pair of keys are offered to start using Licensoft and Licencheck without any extra configuration, but it can be configured with a new pair of keys (see [InstallationGuide](INSTALLATION_GUIDE.md#gen-keys)).

Licencheck must be instantiated **without** parameters.
```
Licencheck l = new Licencheck();
```
If you want to override the Public Key, it must be setted as a **byte[]**. This key must be paired with the Private key of Licensoft web app  (see [InstallationGuide](INSTALLATION_GUIDE.md#gen-keys) to know how to generate a new pair and get the byte[] value of the public key).
```
l.setKey(newKey);
```
Then, the License file must be located on the customer's system file. ***(Best option is to indicate the customer to place it next to the .exe/.jar/etc)***
```
File lic = new File("license-ProductName.txt");
```

Finally, calling method *checkLicenseOffline* will return if it's **valid/not valid**.
```
l.checkLicenseOffline(lic)
```


**3**. Call `licencheck.updateUsage(String licenseSerial, String productName, long usage)` or `licencheck.updateUsage(String licenseSerial, String productName, long usage, String userName)` whenever you want to inform of an usage of the software (functionality, program started, etc). With the second option it can be informed the name of the user that did the usage, for statistics.
- If the product and the serial **exists**, and the subscription is not Lifetime type, will **return the actual usage** of the license (with the new usage updated).
- If **not**, it will return **null**. 



 ## How does Licencheck works inside?
 **ONLINE MODE**
 1. ### Constructor
 Licencheck constructor with params makes a new URL with the param passed adding */licencheck/* at the end.
 With an instance made like following:
 `Licencheck l = new Licencheck("https://localhost:8443",true); `
 Licencheck would construct its final endpoint like:
 `baseEndpoint = https://localhost:8443/licencheck/`
 
 2. ### <a name= "check"></a> Checking a Lickense
 Licencheck will do a HTTP GET request to:
  `baseEndpoint + "checkLicense/"  + productName + "/"  + licenseSerial` 

3. ### <a name= "update"></a>  Updating Usage
Licencheck will do a HTTP PUT request to: `baseEndpoint +"updateUsage/"+usage+"/"+productName+"/"+licenseSerial`

---
**OFFLINE MODE**
  Offline checking is based on a RSA cryptography algorithm. Licencheck receives a signed file (with a private key, in the Licensoft web backend), and tries to certificate that the file hasn't been modified (using the public key, placed inside Licencheck).
  ***! Remember: MB Licenses can't be used with the Offline mode, and UpdateUsage method is just valid for Online Licenses***
 1. ### Constructor
A default Public Key (that pairs with the default Private Key offered in Licensoft web backend) is setted.
 
 2. ### <a name= "check"></a> CheckLickense(File f) 
 Licencheck will check with a RSA Algorithm if the LicenseFile introduced has been changed since its original signing. 
 - If it hasn't been changed, then Licencheck check its attributes inside the LicenseFile and decides if the License is inside the period bounds or not, returning **true/false**.
 - If it has been changed, returns **false**.
 

