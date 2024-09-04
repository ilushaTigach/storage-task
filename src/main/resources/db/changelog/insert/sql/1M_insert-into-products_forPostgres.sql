INSERT INTO products (id, title, article, description, product_type, price, quantity, last_modified, created_ad)
SELECT
    gen_random_uuid(),
    'Product ' || seq,
    'ART-' || seq,
    'Description for product ' || seq,
    'TECH',
    100,
    100,
    current_timestamp,
    current_date
FROM generate_series(1, 500000) seq;