import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.sound.midi.SysexMessage;


public class Licencheck {

    private String baseEndpoint;
    private HttpsURLConnection con;



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



}
