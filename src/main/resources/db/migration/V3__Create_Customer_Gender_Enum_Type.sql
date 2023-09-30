CREATE TYPE Gender AS ENUM ('MALE', 'FEMALE');

ALTER TABLE customer
ADD Gender gender NOT NULL;

CREATE FUNCTION gender_cast(varchar) RETURNS Gender AS $$
    SELECT CASE $1
        WHEN 'MALE' THEN 'MALE'::Gender
        WHEN 'FEMALE' THEN 'FEMALE'::Gender
    END;
$$ LANGUAGE SQL;

CREATE CAST (varchar AS Gender) WITH FUNCTION gender_cast(varchar) AS ASSIGNMENT;