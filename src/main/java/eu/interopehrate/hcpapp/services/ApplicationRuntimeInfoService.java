package eu.interopehrate.hcpapp.services;

import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;

public interface ApplicationRuntimeInfoService {
    Organization organization();
    Practitioner practitioner();
}
