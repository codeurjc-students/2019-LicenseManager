import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TestLicencheck{

    @Test
    public void checkOfflineFalse() throws IOException {
        Licencheck l = new Licencheck();
        File lic = new File("M:\\UNIVERSIDAD\\TFG\\2019-LicenseManager\\Licencheck\\src\\main\\resources\\license-plainChecked.txt");
        Assert.assertFalse(l.checkLicenseOffline(lic));
    }

    @Test
    public void checkOfflineTrue() throws IOException {
        Licencheck l = new Licencheck();
        File lic = new File("C:\\Users\\KIKIT\\Downloads\\license-ImageLifetime (2).txt");
        Assert.assertTrue(l.checkLicenseOffline(lic));
    }

    @Test
    public void testFlujoEntero() throws IOException {
        Licencheck l = new Licencheck();

        l.createLicense("license-plainChecked-v2.txt");

        File lic = new File("license-plainChecked-v2.txt");
        Assert.assertTrue(l.checkLicenseOffline(lic));
    }
/*


    @Test
    public void testCheckLicense(){
        Licencheck l = new Licencheck("http://localhost:8080/",false);
        Assert.assertNotNull(l.checkLicense("XX-X3","sw"));
    }

    @Test
    public void testCheckLicenseWrong(){
        Licencheck l = new Licencheck("http://localhost:8080",false);
        Assert.assertNull(l.checkLicense("X-X3","sw"));
    }

    @Test
    public void testCheckLicenseProductNotExisting(){
        Licencheck l = new Licencheck("http://localhost:8080",false);
        Assert.assertNull(l.checkLicense("X-X3","sw"));
    }


    @Test
    public void testUpdateUsage(){
        Licencheck l = new Licencheck("https://localhost:8443",true);
        Integer e= l.updateUsage("fb13f4a5-f09b-4503-ad34-446b59bd75c0","MB",5);
        System.out.println(e);
        Assert.assertTrue(e>0);
    }

    @Test
    public void testUpdateUsageFail(){
        Licencheck l = new Licencheck("https://localhost:8443",true);
        Integer e= l.updateUsage("fb13f4a5-f09b-4503-ad34-446b59bd","MB",5);
        Assert.assertNull(e);
    }

*/
}
