import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TestLicencheck{

    @Test
    public void testCheckAccount(){
        Licencheck l = new Licencheck();
        try {
            Assert.assertTrue(l.checkAccount("admin","a"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckAccountWrongName(){
        Licencheck l = new Licencheck();
        try {
            Assert.assertFalse(l.checkAccount("admiasn","a"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckAccountWrongPass(){
        Licencheck l = new Licencheck();
        try {
            Assert.assertFalse(l.checkAccount("admin","am"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckLicense(){
        Licencheck l = new Licencheck();
        try {
            Assert.assertNotNull(l.checkLicense("XX-X3","sw","C1","c1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckLicenseWrong(){
        Licencheck l = new Licencheck();
        try {
            Assert.assertNull(l.checkLicense("X-X3","sw","C1","c1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckLicenseProductNotExisting(){
        Licencheck l = new Licencheck();
        try {
            Assert.assertNull(l.checkLicense("X-X3","sw","C1","c1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
