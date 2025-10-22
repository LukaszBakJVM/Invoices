insert into seller(name, nip,regon,city,zip_code,street,house_number)values ('name1','8133209246','060293388'
                                                                                    ,'WARSZAWA','00-999','ulica1','1');
insert into seller(name, nip,regon,city,zip_code,street,house_number)values ('name2','8735769536','060293387','WARSZAWA','00-992','ulica2','2');
insert into seller(name, nip,regon,city,zip_code,street,house_number,account_nb)values ('name3','8735769537','060293389','WARSZAWA','00-999','ulica3','4','768889999000989886353443');


insert into users(  email, password, nip,  active,seller_id)values ('test1@test.pl','{bcrypt}$2a$10$u8sMRN9zavkP4B0wcpObyeEsO7zYI9BCy9DfqT0PD4RTeGNlkZ4o.'
                                                         , '8133209246', true,1);
insert into users( email, password, nip,  active,seller_id)values ('test2@test.pl','{bcrypt}$2a$10$u8sMRN9zavkP4B0wcpObyeEsO7zYI9BCy9DfqT0PD4RTeGNlkZ4o.',
                                                          '8733134841', true,2);
                                                                        --pass lukasz
insert into users(email, password, nip, active,seller_id)values ('test3@test.pl','{bcrypt}$2a$10$u8sMRN9zavkP4B0wcpObyeEsO7zYI9BCy9DfqT0PD4RTeGNlkZ4o.',
                                                       '8733134855', false,3);






