IF (SELECT COUNT(*) FROM project) = 0
BEGIN
    -- Insert statements
    INSERT INTO project (name) VALUES ('A1');
	INSERT INTO project (name) VALUES ('A2');
	INSERT INTO project (name) VALUES ('A3');

END;