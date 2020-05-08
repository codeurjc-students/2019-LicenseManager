# INSTALLATION GUIDE - Production

**Pre-requisites:**
 1. Install [Docker Desktop](https://hub.docker.com/editions/community/docker-ce-desktop-windows)
 2. Make an account in Stripe and get the keys:
	2.1.  [Register into Stripe](https://dashboard.stripe.com/register?redirect=%2Ftest%2Fdashboard) 
	2.2.  [Go to the Dashboard](https://dashboard.stripe.com/test/dashboard)
	2.3.  Activate your account.
	2.4.  Get your API Keys = Dashboard > Developers >  Api Keys
 3. Download the [execution files: Licensoft-v1.5 ](https://github.com/codeurjc-students/2019-LicenseManager/archive/Licensoft-v1.5.zip).


Licensoft offers 2 Docker Images to deploy the application easily:
 * **licensoft-https** : runs under **HTTPS** on port 8443. Needs a SSL certificate (we provide a self-signed one and it will be explained how to make an own self-signed).
 * **licensoft-http**: runs under **HTTP** on port 80.

Both images need to be *paired* with a SQL Docker Container, so we provide a docker-compose.yml inside Licensoft-v1.5 folder that will make them work together. This file will vary on some of its enviroment variables (properties) depending if the chosen image of Licensoft is HTTP or HTTPS.

LicenSoft includes **Mail sending** , **Stripe Integration**, **RSA cryptography** for signing offline Licenses, and **Custom naming** of the app. It is also needed to configure an **Admin Account**. For a correct configuration, some properties must be setted before running the code. 



## Configuration
Inside the Licensoft-v.1.5 folder previously downloaded , a `.env` file is placed next to the `docker-compose.yml` with the structure **key=value**. Last one will get the environment variables needed from the `.env`.

*Example `.env`:* 
```
# LICENSOFT CONFIGURATION
# ------------------------
# Documentation: https://github.com/codeurjc-students/2019-LicenseManager/blob/master/docs/INSTALLATION_GUIDE.md

# The name of your Web (it will be shown on the header) 
APPNAME=Licensoft	

# The domain where the web application will be deployed (including http:// or https://)
APPDOMAIN=http:http://licensoft.es/# 

# Stripe public and private keys
STRIPE_PRIVATEKEY=sk_***
STRIPE_PUBLICKEY=pk_***

# A unique Admin Account is created with the email, name and pass specified. It's not possible to create any new Admin Account.
ADMINEMAIL=admin@email.com
ADMINNAME=adminName
ADMINPASS=adminPass

...

```

## Properties Explanation 
* **Custom naming:**  This will be the name of your Web (the name will be shown on the header) and the domain where the web application will be deployed (including http:// or https://).
![AppName Header](./images/appName.JPG)

* **Stripe:** Private and Public keys needed. See Pre-requisites on top of this page to know how to get them.

* **Admin Account:** A unique Admin Account is created with the email, name and pass specified. It's not possible to create any new Admin Account.

* **Email Sending (GMAIL):** Emails are sent when registering, indicating the credentials created. This email will be sent with the indicated account (must be Gmail). It's necessary to set the password of the account too.

* **RSA Private Key Path (LICENCHECK_KEYS_PRIVATE)**:  The path where your private key used to sign the Licenses is placed. We offer a private.key that is paired with its Public Key that works by default in Licencheck. This file should be always placed in the `keys` folder (inside Licensoft-v1.5 folder previously downloaded).
We recommend to **generate a new pair of keys** (the default ones will be used by everyone who uses Licensoft), using the [License3jRepl]([https://github.com/verhas/License3jrepl](https://github.com/verhas/License3jrepl)) tool with Docker <a name="gen-keys"></a>:
	1. Run the License 3jRepl docker image, where pwd = folder where you want to store the keys (path\to\Licensoft-v1.5\keys)
	```
	docker container run -it -v `pwd`:/opt  verhas/license3jrepl
	```
	2. A L3j> $ prompt will be shown. Introduce the next command:
```generateKeys algorithm=RSA size=1024 format=BINARY public=public.key private=private.key```
 
	 3. Both Private and Public.key will be generated on the folder selected. The path to **private.key** must be placed as the value of this configuration (LICENCHECK_KEYS_PRIVATE).
	 4. To get the **public.key** value on byte[] format for [Licencheck Offline](LicencheckGuide.md#how-to-use) introduce next command and copy the output to set the public key needed in Licencheck:
	 ```dumpPublicKey```
	 
	

* **[IF IMAGE SELECTED IS *licensoft-https*] SSL (HTTPS):** A `.jks` file it's necessary to run the application under HTTPS. By default, there is a selfsigned certificate. 
To create a **new selfsigned credential** you must follow next steps:
	1. Open your Shell.
	2. `cd $JAVA_HOME/bin`
	3. Run `keytool -genkey -keyalg RSA -alias selfsigned -keystore path/to/docker/certs/keystore.jks -storepass yourPass -validity 360 -keysize 2048`
	4. Answer the questions that will be shown.
	5. The last one will be your **key password**. 
Once this is done, properties needed will be:
	* Parameter passed in -storepass (in this case, "**yourPass**")
	* The **key password** introduced.
	*  The **path** where the `.jks` is placed: **must** be in`certs`, inside Licensoft-v1.5 folder previously downloaded.
	
 ## RUNNING THE APP (DOCKER) 

1. Open your Shell and go to the **Licensoft-v1.5 folder** previously downloaded.
2. Run `docker-compose up`. The application will be running on port 80 (image = 
-http) under HTTP or on port 8443 (image = licensoft-https) under HTTPS.
