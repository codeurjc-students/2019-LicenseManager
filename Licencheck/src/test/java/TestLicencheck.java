import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TestLicencheck{
/*
    @Test
    public void testCheckAccount(){
        Licencheck l = new Licencheck();
        Assert.assertTrue(l.checkAccount("admin","a"));

    }

    @Test
    public void testCheckAccountWrongName(){
        Licencheck l = new Licencheck();
        Assert.assertFalse(l.checkAccount("admiasn","a"));
    }

    @Test
    public void testCheckAccountWrongPass(){
        Licencheck l = new Licencheck();
        Assert.assertFalse(l.checkAccount("admin","am"));
    }
    */

    @Test
    public void testCheckLicense(){
        Licencheck l = new Licencheck();
        Assert.assertNotNull(l.checkLicense("XX-X3","sw"));
    }

    @Test
    public void testCheckLicenseWrong(){
        Licencheck l = new Licencheck();
        Assert.assertNull(l.checkLicense("X-X3","sw"));
    }

    @Test
    public void testCheckLicenseProductNotExisting(){
        Licencheck l = new Licencheck();
        Assert.assertNull(l.checkLicense("X-X3","sw"));
    }
}
