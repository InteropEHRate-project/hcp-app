package eu.interopehrate.hcpapp.mvc.controllers;

public interface TemplateNames {
    String INDEX_TEMPLATE = "index";

    // Administration
    String ADMINISTRATION_HEALTH_CARE_ORGANIZATION_VIEW_DETAILS = "administration/health-care-organization/view-details";
    String ADMINISTRATION_HEALTH_CARE_ORGANIZATION_ADD_PAGE = "administration/health-care-organization/upload-page";
    String ADMINISTRATION_HEALTH_CARE_PROFESSIONAL_VIEW_DETAILS = "administration/health-care-professional/view-details";
    String ADMINISTRATION_HEALTH_CARE_PROFESSIONAL_ADD_PAGE = "administration/health-care-professional/upload-page";
    String ADMINISTRATION_INITIAL_DOWNLOAD_FROM_SEHR = "administration/initial-download/from-sehr";
    String ADMINISTRATION_AUDIT_INFORMATION_VIEW_DETAILS = "administration/audit-information/view-details";

    // Current Patient
    String CURRENT_PATIENT_CURRENT_MEDICATIONS_STATEMENT_VIEW_SECTION = "current-patient/current-medications/statement/view-section";
    String CURRENT_PATIENT_PAT_HISTORY_VIEW_SECTION = "current-patient/pat-history/view-section";
    String CURRENT_PATIENT_CURRENT_DISEASES_VIEW_SECTION = "current-patient/current-diseases/view-section";
    String CURRENT_PATIENT_CURRENT_DISEASES_ADD_PAGE = "current-patient/current-diseases/add-info";
    String CURRENT_PATIENT_DEMOGRAPHIC_INFORMATION_VIEW_SECTION = "current-patient/demographic-information/view-section";
    String CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_SECTION = "current-patient/document-history-consultation/view-section";
    String CURRENT_PATIENT_CURRENT_MEDICATIONS_STATEMENT_ADD_PAGE = "current-patient/current-medications/statement/add-info";
    String CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_VIEW_SECTION = "current-patient/current-medications/prescription/view-section";
    String CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_MEDICATION_VIEW = "current-patient/current-medications/prescription/medication-view";
    String CURRENT_PATIENT_ALLERGIES_INTOLERANCES_VIEW_SECTION = "current-patient/allergies-intolerances/view-section";
    String CURRENT_PATIENT_ALLERGIES_INTOLERANCES_ADD_PAGE = "current-patient/allergies-intolerances/add-info";
    String CURRENT_PATIENT_PRESCRIPTION_ADD_PAGE = "current-patient/current-medications/prescription/add-info";
    String CURRENT_PATIENT_PRESCRIPTION_UPDATE_PAGE = "current-patient/current-medications/prescription/update-info";
    String CURRENT_PATIENT_IMMUNIZATIONS_VIEW_SECTION = "current-patient/immunization/view-section";
    String CURRENT_PATIENT_HISTORY_PROCEDURE_PROCEDURE_VIEW_SECTION = "current-patient/history-procedure/procedure/view-section";
    String CURRENT_PATIENT_HISTORY_PROCEDURE_ORGANIZATION_VIEW_SECTION = "current-patient/history-procedure/organization/view-section";
    String CURRENT_PATIENT_MEDICAL_DEVICES_DEVICE_VIEW_SECTION = "current-patient/medical-devices/device/view-section";
    String CURRENT_PATIENT_MEDICAL_DEVICES_USE_DEVICE_STATEMENT_VIEW_SECTION = "current-patient/medical-devices/use-device-statement/view-section";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_OBSERVATION_LABORATORY_VIEW = "current-patient/diagnostic-results/laboratory-results/observation-laboratory-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_SPECIMEN_VIEW = "current-patient/diagnostic-results/laboratory-results/specimen-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_REQUEST_VIEW = "current-patient/diagnostic-results/laboratory-results/request-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_RESULT_VIEW = "current-patient/diagnostic-results/laboratory-results/result-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_ORGANIZATION_LABORATORY_VIEW = "current-patient/diagnostic-results/laboratory-results/organization-laboratory-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_LABORATORY_RESULTS_OBSERVATION_LABORATORY_MEDIA_VIEW = "current-patient/diagnostic-results/laboratory-results/observation-laboratory-media-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_RADIOLOGY_RESULTS_OBSERVATION_VIEW = "current-patient/diagnostic-results/radiology-results/observation-radiology-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_RADIOLOGY_RESULTS_DEVICE_VIEW = "current-patient/diagnostic-results/radiology-results/device-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_RADIOLOGY_RESULTS_IMAGING_STUDY_VIEW = "current-patient/diagnostic-results/radiology-results/imaging-study-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_RADIOLOGY_RESULTS_ORGANIZATION_VIEW = "current-patient/diagnostic-results/radiology-results/organization-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_RADIOLOGY_RESULTS_PRACTITIONER_VIEW = "current-patient/diagnostic-results/radiology-results/practitioner-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_PATHOLOGY_RESULTS_OBSERVATION_PATHOLOGY_VIEW = "current-patient/diagnostic-results/pathology-results/observation-laboratory-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_PATHOLOGY_RESULTS_SPECIMEN_VIEW = "current-patient/diagnostic-results/pathology-results/specimen-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_PATHOLOGY_RESULTS_ORGANISATION_LABORATORY_VIEW = "current-patient/diagnostic-results/pathology-results/organization-laboratory-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_PATHOLOGY_RESULTS_OBSERVATION_LABORATORY_MEDIA_VIEW = "current-patient/diagnostic-results/pathology-results/observation-laboratory-media-view";
    String CURRENT_PATIENT_DIAGNOSTIC_RESULT_OTHER_RESULTS_VIEW_SECTION = "current-patient/diagnostic-results/other-results/view-section";

