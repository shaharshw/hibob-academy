CREATE TABLE IF NOT EXISTS company (
                                       id SERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS employees (
                                         id SERIAL PRIMARY KEY,
                                         company_id BIGINT,
                                         first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    department VARCHAR(50) NOT NULL
    );

INSERT INTO company (name) VALUES
                               ('Friends'),
                               ('Seinfeld');

-- Insert employees for Friends company (id=1)
INSERT INTO employees (company_id, first_name, last_name, role, department) VALUES
                                                                                (1, 'Rachel', 'Green', 'admin', 'Management'),
                                                                                (1, 'Ross', 'Geller', 'hr', 'HR'),
                                                                                (1, 'Monica', 'Geller', 'hr', 'HR'),
                                                                                (1, 'Chandler', 'Bing', 'hr', 'HR'),
                                                                                (1, 'Joey', 'Tribbiani', 'employee', 'R&D'),
                                                                                (1, 'Phoebe', 'Buffay', 'employee', 'R&D'),
                                                                                (1, 'Gunther', 'Central', 'employee', 'Operations'),
                                                                                (1, 'Janice', 'Hosenstein', 'employee', 'Operations'),
                                                                                (1, 'Mike', 'Hannigan', 'employee', 'R&D'),
                                                                                (1, 'David', 'Scientist', 'employee', 'R&D'),
                                                                                (1, 'Ben', 'Geller', 'employee', 'R&D'),
                                                                                (1, 'Carol', 'Willick', 'employee', 'R&D'),
                                                                                (1, 'Susan', 'Bunch', 'employee', 'Operations'),
                                                                                (1, 'Emily', 'Waltham', 'employee', 'Operations'),
                                                                                (1, 'Richard', 'Burke', 'employee', 'Operations');

-- Insert employees for Seinfeld company (id=2)
INSERT INTO employees (company_id, first_name, last_name, role, department) VALUES
                                                                                (2, 'Jerry', 'Seinfeld', 'admin', 'Management'),
                                                                                (2, 'George', 'Costanza', 'hr', 'HR'),
                                                                                (2, 'Elaine', 'Benes', 'hr', 'HR'),
                                                                                (2, 'Cosmo', 'Kramer', 'hr', 'HR'),
                                                                                (2, 'Newman', 'Postman', 'employee', 'Operations'),
                                                                                (2, 'Frank', 'Costanza', 'employee', 'Operations'),
                                                                                (2, 'Estelle', 'Costanza', 'employee', 'Operations'),
                                                                                (2, 'David', 'Puddy', 'employee', 'R&D'),
                                                                                (2, 'Kenny', 'Bania', 'employee', 'R&D'),
                                                                                (2, 'Jackie', 'Chiles', 'employee', 'R&D'),
                                                                                (2, 'Sue', 'Mischke', 'employee', 'R&D'),
                                                                                (2, 'Morty', 'Seinfeld', 'employee', 'Operations'),
                                                                                (2, 'Helen', 'Seinfeld', 'employee', 'Operations'),
                                                                                (2, 'Uncle', 'Leo', 'employee', 'Operations'),
                                                                                (2, 'Babu', 'Bhatt', 'employee', 'Operations');