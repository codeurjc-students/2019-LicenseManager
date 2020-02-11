import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.IOUtils;

import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.*;



public class Licencheck {

    private String endpoint;
    private HttpURLConnection con;
    private HashMap<String,String> properties;

    public Licencheck(){
        properties = new HashMap<String, String>();
        this.readProperties();
        String url= properties.get("url");
        if(url!=null){
            this.endpoint=url;
        }

    }

    private void readProperties(){
        /*try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            InputStream inputStream = this.getClass().getResourceAsStream("licencheck.properties");
            StringWriter writer = new StringWriter();
            try {
                IOUtils.copy(inputStream, writer);
                String theString = writer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File file = new File(classLoader.getResource("licencheck.properties").getFile());
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String[] p=myReader.next().split("=");
                properties.put(p[0],p[1]);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return;
        }*/

        InputStream is = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("licencheck.properties")));

            String line = null;
            while ((line = br.readLine()) != null) {
                String[] p=line.split("=");
                properties.put(p[0],p[1]);
            }
        }
        catch (IOException ioe) {
            System.out.println("Exception while reading input " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }

        }
    }


/*
    public boolean checkAccount(String userName, String password) {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/licencheck/checkAccount");
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        if(url!=null){
            return this.makeRequestBooleanResponse(url,userName,password);
        }else{
            return false;
        }

    }
*/
    //TODO The password can be removed 
    //Return values --> NULL (if check == false ) , type license (L,M,D,Y) = (Life,Month,Day,Year)
    public String checkLicense(String licenseSerial, String productName){
        try {
            URL url = new URL(endpoint + "checkLicense/" + productName + "/" + licenseSerial);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
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

    private JSONObject makeUserJSON(String userName, String password){
        JSONObject user = new JSONObject();
        user.put("userName",userName);
        user.put("password", password);
        return user;
    }

    private boolean makeRequestBooleanResponse(URL url,String userName, String password) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(this.makeUserJSON(userName, password).toString());
            wr.flush();

            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                con.disconnect();
                return true;
            } else {
                con.disconnect();
                return false;
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

}