    // Visit data
    String CURRENT_PATIENT_VITAL_SIGNS_VIEW_SECTION = "current-patient/visit-data/vital-signs/view-section";
    String CURRENT_PATIENT_REASON_VIEW_SECTION = "current-patient/visit-data/reason/view-section";
    String CURRENT_PATIENT_PH_EXAM_VIEW_SECTION = "current-patient/visit-data/ph-exam/view-section";
    String CURRENT_PATIENT_CONCLUSION_VIEW_SECTION = "current-patient/visit-data/conclusion/view-section";
    String CURRENT_PATIENT_HISTORY_OF_PAST_ILLNESS_VIEW_SECTION = "current-patient/history-past-illness/view-section";
    String CURRENT_PATIENT_PREGNANCY_EDD_VIEW_SECTION = "current-patient/pregnancy/edd/view-section";
    String CURRENT_PATIENT_PREGNANCY_OUTCOME_VIEW_SECTION = "current-patient/pregnancy/outcome/view-section";
    String CURRENT_PATIENT_PREGNANCY_STATUS_VIEW_SECTION = "current-patient/pregnancy/status/view-section";
    String CURRENT_PATIENT_SOCIAL_HISTORY_ALCOHOL_VIEW_SECTION = "current-patient/social-history/alcohol/view-section";
    String CURRENT_PATIENT_SOCIAL_HISTORY_TOBACCO_VIEW_SECTION = "current-patient/social-history/tobacco/view-section";
    String CURRENT_PATIENT_PLAN_OF_CARE_VIEW_SECTION = "current-patient/plan-care/view-section";
    String CURRENT_PATIENT_FUNCTIONAL_STATUS_VIEW_SECTION = "current-patient/functional-status/view-section";
    String CURRENT_PATIENT_ADVANCE_DIRECTIVES_VIEW_SECTION = "current-patient/advance-directives/view-section";

    //Emergency
    String EMERGENCY_INDEX_TEMPLATE = "emergency/emergency-index";
}
