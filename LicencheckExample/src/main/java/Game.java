import java.util.Random;
import java.util.Set;
import java.util.Timer;

public class Game implements Runnable{

    private int i = 0;


    private Licencheck licencheck;

    public Game(){
        this.config();
    }

    public void config() {
        this.licencheck = new Licencheck("http://localhost:80", false);
        System.out.println("Welcome to the example of Licensoft. Introduce your ExampleLicencheck serial:");

        int[] triesRecon = new int[1];
        triesRecon[0]=0;
        String[] serial = new String[2];
        serial[0] = System.console().readLine();
        serial[1] = "noOK";
        String typeLicense = null;
        while (typeLicense == null) {            //Checking if the serial is valid for our product ExampleLicensoft. It returns the type of license (L, D, M, A)
            licencheck.addLicencheckListener(checkInfo -> {
                switch (checkInfo.getReason()) {
                    case("VALID_R"):{
                        System.out.println("---[BG] license checked ---");
                        break;
                    }
                    case ("VALID"): {
                        System.out.println("Valid Serial!");
                        triesRecon[0]=0; //Reset tries if last execution came from INTERNET_CON_ERROR
                        serial[1] = "ok";
                        this.run();
                        break;
                    }
                    case ("NOT_VALID_R"): {
                        System.out.println("The license has expired. Introduce other valid license serial to keep using this app:");
                        serial[1] = "noOK";
                        serial[0] = System.console().readLine();
                    }

                    case ("NOT_VALID"): {
                        System.out.println("Wrong serial. Introduce it again please:");
                        serial[1] = "noOK";
                        serial[0] = System.console().readLine();
                        break;
                    }
                    case ("INTERNET_CON_ERROR_R"):
                    case ("INTERNET_CON_ERROR"): {
                        if(triesRecon[0]>5){
                            System.out.println("Max reconnecting tries. Exiting...");
                            System.exit(1);
                        }
                        System.out.println("[SW]: Error while connecting for checking the license. Let's wait for the next check");
                        serial[1] = "errcon";
                        triesRecon[0]++;
                        break;
                    }
                    case ("UNKNOWN_ERROR_R"):
                    case ("UNKNOWN_ERROR"): {
                        System.out.println("Something wrong has happened. Introduce the serial again, please:");
                        serial[0] = System.console().readLine();
                        break;
                    }

                }
            });
            if (serial[1].equals("noOK")) {
                licencheck.checkLicensePeriodically(serial[0], "ExampleLicencheck");
            }

        }
    }



    @Override
    public void run() {
        Random random = new Random();

        while(true){
            System.out.println("Introduce maximum number:");
            int maxNum = Integer.valueOf(System.console().readLine());
            int randomNumber = random.nextInt(maxNum);
            System.out.println("Random number (1-"+maxNum+") = " + randomNumber);
        }
    }
}
