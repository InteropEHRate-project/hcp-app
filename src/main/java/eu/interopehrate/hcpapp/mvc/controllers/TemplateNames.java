package eu.interopehrate.hcpapp.mvc.controllers;

public interface TemplateNames {
    String INDEX_TEMPLATE = "index";
    String INDEX_NEW_PATIENT = "index/new-patient";
    String INDEX_NEW_PATIENT_SELECT = "index/new-patient-select";
    String INDEX_EXISTING_VISIT = "index/existing-visit";
    String INDEX_EMERGENCY = "index/emergency";
    String INDEX_INPATIENT_ENCOUNTER = "index/inpatient-encounter";
    String ACCESS_DENIED = "access-denied";

    // Administration
    String ADMINISTRATION_HEALTH_CARE_ORGANIZATION_VIEW_DETAILS = "administration/health-care-organization/view-details";
    String ADMINISTRATION_HEALTH_CARE_ORGANIZATION_ADD_PAGE = "administration/health-care-organization/upload-page";
    String ADMINISTRATION_HEALTH_CARE_PROFESSIONAL_VIEW_DETAILS = "administration/health-care-professional/view-details";
    String ADMINISTRATION_HEALTH_CARE_PROFESSIONAL_ADD_PAGE = "administration/health-care-professional/upload-page";
    String ADMINISTRATION_INITIAL_DOWNLOAD_FROM_SEHR = "administration/initial-download/from-sehr";
    String ADMINISTRATION_AUDIT_INFORMATION_VIEW_DETAILS = "administration/audit-information/view-details";
    String ADMINISTRATION_VITAL_SIGNS_NOMENCLATURE_VIEW_DETAILS = "administration/vital-signs-nomenclature/view-details";
    String ADMINISTRATION_PRESCRIPTIONS_NOMENCLATURE_VIEW_DETAILS = "administration/prescriptions-nomenclature/view-details";
    String ADMINISTRATION_HCPS_VIEW_DETAILS = "administration/hcps/view-details";

