ALTER TABLE prices
ADD CONSTRAINT uq_prices_starttime_resolution UNIQUE (start_time, resolution_minutes);