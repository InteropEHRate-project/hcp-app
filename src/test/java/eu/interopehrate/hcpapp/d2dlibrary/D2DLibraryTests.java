package eu.interopehrate.hcpapp.d2dlibrary;

import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.api.D2DConnection;
import eu.interopehrate.td2de.api.D2DHRExchangeListeners;
import eu.interopehrate.td2de.api.D2DSecurityConnection;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
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

    @Test
    public void testOnNoConformantPatientSummaryReceived() {
        class D2DHRExchangeListenersTestImpl implements D2DHRExchangeListeners {
            @Override
            public void onPersonalIdentityReceived(Patient patient) {

            }

            @Override
            public void onPatientSummaryReceived(Bundle bundle) {

            }

            @Override
            public void onConsentAnswerReceived(String s) {

            }

            @Override
            public void onNoConformantPatientSummaryReceived() {

            }
        }

        D2DHRExchangeListeners d2DHRExchangeListeners = new D2DHRExchangeListenersTestImpl();
        D2DLibraryMock d2DLibraryMock = new D2DLibraryMock(d2DHRExchangeListeners);
        d2DLibraryMock.simulateNonValidPatientSummaryReceived();
    }

    private static class D2DLibraryMock {
        private D2DHRExchangeListeners listeners;

        public D2DLibraryMock(D2DHRExchangeListeners listeners) {
            this.listeners = listeners;
        }

        public void simulateNonValidPatientSummaryReceived() {
            listeners.onNoConformantPatientSummaryReceived();
        }
    }
}
