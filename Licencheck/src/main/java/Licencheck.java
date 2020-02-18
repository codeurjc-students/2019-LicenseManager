import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class Licencheck {

    private String baseEndpoint;
    private HttpURLConnection con;



    public Licencheck(String url){
        System.out.println(url.charAt(url.length()-1));
        if(url.charAt(url.length()-1)=='/'){
            //Remove the last dash '/'
            url = url.substring(0,url.length()-1);
        }
        System.out.println(url);
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

}
