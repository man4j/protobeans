CREATE TABLE codes (
  cis VARCHAR NOT NULL,
  order_id VARCHAR NOT NULL,
  report_id VARCHAR NOT NULL,                    
  
  CONSTRAINT codes_pk PRIMARY KEY (cis)
);