CREATE TABLE users (id UUID PRIMARY KEY NOT NULL, 
                    roles STRING(512),
                    email STRING(256) UNIQUE NOT NULL,
                    password STRING(128) NOT NULL,
                    confirm_uuid STRING(64) NOT NULL,
                    confirmed BOOL NOT NULL,
                    userName STRING(128) NOT NULL,
                    FAMILY "primary" (id, roles, email, password, confirm_uuid, confirmed, userName)
);

INSERT INTO users VALUES ('d5915e4f-cd31-420b-a90c-4530e2122fbe', 
                          'ROLE_ADMIN', 
                          'mylonglongname@gmail.com', 
                          '{bcrypt}$2a$10$o1bo4x4ELUJOVVbOkVMjWO9kX0LbACwT36NO0ICSRfM.LfStv9.X6', 
                          'd5915e4f-cd31-420b-a90c-4530e2122fbe', 
                          TRUE, 
                          'admin')