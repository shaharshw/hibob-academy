CREATE TABLE IF NOT EXISTS feedback (
                                        id BIGSERIAL PRIMARY KEY,
                                        company_id BIGINT NOT NULL,
                                        sender_id BIGINT NOT NULL,
                                        text TEXT NOT NULL,
                                        is_anonymous BOOLEAN DEFAULT FALSE,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        status VARCHAR(50) DEFAULT 'Unreviewed',
                                        last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS feedback_response (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 company_id BIGINT NOT NULL,
                                                 feedback_id BIGINT NOT NULL,
                                                 reviewer_id BIGINT NOT NULL,
                                                 response_text TEXT NOT NULL,
                                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                 last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

CREATE UNIQUE INDEX unique_feedback_responder_idx ON feedback_response (company_id, feedback_id, reviewer_id);



