-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

insert into COUNTRIES (created_date, updated_date, version, alpha_2_code, alpha_3_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'RO', 'ROU', 'Romania');
insert into COUNTRIES (created_date, updated_date, version, alpha_2_code, alpha_3_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'IT', 'ITA', 'Italy');

insert into CITIES (created_date, updated_date, version, name, country_id) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Bucharest', (select id from countries where alpha_2_code = 'RO'));
insert into CITIES (created_date, updated_date, version, name, country_id) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Rome', (select id from countries where alpha_2_code = 'IT'));

insert into ADDRESSES (created_date, updated_date, version, city_id, use, postal_code, street, number, details)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, (select id from cities where name = 'Bucharest'), 'WORK', '013685', 'Soseaua Bucuresti-Ploiesti', '73-81', 'Sector 1, Victoria Park, Cladirea 4');
insert into ADDRESSES (created_date, updated_date, version, city_id, use, postal_code, street, number, details)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, (select id from cities where name = 'Bucharest'), 'WORK', '041915', 'Șoseaua Berceni', '12', 'Sector 4');

insert into CONTACT_POINTS (created_date, updated_date, version, type, use, value) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'PHONE', 'WORK', '+4021 334 30 25');
insert into CONTACT_POINTS (created_date, updated_date, version, type, use, value) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'PHONE', 'WORK', '+4021 334 30 26');
insert into CONTACT_POINTS (created_date, updated_date, version, type, use, value) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'PHONE', 'WORK', '+4021 334 30 27');

insert into HEALTH_CARE_OCCUPATION_GROUPS (created_date, updated_date, version, isco_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, '2211', 'Generalist medical practitioners');
insert into HEALTH_CARE_OCCUPATION_GROUPS (created_date, updated_date, version, isco_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, '2212', 'Specialist medical practitioners');

insert into HEALTH_CARE_OCCUPATIONS (created_date, updated_date, version, name, group_id)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Medical doctor (general)', (select id from health_care_occupation_groups where isco_code = '2211'));
insert into HEALTH_CARE_OCCUPATIONS (created_date, updated_date, version, name, group_id)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Medical officer (general)', (select id from health_care_occupation_groups where isco_code = '2211'));

insert into PERSONS (created_date, updated_date, version, first_name, last_name, gender, birth_date)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Enzo Anselmo', 'Ferrari', 'MALE', parsedatetime('01-01-1990', 'dd-MM-yyyy'));

insert into HEALTH_CARE_PROFESSIONAL (id, OCCUPATION_ID) values ((select id from persons where first_name = 'Enzo Anselmo'), (select id from health_care_occupations where name = 'Medical doctor (general)'));

insert into PERSON_ADDRESS (person_id, address_id) values ((select id from persons where first_name = 'Enzo Anselmo'), (select id from addresses where postal_code = '013685'));

insert into HEALTH_CARE_ORGANIZATION (created_date, updated_date, version, code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SCUBA', 'Spitalul Clinic de Urgenta Bagdasar-Arseni');

insert into HCO_ADDRESS (hco_id, address_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'SCUBA'), (select id from addresses where postal_code = '041915'));

insert into HCO_CONTACT_POINT (hco_id, contact_point_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'SCUBA'), (select id from CONTACT_POINTS where value = '+4021 334 30 25'));
insert into HCO_CONTACT_POINT (hco_id, contact_point_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'SCUBA'), (select id from CONTACT_POINTS where value = '+4021 334 30 26'));
insert into HCO_CONTACT_POINT (hco_id, contact_point_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'SCUBA'), (select id from CONTACT_POINTS where value = '+4021 334 30 27'));

insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'FC', 'Frequenza cardiaca', 'bpm', '0-300', '8867-4');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'FR', 'Frequenza respiratoria', 'atti/min', '0-60', '9279-1');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Sat Oxy', 'Oxygen saturation in Arterial blood by Pulse oximetry', '%', '0-100', '59408-5');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Weight', 'Weight', 'kg', '', '29463-7');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Heart rate', 'Heart rate', '/min', '', '73799-9');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SpO2', 'SpO2', '%', '', '59407-7');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Mean Arterial pressure', 'Mean Arterial pressure', 'mm[Hg]', '', '8478-0');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Body height', 'Body height', 'cm', '', '8302-2');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Creatinine', 'Creatinine', 'mg/dL', '', '3097-3');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Systolic Blood Pressure', 'Systolic Blood Pressure', 'mmHg', '0-300', '8480-6');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Body Weight', 'Body Weight', 'kg', '0-300', '3141-9');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Diastolic Blood Pressure', 'Diastolic Blood Pressure', 'mmHg', '0-300', '8462-4');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Glucose [Mass/volume] in Serum or Plasma', 'Glucose [Mass/volume] in Serum or Plasma', 'mg/dL', '', '2345-7');

insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Vicodin', 'Opioid/acetaminophen combinations', '1');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Synthroid', 'Thyroxines', '2');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Delasone', 'Corticosteroids', '3');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Amoxil', 'Penicillin antibiotics', '4');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Neurontin', 'Anti-epileptics', '5');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Prinivil', 'Angiotensin converting enzyme Inhibitors', '6');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Lipitor', 'Statin', '7');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Glucophage', 'Biguanides', '8');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Zofran', 'Serotonin antagonists', '9');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Motrin', 'Nonsteroidal anti-inflammatory drugs', '10');