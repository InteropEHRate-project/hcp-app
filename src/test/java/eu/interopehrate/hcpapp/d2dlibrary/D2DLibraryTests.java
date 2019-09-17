package eu.interopehrate.hcpapp.d2dlibrary;

import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.api.D2DConnection;
import org.junit.Assert;
import org.junit.Test;

import javax.bluetooth.BluetoothStateException;

public class D2DLibraryTests {

    @Test
    public void testGetBTAdapterAddress() throws BluetoothStateException {
        D2DConnection d2DConnection = new BluetoothConnection();
        String btAdapterAddress = d2DConnection.getBTadapterAddress();
        Assert.assertNotNull(btAdapterAddress);
        System.out.println(btAdapterAddress);
    }
}
