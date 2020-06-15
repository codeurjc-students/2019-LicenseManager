import org.quartz.*;

import java.util.Date;


public class CheckingTimerTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
        String licenseSerial = data.getString("LICENSE_SERIAL");
        String productName = data.getString("PRODUCT_NAME");
        Licencheck licencheck = (Licencheck)data.get("LICENCHECK");

        try {
            licencheck.checkLicenseScheduled(licenseSerial, productName);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        licencheck.addRepetition();
    }
}
