--- Trigger and it's function removing the first row of frames table,
--- to allow next frame to be used for next 50 posts

--- On the next update this file will be removed and corresponding
--- functionality will be implemented in microservices

CREATE OR REPLACE FUNCTION modify_frames_on_post_insert()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.id % 50 = 0 THEN

        DELETE
        FROM frames
        WHERE id = (SELECT id
                    FROM frames
                    ORDER BY id
                    LIMIT 1);
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE or replace TRIGGER trigger_modify_frames_on_post_insert
    AFTER INSERT
    ON anonymous_posts
    FOR EACH ROW
EXECUTE FUNCTION modify_frames_on_post_insert();
