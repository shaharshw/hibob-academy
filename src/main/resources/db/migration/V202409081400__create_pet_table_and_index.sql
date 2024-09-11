create table pets
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255),
    type            VARCHAR(255) NOT NULL,
    data_of_arrival DATE DEFAULT CURRENT_DATE,
    company_id      BIGINT NOT NULL
);

create unique index idx_pets_company_id on pets (company_id);