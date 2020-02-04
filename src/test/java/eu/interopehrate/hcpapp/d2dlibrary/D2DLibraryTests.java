package eu.interopehrate.hcpapp.d2dlibrary;

import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.api.D2DConnection;
import eu.interopehrate.td2de.api.D2DSecurityConnection;
import org.junit.Assert;
import org.junit.Test;

public class D2DLibraryTests {

    @Test
    public void testGetBTAdapterAddress() throws Exception {
        D2DConnection d2DConnection = new BluetoothConnection();
        ((D2DSecurityConnection) d2DConnection).fetchCertificate();
        String btAdapterAddress = d2DConnection.getBTadapterAddress();
        Assert.assertNotNull(btAdapterAddress);
        System.out.println(btAdapterAddress);
    }
}