    // Current Patient
    String CURRENT_PATIENT_PAT_HISTORY_VIEW_SECTION = "current-patient/pat-history/view-section";
    String CURRENT_PATIENT_PAT_HISTORY_UPDATE_PAGE = "current-patient/pat-history/update-info";
    String CURRENT_PATIENT_CURRENT_DISEASES_VIEW_SECTION = "current-patient/current-diseases/view-section";
    String CURRENT_PATIENT_CURRENT_DISEASES_ADD_PAGE = "current-patient/current-diseases/add-info";
    String CURRENT_PATIENT_CURRENT_DISEASE_UPDATE_PAGE = "current-patient/current-diseases/update-info";
    String CURRENT_PATIENT_CURRENT_DISEASE_UPDATE_PAGE_DB = "current-patient/current-diseases/update-info-db";
    String CURRENT_PATIENT_DEMOGRAPHIC_INFORMATION_VIEW_SECTION = "current-patient/demographic-information/view-section";
    String CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_SECTION = "current-patient/document-history-consultation/view-section";
    String CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_FILE = "current-patient/document-history-consultation/view-file";
    String CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_FILE_NEW_TAB = "current-patient/document-history-consultation/view-file-new-tab";
    String CURRENT_PATIENT_CURRENT_MEDICATIONS_STATEMENT_VIEW_SECTION = "current-patient/current-medications/statement/view-section";
    String CURRENT_PATIENT_CURRENT_MEDICATIONS_STATEMENT_ADD_PAGE = "current-patient/current-medications/statement/add-info";
    String CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_VIEW_SECTION = "current-patient/current-medications/prescription/view-section";
    String CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_MEDICATION_VIEW = "current-patient/current-medications/prescription/medication-view";
    String CURRENT_PATIENT_ALLERGIES_VIEW_SECTION = "current-patient/allergies/view-section";
    String CURRENT_PATIENT_ALLERGIES_ADD_PAGE = "current-patient/allergies/add-info";
    String CURRENT_PATIENT_ALLERGIES_UPDATE_PAGE = "current-patient/allergies/update-info";
    String CURRENT_PATIENT_ALLERGIES_UPDATE_SEHR_PAGE = "current-patient/allergies/update-sehr-info";
    String CURRENT_PATIENT_LABORATORY_TESTS_LABORATORY_RESULTS_OBSERVATION_LABORATORY_VIEW = "current-patient/laboratory-tests/laboratory-results/observation-laboratory-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_LABORATORY_RESULTS_ORGANIZATION_LABORATORY_VIEW = "current-patient/laboratory-tests/laboratory-results/organization-laboratory-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_LABORATORY_RESULTS_OBSERVATION_LABORATORY_MEDIA_VIEW = "current-patient/laboratory-tests/laboratory-results/observation-laboratory-media-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_RADIOLOGY_RESULTS_OBSERVATION_VIEW = "current-patient/laboratory-tests/radiology-results/observation-radiology-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_RADIOLOGY_RESULTS_DEVICE_VIEW = "current-patient/laboratory-tests/radiology-results/device-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_RADIOLOGY_RESULTS_IMAGING_STUDY_VIEW = "current-patient/laboratory-tests/radiology-results/imaging-study-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_RADIOLOGY_RESULTS_ORGANIZATION_VIEW = "current-patient/laboratory-tests/radiology-results/organization-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_RADIOLOGY_RESULTS_PRACTITIONER_VIEW = "current-patient/laboratory-tests/radiology-results/practitioner-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_PATHOLOGY_RESULTS_OBSERVATION_PATHOLOGY_VIEW = "current-patient/laboratory-tests/pathology-results/observation-laboratory-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_PATHOLOGY_RESULTS_SPECIMEN_VIEW = "current-patient/laboratory-tests/pathology-results/specimen-view";
    String CURRENT_PATIENT_LABORATORY_RESULTS_PATHOLOGY_RESULTS_ORGANISATION_LABORATORY_VIEW = "current-patient/laboratory-tests/pathology-results/organization-laboratory-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_PATHOLOGY_RESULTS_OBSERVATION_LABORATORY_MEDIA_VIEW = "current-patient/laboratory-tests/pathology-results/observation-laboratory-media-view";
    String CURRENT_PATIENT_LABORATORY_TESTS_OTHER_RESULTS_VIEW_SECTION = "current-patient/laboratory-tests/other-results/view-section";
    String CURRENT_PATIENT_DIAGNOSTIC_IMAGING_VIEW_SECTION = "current-patient/diagnostic-imaging/view-section";
    String CURRENT_PATIENT_DIAGNOSTIC_IMAGING_DICOM = "current-patient/diagnostic-imaging/DICOM";
    String CURRENT_PATIENT_DIAGNOSTIC_IMAGING_IMAGE_REPORT = "current-patient/diagnostic-imaging/image-report";
    String CURRENT_PATIENT_DIAGNOSTIC_IMAGING_IMAGE_REPORT_VIEW_FILE = "current-patient/diagnostic-imaging/view-file";
    String CURRENT_PATIENT_DIAGNOSTIC_IMAGING_IMAGE_REPORT_VIEW_FILE_NEW_TAB = "current-patient/diagnostic-imaging/view-file-new-tab";
    String CURRENT_PATIENT_HOSPITAL_DISCHARGE_REPORT = "current-patient/hospital-discharge-report/view-section";
    String CURRENT_PATIENT_HOSPITAL_DISCHARGE_REPORT_DOCUMENT = "current-patient/hospital-discharge-report/document";
    String CURRENT_PATIENT_OUTPATIENT_REPORT = "current-patient/outpatient-report/view-section";
    String CURRENT_PATIENT_OUTPATIENT_REPORT_DOCUMENT = "current-patient/outpatient-report/document";
    String CURRENT_PATIENT_OUTPATIENT_REPORT_MEDICATION_VIEW = "current-patient/outpatient-report/medication-view";
    String CURRENT_PATIENT_SEND_TO_OTHER_HCP = "current-patient/send-to-other-hcp/view-section";
    String CURRENT_PATIENT_PERMANENT_STORAGE = "current-patient/permanent-storage/view-section";

    // Visit data
    String CURRENT_PATIENT_VITAL_SIGNS_VIEW_SECTION = "current-patient/visit-data/vital-signs/view-section";
    String CURRENT_PATIENT_VITAL_SIGNS_ADD_PAGE = "current-patient/visit-data/vital-signs/add-info";
    String CURRENT_PATIENT_VITAL_SIGNS_EDIT_PAGE = "current-patient/visit-data/vital-signs/edit-info";
    String CURRENT_PATIENT_VISIT_DATA_PRESCRIPTION_VIEW_SECTION = "current-patient/visit-data/visit-prescription/view-section";
    String CURRENT_PATIENT_VISIT_DATA_PRESCRIPTION_MEDICATION_VIEW_SECTION = "current-patient/visit-data/visit-prescription/medication-view";
    String CURRENT_PATIENT_VISIT_DATA_PRESCRIPTION_ADD_PAGE = "current-patient/visit-data/visit-prescription/add-info";
    String CURRENT_PATIENT_VISIT_PRESCRIPTION_UPDATE_PAGE = "current-patient/visit-data/visit-prescription/update-info";
    String CURRENT_PATIENT_REASON_VIEW_SECTION = "current-patient/visit-data/reason/view-section";
    String CURRENT_PATIENT_PH_EXAM_VIEW_SECTION = "current-patient/visit-data/ph-exam/view-section";
    String CURRENT_PATIENT_CONCLUSION_VIEW_SECTION = "current-patient/visit-data/conclusion/view-section";
}
