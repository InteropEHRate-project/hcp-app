insert into COUNTRIES (version, alpha_2_code, alpha_3_code, name) values (0, 'RO', 'ROU', 'Romania');
insert into COUNTRIES (version, alpha_2_code, alpha_3_code, name) values (0, 'IT', 'ITA', 'Italy');

insert into CITIES (version, name, country_id) values (0, 'Bucharest', (select id from countries where alpha_2_code = 'RO'));
insert into CITIES (version, name, country_id) values (0, 'Rome', (select id from countries where alpha_2_code = 'IT'));

insert into HEALTH_CARE_OCCUPATION_GROUPS (version, isco_code, name) values (0, '2211', 'Generalist medical practitioners');
insert into HEALTH_CARE_OCCUPATION_GROUPS (version, isco_code, name) values (0, '2212', 'Specialist medical practitioners');

insert into HEALTH_CARE_OCCUPATIONS (version, name, group_id) values (0, 'Medical doctor (general)', (select id from health_care_occupation_groups where isco_code = '2211'));
insert into HEALTH_CARE_OCCUPATIONS (version, name, group_id) values (0, 'Medical officer (general)', (select id from health_care_occupation_groups where isco_code = '2211'));

insert into PERSONS (version, first_name, last_name, gender, birth_date) values (0, 'Ion', 'Popescu', 'MALE', parsedatetime('01-01-1990', 'dd-MM-yyyy'));

insert into HEALTH_CARE_PROFESSIONAL (id, OCCUPATION_ID) values ((select id from persons where first_name = 'Ion'), (select id from health_care_occupations where name = 'Medical doctor (general)'));

insert into ADDRESSES (version, city_id, use, postal_code, street, number, details) values (0, (select id from cities where name = 'Bucharest'), 'WORK', '013685', 'Soseaua Bucuresti-Ploiest', '73-81', 'Sector 1, Victoria Park, Cladirea 4');

insert into PERSON_ADDRESS (person_id, address_id) values ((select id from persons where first_name = 'Ion'), (select id from addresses where postal_code = '013685'));

insert into HEALTH_CARE_ORGANIZATION (version, code, name, phone, address) values (0, 'SCUBA', 'Spitalul Clinic de Urgenta Bagdasar-Arseni', '+4021 334 30 25 / +4021 334 30 26 / +4021 334 30 27', 'Șoseaua Berceni nr. 12, Sector 4, cod 041915, București');