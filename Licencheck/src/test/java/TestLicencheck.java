import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class TestLicencheck {

    @Test
    public void testOnlineUniqueChecking() {
        Licencheck l = new Licencheck("http://localhost:80/",false);

        String typeL = l.checkLicenseOnce("a5e3c1dd-dbd2-432e-880c-85c4535983f9","ExampleLicencheck");

        Assert.assertTrue(typeL.equals("M"));
    }

    @Test
    public void testOnlineUniqueCheckingNotValid()  {
        Licencheck l = new Licencheck("http://localhost:80/",false);

        String typeL = l.checkLicenseOnce("ddd3c1dd-dbd2-432e-880c-85c4535983f9","ExampleLicencheck");

        Assert.assertTrue(typeL==null);
    }

    @Test
    public void testOnlinePeriodicallyChecking() {
        Licencheck l = new Licencheck("http://localhost:80/",false);
        String[] resp = new String[1];
        l.addLicencheckListener(checkInfo -> {
            resp[0]=checkInfo.getReason();
        });
        l.checkLicensePeriodically("a5e3c1dd-dbd2-432e-880c-85c4535983f9","ExampleLicencheck");
        Assert.assertTrue(resp[0].equals("VALID"));

        l.checkLicensePeriodically("dddddd-dbd2-432e-880c-85c4535983f9","ExampleLicencheck");
        Assert.assertTrue(resp[0].equals("NOT_VALID"));
    }


    @Test
    public void testCheckLicenseOffline() throws IOException {
        Licencheck l = new Licencheck();

        l.createLicense("license-plainChecked-v2.txt");

        File lic = new File("license-plainChecked-v2.txt");
        Assert.assertTrue(l.checkLicenseOffline(lic));
    }

    @Test
    public void updateUsage(){
        Licencheck l = new Licencheck("http://localhost:80",false);
        Integer e= l.updateUsage("a5e3c1dd-dbd2-432e-880c-85c4535983f9","ExampleLicencheck",5);
        System.out.println(e);
        Assert.assertTrue(e>0);
    }

    @Test
    public void testUpdateUsageFail(){
        Licencheck l = new Licencheck("http://localhost:80",false);
        Integer e= l.updateUsage("ssssss-f09b-4503-ad34-446b59bd","ExampleLicencheck",5);
        Assert.assertNull(e);
    }

}
