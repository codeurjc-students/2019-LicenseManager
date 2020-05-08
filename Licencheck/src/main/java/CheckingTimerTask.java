import java.util.TimerTask;

public class CheckingTimerTask extends TimerTask {



    private Licencheck licencheck;
    private String licenseSerial;
    private String productName;
    private boolean hasStarted = false;

    public CheckingTimerTask(Licencheck l, String licenseSerial, String productName){
        this.licencheck=l;
        this.licenseSerial=licenseSerial;
        this.productName=productName;
    }

    @Override
    public void run() {
        this.hasStarted = true;
        this.licencheck.checkLicense(licenseSerial, productName);


    }

    public boolean hasRunStarted() {
        return this.hasStarted;
    }
}
