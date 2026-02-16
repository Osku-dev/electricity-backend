CREATE TABLE price (
    id BIGINT PRIMARY KEY,
    start_time TIMESTAMPTZ NOT NULL,
    price_cents NUMERIC(10,3) NOT NULL,
    resolution_minutes INTEGER NOT NULL DEFAULT 60
);