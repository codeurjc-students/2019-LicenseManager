import java.io.IOException;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws IOException {
        Licencheck licencheck = new Licencheck("http://localhost:8080");
        System.out.println("Welcome to the example of Licensoft. Introduce your ExampleLicencheck serial:");
        String serial = System.console().readLine();
        String typeLicense=null;
        while(typeLicense==null){
            //Checking if the serial is valid for our product ExampleLicensoft. It returns the type of license (L, D, M, A)
            typeLicense = licencheck.checkLicense(serial,"ExampleLicencheck");

            if(typeLicense!=null){//It was valid
                System.out.println("Valid Serial!");
                start();
            }else{
                System.out.println("Wrong serial. Introduce it again:");
                serial = System.console().readLine();
            }
        }


    }

    private static void start(){
        Random random = new Random();
        while(true){
            System.out.println("Introduce maximum number:");
            int maxNum = Integer.valueOf(System.console().readLine());
            int randomNumber = random.nextInt(maxNum);
            System.out.println("Random number (1-"+maxNum+") = " + randomNumber);
        }
    }
}
