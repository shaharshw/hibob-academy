CREATE TABLE IF NOT EXISTS feedback (
                                        id BIGSERIAL PRIMARY KEY,
                                        company_id BIGINT NOT NULL,
                                        sender_id BIGINT NOT NULL,
                                        text TEXT NOT NULL,
                                        is_anonymous BOOLEAN DEFAULT FALSE,
                                        created_at DATE DEFAULT CURRENT_DATE,
                                        status VARCHAR(50) DEFAULT 'Unreviewed'
    );


CREATE TABLE IF NOT EXISTS feedback_response (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 company_id BIGINT NOT NULL,
                                                 feedback_id BIGINT NOT NULL,
                                                 reviewer_id BIGINT NOT NULL,
                                                 response_text TEXT NOT NULL,
                                                 created_at DATE DEFAULT CURRENT_DATE
);



