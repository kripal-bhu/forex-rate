CREATE TABLE EXCHANGE_RATE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exchange_date DATE NOT NULL,    -- Renamed to something more descriptive
    source VARCHAR(255) NOT NULL
);

CREATE TABLE RATE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency VARCHAR(50) NOT NULL,
    rate_value DECIMAL(18, 6) NOT NULL,
    exchange_rate_id BIGINT,  -- Foreign key to EXCHANGE_RATE
    FOREIGN KEY (exchange_rate_id)
        REFERENCES EXCHANGE_RATE(id)
);