CREATE TABLE products
(
    id            UUID PRIMARY KEY    NOT NULL,
    title         VARCHAR(255)        NOT NULL,
    article       VARCHAR(255) UNIQUE NOT NULL,
    description   TEXT                NOT NULL,
    product_type  VARCHAR(255)        NOT NULL,
    price         DECIMAL(10, 2)      NOT NULL,
    quantity      DECIMAL(10, 2)      NOT NULL,
    last_modified TIMESTAMP           NOT NULL,
    created_ad    DATE                NOT NULL
);
