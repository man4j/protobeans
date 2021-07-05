CREATE TABLE stats_codes
(  
  order_id String,  
  issuer_id String,
  issuer_name String,  
  sender_id String,
  sender_name String,  
  recipient_area_id String,
  recipient_area_name String,    
  create_time DateTime,  
  ordered_codes Int32,
  applied_codes Int32,
  left_codes Int32,
  rejected_codes Int32,
  unprocessed_codes Int32,  
  gtin String
) 
ENGINE = ReplacingMergeTree(create_time)
PARTITION BY toYYYYMM(create_time)
ORDER BY (order_id, gtin);


CREATE MATERIALIZED VIEW stats_codes_outgoing
ENGINE = ReplacingMergeTree(create_time)
PARTITION BY toYYYYMM(create_time) ORDER BY (sender_id, recipient_area_id, order_id, gtin) -- для поиска исходящих документов (когда включен фильтр Отправленные)
POPULATE
AS SELECT
  order_id,
  gtin,  
  ordered_codes,
  applied_codes,
  left_codes,
  rejected_codes,
  unprocessed_codes,  
  issuer_id,
  issuer_name,  
  sender_id,
  sender_name,  
  recipient_area_id,
  recipient_area_name,  
  create_time
FROM stats_codes;


CREATE MATERIALIZED VIEW stats_codes_incoming
ENGINE = ReplacingMergeTree(create_time)
PARTITION BY toYYYYMM(create_time) ORDER BY (recipient_area_id, sender_id, order_id, gtin) -- для поиска входящих документов (когда включен фильтр Отправленные)
POPULATE
AS SELECT
  order_id,
  gtin,  
  ordered_codes,
  applied_codes,
  left_codes,
  rejected_codes,
  unprocessed_codes,  
  issuer_id,
  issuer_name,  
  sender_id,
  sender_name,  
  recipient_area_id,
  recipient_area_name,  
  create_time
FROM stats_codes;