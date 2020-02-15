import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TestLicencheck{

    @Test
    public void testCheckLicense(){
        Licencheck l = new Licencheck("http://localhost:8080");
        Assert.assertNotNull(l.checkLicense("XX-X3","sw"));
    }

    @Test
    public void testCheckLicenseWrong(){
        Licencheck l = new Licencheck("http://localhost:8080");
        Assert.assertNull(l.checkLicense("X-X3","sw"));
    }

    @Test
    public void testCheckLicenseProductNotExisting(){
        Licencheck l = new Licencheck("http://localhost:8080");
        Assert.assertNull(l.checkLicense("X-X3","sw"));
    }
}
