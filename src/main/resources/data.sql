-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

insert into COUNTRIES (created_date, updated_date, version, alpha_2_code, alpha_3_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'RO', 'ROU', 'Romania');
insert into COUNTRIES (created_date, updated_date, version, alpha_2_code, alpha_3_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'IT', 'ITA', 'Italy');

insert into CITIES (created_date, updated_date, version, name, country_id) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Bucharest', (select id from countries where alpha_2_code = 'RO'));
insert into CITIES (created_date, updated_date, version, name, country_id) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Rome', (select id from countries where alpha_2_code = 'IT'));

insert into ADDRESSES (created_date, updated_date, version, city_id, use, postal_code, street, number, details) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, (select id from cities where name = 'Bucharest'), 'WORK', '013685', 'Soseaua Bucuresti-Ploiesti', '73-81', 'Sector 1, Victoria Park, Cladirea 4');
insert into ADDRESSES (created_date, updated_date, version, city_id, use, postal_code, street, number, details) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, (select id from cities where name = 'Bucharest'), 'WORK', '041915', 'Șoseaua Berceni', '12', 'Sector 4');

insert into CONTACT_POINTS (created_date, updated_date, version, type, use, value) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'PHONE', 'WORK', '+4021 334 30 25');
insert into CONTACT_POINTS (created_date, updated_date, version, type, use, value) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'PHONE', 'WORK', '+4021 334 30 26');
insert into CONTACT_POINTS (created_date, updated_date, version, type, use, value) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'PHONE', 'WORK', '+4021 334 30 27');

insert into HEALTH_CARE_OCCUPATION_GROUPS (created_date, updated_date, version, isco_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, '2211', 'Generalist medical practitioners');
insert into HEALTH_CARE_OCCUPATION_GROUPS (created_date, updated_date, version, isco_code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, '2212', 'Specialist medical practitioners');

insert into HEALTH_CARE_OCCUPATIONS (created_date, updated_date, version, name, group_id) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Medical doctor (general)', (select id from health_care_occupation_groups where isco_code = '2211'));
insert into HEALTH_CARE_OCCUPATIONS (created_date, updated_date, version, name, group_id) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Medical officer (general)', (select id from health_care_occupation_groups where isco_code = '2211'));

insert into PERSONS (created_date, updated_date, version, first_name, last_name, gender, birth_date) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Ion', 'Popescu', 'MALE', parsedatetime('01-01-1990', 'dd-MM-yyyy'));

insert into HEALTH_CARE_PROFESSIONAL (id, OCCUPATION_ID) values ((select id from persons where first_name = 'Ion'), (select id from health_care_occupations where name = 'Medical doctor (general)'));

insert into PERSON_ADDRESS (person_id, address_id) values ((select id from persons where first_name = 'Ion'), (select id from addresses where postal_code = '013685'));

insert into HEALTH_CARE_ORGANIZATION (created_date, updated_date, version, code, name) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SCUBA', 'Spitalul Clinic de Urgenta Bagdasar-Arseni');

insert into HCO_ADDRESS (hco_id, address_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'SCUBA'), (select id from addresses where postal_code = '041915'));

insert into HCO_CONTACT_POINT (hco_id, contact_point_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'SCUBA'), (select id from CONTACT_POINTS where value = '+4021 334 30 25'));
insert into HCO_CONTACT_POINT (hco_id, contact_point_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'SCUBA'), (select id from CONTACT_POINTS where value = '+4021 334 30 26'));
insert into HCO_CONTACT_POINT (hco_id, contact_point_id) values ((select id from HEALTH_CARE_ORGANIZATION where code = 'SCUBA'), (select id from CONTACT_POINTS where value = '+4021 334 30 27'));

insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, description) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Heart rate', 'Heart beats per minute');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, description) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Systolic blood pressure', 'Systolic blood pressure');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, description) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Diastolic blood pressure', 'Diastolic blood pressure');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, description) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Temperature', 'Body temperature');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, description) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Height', 'Body height');
insert into VITAL_SIGNS_TYPES (created_date, updated_date, version, name, description) values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Weight', 'Body weight');