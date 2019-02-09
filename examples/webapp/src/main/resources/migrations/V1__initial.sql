CREATE TABLE user_profiles (
  email VARCHAR NOT NULL,
  roles VARCHAR,                    
  password VARCHAR NOT NULL,
  confirm_uuid VARCHAR NOT NULL,
  confirmed BOOLEAN NOT NULL,
  user_name VARCHAR NOT NULL,
  
  CONSTRAINT users_pk PRIMARY KEY (email)
);

INSERT INTO user_profiles VALUES ('mylonglongname@gmail.com', 
                          'ROLE_ADMIN', 
                          '{bcrypt}$2a$10$o1bo4x4ELUJOVVbOkVMjWO9kX0LbACwT36NO0ICSRfM.LfStv9.X6', 
                          'd5915e4f-cd31-420b-a90c-4530e2122fbe', 
                          TRUE, 
                          'admin')