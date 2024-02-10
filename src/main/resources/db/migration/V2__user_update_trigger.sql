--- Trigger and function for replaces the last_modified_date column of users table with NOW()
--- On the next update this file will me removed and replaced with user auditing service

CREATE OR REPLACE FUNCTION update_last_modified_date()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.last_modified_date = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



CREATE or replace TRIGGER user_trigger_update_last_modified_date
    AFTER UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION update_last_modified_date();