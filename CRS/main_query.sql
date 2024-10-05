USE computer_repair_service;

/*
    INSERT INTO customers (full_name, email, phone_no, address, zip) VALUES ();
    INSERT INTO technicians (full_name, salary) VALUES ();
    INSERT INTO repair_tickets (cust_id, computer_desc, issue, date_submitted) VALUES ();
    INSERT INTO invoices (ticket_id, tech_id, diagnosis, amount) VALUES ();
    INSERT INTO suppliers (full_name, email, phone_no, address) VALUES ();
    INSERT INTO parts (supplier_id, name, description, value, stock) VALUES ();
    INSERT INTO repair_tickets_part (ticket_id, part_id, quantity) VALUES ();
    INSERT INTO jobs (ticket_id, date_start, date_finish) VALUES ();
    INSERT INTO jobs_technicians (job_id, tech_id, task) VALUES ();
    INSERT INTO payments (job_id, pay_date, amount) VALUES ();
    INSERT INTO feedbacks (job_id, cust_comment, rating, date_submit) VALUES ();
*/

SELECT rt.ticket_id, c.full_name AS 'customer', rt.computer_desc AS 'computer', rt.issue, s.description AS 'status'
    FROM repair_tickets rt
    JOIN ticket_status s ON rt.status = s.status
    JOIN customers c ON c.cust_id = rt.cust_id;

/* CUSTOMER */
SELECT * FROM customers;
INSERT INTO repair_tickets (cust_id, computer_desc, issue, date_submitted)
    VALUES (101, 'Lenovo Ideapad 5', 'Upgrade SSD and doesn\'t play sound', CURDATE());

/* REPAIR SERVICE */
SET @current_ticket_id = 10002;
INSERT INTO repair_tickets_part VALUES
    (@current_ticket_id, 1002, 2),
    (@current_ticket_id, 1006, 1);

SELECT rtp.ticket_id, p.*, rtp.quantity FROM repair_tickets_part rtp, parts p WHERE rtp.ticket_id = @current_ticket_id AND rtp.part_id = p.part_id;

-- Put aside necessary parts' stock
UPDATE parts p JOIN repair_tickets_part rtp ON p.part_id = rtp.part_id
    SET p.stock = p.stock - rtp.quantity
    WHERE rtp.ticket_id = @current_ticket_id;

-- NEW INVOICE, with parts total value + cost of labour
SET @labour = 225;
SET @default_tech_id = 104;
INSERT INTO invoices (ticket_id, tech_id, diagnosis, amount)
    SELECT @current_ticket_id, @default_tech_id, 'Add two new SSDs and change speaker.', (SUM(p.value * rpt.quantity) + @labour)
    FROM repair_tickets_part rpt, parts p WHERE rpt.ticket_id = @current_ticket_id AND rpt.part_id = p.part_id;

UPDATE repair_tickets SET status=1 WHERE ticket_id = @current_ticket_id;

/* CUSTOMER */
-- Customer accepts invoice
UPDATE repair_tickets SET status=2 WHERE ticket_id = @current_ticket_id;

/*
-- Customer rejects invoice
UPDATE repair_tickets SET status=3 WHERE ticket_id = @current_ticket_id;
-- Restore parts' stock
UPDATE parts p JOIN repair_tickets_part rtp ON p.part_id = rtp.part_id
    SET p.stock = p.stock + rtp.quantity
    WHERE rtp.ticket_id = @current_ticket_id;
*/

/* REPAIR SERVICE */
-- Try to add new parts (Test trigger) --
INSERT INTO repair_tickets_part
    VALUES (@current_ticket_id, 1004, 5);
--  --

INSERT INTO jobs (ticket_id, date_start)
    VALUES (@current_ticket_id, CURDATE());


-- NEW JOB_TECHNICIAN, with ID of the technician who did the Invoice
SET @job_id = (SELECT job_id FROM jobs WHERE ticket_id = @current_ticket_id);

SELECT j.*, i.diagnosis FROM jobs j, invoices i WHERE j.job_id = @job_id AND j.ticket_id = i.ticket_id;

INSERT INTO jobs_technicians
    SELECT @job_id, i.tech_id, 'Take away old SSD'
    FROM jobs j, invoices i WHERE j.job_id = @job_id AND j.ticket_id = i.ticket_id;

SELECT * FROM jobs_technicians WHERE job_id = @job_id;

-- Technician starts the Job with @job_id
UPDATE repair_tickets SET status=4 WHERE ticket_id = @current_ticket_id;

-- Random technician ID, different from the one initially assigned to Job with @job_id
DROP FUNCTION IF EXISTS random_tech_id;
CREATE FUNCTION random_tech_id (curr_job_id INT)
    RETURNS INT READS SQL DATA
    RETURN (
        SELECT t.tech_id FROM technicians t, jobs_technicians jt
        WHERE jt.job_id = curr_job_id AND
            t.tech_id NOT IN (SELECT tech_id FROM jobs_technicians WHERE job_id = @job_id)
        ORDER BY RAND() LIMIT 1
    );

INSERT INTO jobs_technicians VALUES
    (@job_id, random_tech_id(@job_id), 'Replace speaker'),
    (@job_id, random_tech_id(@job_id), 'Place new SSDs');

-- Technicians finish their task
UPDATE jobs_technicians SET done=1 WHERE job_id = @job_id AND tech_id = 00000000000; -- Check technicians IDs

UPDATE jobs SET date_finish=(CURDATE() + INTERVAL 1 DAY) WHERE job_id = @job_id;
UPDATE repair_tickets SET status=5 WHERE ticket_id = @current_ticket_id;
                                                -- SELECT ticket_id FROM jobs WHERE job_id = @job_id

/* CUSTOMER */
INSERT INTO payments (job_id, pay_date, amount)
    SELECT @job_id, (CURDATE() + INTERVAL 2 DAY), amount
    FROM invoices WHERE ticket_id = @current_ticket_id;

UPDATE repair_tickets SET status=6 WHERE ticket_id = @current_ticket_id;

INSERT INTO feedbacks (job_id, cust_comment, rating, date_submit)
    VALUES (@job_id, 'Everything works perfect. Not so good customer service.', 4, (CURDATE() + INTERVAL 2 DAY));

SELECT * FROM feedbacks;
