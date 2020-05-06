import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax0.license3j.Feature;
import javax0.license3j.License;
import javax0.license3j.crypto.LicenseKeyPair;
import javax0.license3j.io.IOFormat;
import javax0.license3j.io.KeyPairReader;
import javax0.license3j.io.LicenseReader;
import javax0.license3j.io.LicenseWriter;
import sun.security.rsa.RSAPrivateKeyImpl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



public class Licencheck {


    private String baseEndpoint;
    private HttpsURLConnection con;
    private byte [] key;
    private byte [] digest;

    private LicencheckListener listener;

   // private Future<CheckInfo> checkInfo;

    //Constructor for non-Internet checking
    public Licencheck(){
        this.digest = new byte[] {
                (byte)0x55,
                (byte)0x49, (byte)0x06, (byte)0x1B, (byte)0x28, (byte)0x20, (byte)0x3C, (byte)0x41, (byte)0x15,
                (byte)0xD5, (byte)0x40, (byte)0x8B, (byte)0x65, (byte)0x51, (byte)0xDA, (byte)0x8D, (byte)0xCF,
                (byte)0x55, (byte)0x54, (byte)0x8A, (byte)0x4E, (byte)0x52, (byte)0xD3, (byte)0x25, (byte)0x81,
                (byte)0xB0, (byte)0xC1, (byte)0x9C, (byte)0xAB, (byte)0x95, (byte)0x9B, (byte)0xCA, (byte)0x14,
                (byte)0x62, (byte)0xC7, (byte)0x2B, (byte)0x3D, (byte)0xBC, (byte)0xA7, (byte)0xD9, (byte)0xF6,
                (byte)0xFE, (byte)0xB7, (byte)0x8D, (byte)0x6B, (byte)0x73, (byte)0xB9, (byte)0xC3, (byte)0x33,
                (byte)0x44, (byte)0x9C, (byte)0x49, (byte)0x25, (byte)0xB3, (byte)0x81, (byte)0x64, (byte)0x51,
                (byte)0x26, (byte)0xEC, (byte)0x82, (byte)0xB6, (byte)0x1E, (byte)0x6E, (byte)0x55,
        };
        this.key = new byte[] {
                (byte)0x52,
                (byte)0x53, (byte)0x41, (byte)0x00, (byte)0x30, (byte)0x81, (byte)0x9F, (byte)0x30, (byte)0x0D,
                (byte)0x06, (byte)0x09, (byte)0x2A, (byte)0x86, (byte)0x48, (byte)0x86, (byte)0xF7, (byte)0x0D,
                (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x05, (byte)0x00, (byte)0x03, (byte)0x81, (byte)0x8D,
                (byte)0x00, (byte)0x30, (byte)0x81, (byte)0x89, (byte)0x02, (byte)0x81, (byte)0x81, (byte)0x00,
                (byte)0x89, (byte)0x2F, (byte)0xCF, (byte)0x51, (byte)0xAC, (byte)0x89, (byte)0x4B, (byte)0x35,
                (byte)0xC4, (byte)0x59, (byte)0x08, (byte)0xE0, (byte)0xBB, (byte)0x70, (byte)0x41, (byte)0x0A,
                (byte)0x08, (byte)0x29, (byte)0x33, (byte)0x87, (byte)0x69, (byte)0x98, (byte)0xDF, (byte)0x2E,
                (byte)0xFC, (byte)0xC7, (byte)0x41, (byte)0xCD, (byte)0x1E, (byte)0xC4, (byte)0xD2, (byte)0x11,
                (byte)0x6E, (byte)0xF8, (byte)0xBC, (byte)0x65, (byte)0x25, (byte)0x63, (byte)0x10, (byte)0x08,
                (byte)0x34, (byte)0x01, (byte)0xE9, (byte)0x44, (byte)0x27, (byte)0x94, (byte)0x59, (byte)0x53,
                (byte)0xA5, (byte)0xA2, (byte)0x70, (byte)0x54, (byte)0xF2, (byte)0x9F, (byte)0x6A, (byte)0x0A,
                (byte)0x5E, (byte)0x17, (byte)0xF2, (byte)0x86, (byte)0x87, (byte)0x5B, (byte)0x1A, (byte)0x9D,
                (byte)0xEC, (byte)0x09, (byte)0xBF, (byte)0x51, (byte)0x07, (byte)0x04, (byte)0x87, (byte)0xA4,
                (byte)0x23, (byte)0x7F, (byte)0xB9, (byte)0x9D, (byte)0xC0, (byte)0xE0, (byte)0x21, (byte)0x2C,
                (byte)0x4B, (byte)0x71, (byte)0x50, (byte)0x17, (byte)0xB0, (byte)0x44, (byte)0xC5, (byte)0xF4,
                (byte)0x4F, (byte)0xAE, (byte)0x8E, (byte)0x77, (byte)0x51, (byte)0xEF, (byte)0x3B, (byte)0x24,
                (byte)0x15, (byte)0x6F, (byte)0xBA, (byte)0xE8, (byte)0x70, (byte)0xA0, (byte)0x22, (byte)0xA0,
                (byte)0x52, (byte)0x74, (byte)0x9C, (byte)0x37, (byte)0x60, (byte)0x2F, (byte)0xD1, (byte)0x52,
                (byte)0xCE, (byte)0xE3, (byte)0x57, (byte)0x06, (byte)0xD4, (byte)0xDE, (byte)0x5C, (byte)0xD0,
                (byte)0xA0, (byte)0x3A, (byte)0x78, (byte)0x11, (byte)0xCB, (byte)0x68, (byte)0xED, (byte)0x31,
                (byte)0x02, (byte)0x03, (byte)0x01, (byte)0x00, (byte)0x01,
        };
    }

    public Licencheck(String url, boolean selfsigned){
        if(url.charAt(url.length()-1)=='/'){
            //Remove the last dash '/'
            url = url.substring(0,url.length()-1);
        }

        if(selfsigned) {
            //AVOID PROBLEMS WITH SELFSIGNED CERTIFICATES
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (GeneralSecurityException e) {
            }
        }
        this.baseEndpoint = url + "/licencheck/";

    }


    //Return values --> NULL (if check == false ) , type license (L,M,D,Y) = (Life,Month,Day,Year)
    public String checkLicense(String licenseSerial, String productName){
        try {
            URL url = new URL(baseEndpoint + "checkLicense/" + productName + "/" + licenseSerial);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");


            int HttpResult = con.getResponseCode();


            StringBuilder sb = new StringBuilder();

            if (HttpResult == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                JsonObject jsonObject = new JsonParser().parse(sb.toString()).getAsJsonObject();
                JsonElement type = jsonObject.get("type");
                System.out.println("The type of the license is --> " + type.getAsString());
                br.close();
                con.disconnect();
                return type.getAsString();
            } else {
                con.disconnect();
                return null;  //No existe la licencia para ese usuario (con su respectiva contraseña) y producto
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }

    //Returns the actual usage or null if not exists
    public Integer updateUsage(String licenseSerial, String productName, long usage){
        try {
            URL url = new URL(baseEndpoint +"updateUsage/"+usage+"/"+productName+"/"+licenseSerial);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");


            int HttpResult = con.getResponseCode();


            StringBuilder sb = new StringBuilder();


            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }


                br.close();
                con.disconnect();
                return Integer.valueOf(sb.toString().trim());

            } else {
                con.disconnect();
                return null;
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }

    public boolean checkLicenseOffline (File l) throws IOException{
        if(l!=null) {
            try (LicenseReader reader = new LicenseReader(l)) {
                License license = reader.read(IOFormat.STRING);
                if (license.isOK(key)){
                    if(!license.get("type").getString().equals("L")){ //Subscription Type Licenses
                        Date startDate = license.get("startDate").getDate();
                        Date endDate = license.get("endDate").getDate();
                        Date actual = new Date();

                        return (actual.after(startDate) && actual.before(endDate));  //Inside of bounds

                    }else{  //Lifetime License = always valid if license.isOK==true
                        return true;
                    }
                }else{
                    return false;
                }
            } catch (IOException e) {
                System.out.print("Error reading license file " + e);
                return false;
            }
        }else{
            throw new FileNotFoundException("The file introduced is null");
        }
    }

    /*
     CheckInfo checkInfo = new CheckInfo();


    checkInfo.setReason("LICENSE NOT VALID");
    this.listener.checkResult(checkInfo);
     */
    public void createLicense(String path){
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        License license = new License();
        license.add(Feature.Create.stringFeature("serial", UUID.randomUUID().toString()));
        license.add(Feature.Create.dateFeature("endDate",dt));
        license.add(Feature.Create.dateFeature("startDate",new Date()));
        license.add(Feature.Create.stringFeature("product","p1"));
        license.add(Feature.Create.stringFeature("type","D"));
        license.setExpiry(license.get("endDate").getDate());
        license.setLicenseId(UUID.fromString(license.get("serial").getString()));


        try {
            KeyPairReader kpr = new KeyPairReader("M:\\UNIVERSIDAD\\TFG\\License3jRepl-master\\private.key");

            LicenseKeyPair lkp = kpr.readPrivate();
            license.sign(lkp.getPair().getPrivate(),"SHA-512");

            LicenseWriter licenseWriter = new LicenseWriter(path);
            licenseWriter.write(license,IOFormat.STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addInvalidLicenseListener(LicencheckListener listener){
        this.listener = listener;
    }

   /* public Future<CheckInfo> getCheckInfo() {
        return checkInfo;
    }*/
}
