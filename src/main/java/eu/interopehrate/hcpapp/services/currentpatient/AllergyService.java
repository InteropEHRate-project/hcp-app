package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.AllergyTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;
import org.hl7.fhir.r4.model.AllergyIntolerance;

import java.util.List;

public interface AllergyService {
    CurrentPatient getCurrentPatient();
    CurrentD2DConnection getCurrentD2DConnection();
    AllergyCommand allergyInfoCommand();
    AllergyCommand allergyInfoCommandTranslated();
    void insertAllergy(AllergyInfoCommand allergyInfoCommand);
    List<AllergyInfoCommand> listOfNewAllergies();
    void deleteNewAllergy(Long id);
    AllergyInfoCommand retrieveNewAllergyById(Long id);
    void updateNewAllergy(AllergyInfoCommand allergyInfoCommand);
    void deleteAllergyFromSEHR(String id);
    AllergyInfoCommand retrieveAllergyFromSEHRById(String id);
    void updateAllergyFromSEHR(AllergyInfoCommand allergyInfoCommand);
    AllergyIntolerance callAllergies();
    AllergyCommand allergiesUpload();
    AllergyTypesRepository getAllergyTypesRepository();
    void refreshData();
}
