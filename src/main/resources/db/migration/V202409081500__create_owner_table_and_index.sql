create table owner
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255),
    company_id  UUID,
    employee_id UUID
);

create index idx_owner_company_id_employee_id
    on owner (company_id, employee_id);


/*
    insert into pets(name, type, data_of_arrival, company_id)
    values('Leo', 'Dog', '2022-09-08', 'f7b3b3b3-7b3b-4b3b-8b3b-3b3b3b3b3b3b');

    insert into pets(name, type, data_of_arrival, company_id)
    values('Tom', 'Cat', '2024-09-08', 'f7b3b3b3-7b3b-4b3b-8b3b-3b3b3b3b3b3b');

    select * from pets where type = 'Dog';

    delete from pets where id = '8b3fe77a-d799-4684-ad3a-7794e4e25bb5';

    select * from pets where data_of_arrival < '2023-09-08';
 */