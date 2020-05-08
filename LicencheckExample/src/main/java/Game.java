import java.util.Random;
import java.util.Set;
import java.util.Timer;

public class Game implements Runnable{

    private int i = 0;

    private boolean running = false;

    private Licencheck licencheck;

    public Game(){
        this.config();
    }

    public void config() {
        this.licencheck = new Licencheck("http://localhost:80", false);
        System.out.println("Welcome to the example of Licensoft. Introduce your ExampleLicencheck serial:");
        String[] serial = new String[3];
        serial[0] = System.console().readLine();
        serial[1] = "noOK";
        String typeLicense = null;
        while (typeLicense == null) {            //Checking if the serial is valid for our product ExampleLicensoft. It returns the type of license (L, D, M, A)
            licencheck.addInvalidLicenseListener(checkInfo -> {
                switch (checkInfo.getReason()) {
                    case ("VALID"): {
                        System.out.println("Valid Serial!");
                        serial[1] = "ok";
                        System.out.println("--> " + licencheck.getI());
                        if (!this.running) {
                            this.run(); //Al llamar a esto ya se corta el timer de Licencheck y no vuelve a hacerlo periódicamente.
                            this.running=true;
                        }

                        break;
                    }
                    case ("NOT_VALID"): {
                        System.out.println("Wrong serial. Introduce it again please :");
                        serial[0] = System.console().readLine();
                        break;
                    }
                    case ("INTERNET_CON_ERROR"): {
                        System.out.println("[SW]: Error while connecting for checking the license. Let's wait for the next check");
                        serial[1] = "errcon";
                        break;
                    }
                    case ("UNKNOWN_ERROR"): {
                        System.out.println("Something wrong has happened. Introduce the serial again:");
                        serial[0] = System.console().readLine();
                        break;
                    }

                }
            });

            if (serial[1].equals("noOK")) {
                licencheck.checkLicense(serial[0], "ExampleLicencheck");
            }

        }
    }


    public void start(){

        Random random = new Random();
        Set<Thread> threadSet2 = Thread.getAllStackTraces().keySet();
        System.out.println(threadSet2.size());

        while(true){
            /*System.out.println("Introduce maximum number:");
            int maxNum = Integer.valueOf(System.console().readLine());
            int randomNumber = random.nextInt(maxNum);
            System.out.println("Random number (1-"+maxNum+") = " + randomNumber);*/
        }
    }

    @Override
    public void run() {
        this.running=true;
        System.out.println(";;ss");
        for(int i = 0;i<15;i++){
            try {
                Thread.sleep(1000);
                System.out.println("::> " +  this.licencheck.getI());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


       // while(true) {
            //System.out.println("Hola " + i);
        //    i++;

       // }
    }
}
