-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

insert into COUNTRIES (created_date, updated_date, version, alpha_2_code, alpha_3_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'RO', 'ROU', 'Romania');
insert into COUNTRIES (created_date, updated_date, version, alpha_2_code, alpha_3_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'IT', 'ITA', 'Italy');
insert into COUNTRIES (created_date, updated_date, version, alpha_2_code, alpha_3_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'FR', 'FRA', 'Belgique');

insert into CITIES (created_date, updated_date, version, name, country_id) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Bucharest', (select id from countries where alpha_2_code = 'RO'));
insert into CITIES (created_date, updated_date, version, name, country_id) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Pisa', (select id from countries where alpha_2_code = 'IT'));

insert into ADDRESSES (created_date, updated_date, version, city_id, use, postal_code, street, number, details)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, (select id from cities where name = 'Pisa'), 'WORK', '013685', 'CNR Foundation / Tuscany Region', '1', 'Giuseppe Moruzzi');
insert into ADDRESSES (created_date, updated_date, version, city_id, use, postal_code, street, number, details)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, (select id from cities where name = 'Pisa'), 'WORK', '56124', 'CNR Foundation / Tuscany Region', '12', 'Sector 4');

insert into CONTACT_POINTS (created_date, updated_date, version, type, use, value) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'PHONE', 'WORK', '+39 050 315 2216');
--insert into CONTACT_POINTS (created_date, updated_date, version, type, use, value) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'PHONE', 'WORK', '+4021 334 30 26');
--insert into CONTACT_POINTS (created_date, updated_date, version, type, use, value) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'PHONE', 'WORK', '+4021 334 30 27');

insert into HEALTH_CARE_OCCUPATION_GROUPS (created_date, updated_date, version, isco_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, '2211', 'Generalist medical practitioners');
insert into HEALTH_CARE_OCCUPATION_GROUPS (created_date, updated_date, version, isco_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, '2212', 'Specialist medical practitioners');

insert into HEALTH_CARE_OCCUPATIONS (created_date, updated_date, version, name, group_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Medical doctor specialist in Cardiology', (select id from health_care_occupation_groups where isco_code = '2211'));
insert into HEALTH_CARE_OCCUPATIONS (created_date, updated_date, version, name, group_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Medical officer (general)', (select id from health_care_occupation_groups where isco_code = '2211'));

insert into PERSONS (created_date, updated_date, version, first_name, last_name, gender, birth_date)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Giuseppe', 'Vergaro', 'MALE', parsedatetime('24-11-1983', 'dd-MM-yyyy'));

insert into HEALTH_CARE_PROFESSIONAL (id, OCCUPATION_ID) values ((select id from persons where first_name = 'Giuseppe'), (select id from health_care_occupations where name = 'Medical doctor specialist in Cardiology'));

insert into PERSON_ADDRESS (person_id, address_id) values ((select id from persons where first_name = 'Giuseppe'), (select id from addresses where postal_code = '013685'));

insert into HEALTH_CARE_ORGANIZATION (created_date, updated_date, version, code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'FTGM', 'Fondazione Toscana "Gabriele Monasterio" Hospital');

insert into HCO_ADDRESS (hco_id, address_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'FTGM'), (select id from addresses where postal_code = '56124'));

insert into HCO_CONTACT_POINT (hco_id, contact_point_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'FTGM'), (select id from CONTACT_POINTS where value = '+39 050 315 2216'));
--insert into HCO_CONTACT_POINT (hco_id, contact_point_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'SCUBA'), (select id from CONTACT_POINTS where value = '+4021 334 30 26'));
--insert into HCO_CONTACT_POINT (hco_id, contact_point_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'SCUBA'), (select id from CONTACT_POINTS where value = '+4021 334 30 27'));

-- insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
--     values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'FC', 'Frequenza cardiaca', 'bpm', '0-300', '8867-4');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Breathing rate', 'FR', 'atti/min', '0-60', '9279-1');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Oxygen saturation in Arterial blood by Pulse oximetry', 'Sat Oxy', '%', '0-100', '59408-5');
-- insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
--     values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Weight', 'Weight', 'kg', '', '29463-7');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Heart rate', 'Heart rate', '/min', '', '8867-4');
-- insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
--     values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SpO2', 'SpO2', '%', '', '59407-7');
-- insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
--     values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Mean Arterial pressure', 'Mean Arterial pressure', 'mm[Hg]', '', '8478-0');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Body height', 'Body height', 'cm', '', '8302-2');
-- insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
--     values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Creatinine', 'Creatinine', 'mg/dL', '', '3097-3');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Systolic Blood Pressure', 'Systolic Blood Pressure', 'mmHg', '0-300', '8480-6');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Body Weight', 'Body Weight', 'kg', '0-300', '3141-9');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Diastolic Blood Pressure', 'Diastolic Blood Pressure', 'mmHg', '0-300', '8462-4');
-- insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, comm, ucum, bioprm_range, loinc)
--     values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Glucose [Mass/volume] in Serum or Plasma', 'Glucose [Mass/volume] in Serum or Plasma', 'mg/dL', '', '2345-7');

insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Vicodin', 'Opioid/acetaminophen combinations', 'R05DA03 ');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Synthroid', 'Thyroxines', 'H03AA');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Delasone', 'Corticosteroids', 'H02AB02');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Amoxil', 'Penicillin antibiotics', 'J01CA04');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Neurontin', 'Anti-epileptics', 'N03AX');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Prinivil', 'Angiotensin converting enzyme Inhibitors', 'C09AA04');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Lipitor', 'Statin', 'C10AA05');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Glucophage', 'Biguanides', 'A10BA');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Zofran', 'Serotonin antagonists', 'A03AE');
insert into PRESCRIPTION_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Motrin', 'Nonsteroidal anti-inflammatory drugs', 'G02CC01');

insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Essential (primary) hypertension', 'I10', 'I10.0');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Hypertension secondary to other renal disorders', 'I15', 'I15.1');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Hypertension secondary to endocrine disorders', 'I15', 'I15.2');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Other secondary hypertension', 'I15', 'I15.8');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Secondary hypertension, unspecified', 'I15', 'I15.9');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Hypertensive heart disease with heart failure', 'I11', 'I11.0');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Hypertensive heart disease without heart failure', 'I11', 'I11.9');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Dilated cardiomyopathy', 'I42', 'I42.0');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Obstructive hypertrophic cardiomyopathy', 'I42', 'I42.1');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Pre-existing type 1 diabetes mellitus, in pregnancy, childbirth and the puerperium', 'O24', 'O24.0');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Pre-existing type 1 diabetes mellitus, in pregnancy', 'O24', 'O24.1');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Pre-existing type 1 diabetes mellitus, in childbirth', 'O24' ,'O24.2');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Pre-existing type 1 diabetes mellitus, in the puerperium','O24','O24.3');
insert into CURRENT_DISEASE_TYPES (created_date, updated_date, version, name, DRUG_CLASS, loinc)
   values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Poisoning by, adverse effect of and underdosing of insulin and oral hypoglycemic [antidiabetic] drugs', 'T38', 'T38.3');

insert into ALLERGY_TYPES (created_date, updated_date, version, name, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Feather', 'V01AA01');
insert into ALLERGY_TYPES (created_date, updated_date, version, name, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Grass pollen', 'V01AA02');
insert into ALLERGY_TYPES (created_date, updated_date, version, name, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'House dust mites', 'V01AA03');
insert into ALLERGY_TYPES (created_date, updated_date, version, name, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Mould Fungus and yeast fungus', 'V01AA04');
insert into ALLERGY_TYPES (created_date, updated_date, version, name, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Tree pollen', 'V01AA05');
insert into ALLERGY_TYPES (created_date, updated_date, version, name, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Insects', 'V01AA07');
insert into ALLERGY_TYPES (created_date, updated_date, version, name, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Food', 'V01AA08');
insert into ALLERGY_TYPES (created_date, updated_date, version, name,loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Textiles', 'V01AA09');
insert into ALLERGY_TYPES (created_date, updated_date, version, name, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Flowers', 'V01AA10');
insert into ALLERGY_TYPES (created_date, updated_date, version, name, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Animals', 'V01AA11');
insert into ALLERGY_TYPES (created_date, updated_date, version, name, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Various', 'V01AA020');

insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Creatinine', 'mg/dL', '3097-3');
insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Potassium [Moles/volume] in Blood', 'mEq/L', '75940-7');
insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Sodium [Moles/volume] in Blood', 'mEq/L', '2947-0');
insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Troponin T.cardiac ', 'ng/L', '67151-1');
insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Natriuretic peptide.B prohormone N-Terminal [Mass/volume] in Serum or Plasma', 'ng/L', '33762-6');
insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Hemoglobin HGB', 'mg/dL', '41995-2');
insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Glucose [Mass/volume] in Serum or Plasma', 'mg/dL', '2345-7');
insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Urea in Blood', 'mg/dL', '20977-5');
insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Glomerular filtration rate/1.73 sq M.predicted ', 'mL/min/1,73m2', '77147-7');
insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Chloride [Moles/volume] in Blood', 'mEq/L', '2069-3');
insert into LABORATORY_TESTS_TYPES (created_date, updated_date, version, name, ucum, loinc)
  values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Renin [Enzymatic activity/volume] in Plasma', 'mU/L', '2915-7');