create table vaccine_to_pet
(
    id              BIGSERIAL PRIMARY KEY NOT NULL,
    pet_id          BIGINT NOT NULL,
    vaccine_id      BIGINT NOT NULL,
    date_of_vaccine DATE DEFAULT CURRENT_DATE
);

