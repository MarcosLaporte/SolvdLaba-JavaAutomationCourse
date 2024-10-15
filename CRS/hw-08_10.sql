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

/* Statements for insertion. */
INSERT INTO customers (full_name, email, phone_no, address, zip) VALUES
    ('Lara Croft', 'lara.croft@email.com', 66778899001, 'Croft Manor, Surrey, England', 96341),
    ('John McClane', 'john.mcclane@nakatomi.com', 77889900112, '2121 Avenue of the Stars, LA', 48156),
    ('Walter White', 'walter.white@email.com', 88990011223, '308 Negra Arroyo Lane, Albuquerque', 87111);

INSERT INTO technicians (full_name, salary) VALUES
    ('Alan Turing', 80000.00),
    ('Grace Hopper', 75000.00),
    ('Linus Torvalds', 95000.00),
    ('Ada Lovelace', 82500.00),
    ('Bill Gates', 150000.00);

INSERT INTO suppliers (full_name, email, phone_no, address) VALUES
    ('Wayne Enterprises', 'contact@wayneenterprises.com', 1234567890, '1007 Mountain Drive, Gotham'),
    ('Stark Industries', 'info@starkindustries.com', 2345678901, '200 Park Avenue, New York City'),
    ('Cyberdyne Systems', 'support@cyberdyne.com', 3456789012, '800 Terminator Blvd, Los Angeles');

INSERT INTO parts (supplier_id, name, description, value, stock) VALUES
    (102, '1TB HDD', 'High-capacity hard drive for storage', 49.99, 40),
    (106, 'Intel Core i7 Processor', 'High-performance CPU for multitasking', 329.99, 15),
    (103, '500W Power Supply', 'Standard power supply for desktop PCs', 59.99, 30),
    (104, '27-inch Monitor', 'Full HD display with crisp visuals', 199.99, 20),
    (102, 'Mechanical Keyboard', 'Durable keyboard with mechanical switches', 89.99, 25),
    (101, 'Wireless Mouse', 'Ergonomic mouse with wireless connectivity', 29.99, 40),
    (105, 'CPU Cooler', 'Efficient cooling system for processor', 49.99, 30),
    (106, 'ATX Motherboard', 'Motherboard with multiple PCIe slots', 149.99, 20),
    (101, 'USB 3.0 Hub', 'Multi-port USB hub for fast data transfer', 24.99, 35),
    (102, '256GB NVMe SSD', 'Ultra-fast storage with PCIe interface', 109.99, 50);

INSERT INTO jobs_technicians (job_id, tech_id, task)
    SELECT @job_id, i.tech_id, 'technician task'
    FROM jobs j, invoices i WHERE j.job_id = @job_id AND j.ticket_id = i.ticket_id;

INSERT INTO payments (job_id, pay_date, amount)
    SELECT @job_id, (CURDATE() + INTERVAL 2 DAY), amount
    FROM invoices WHERE ticket_id = @current_ticket_id;


/* Statements for updating. */
UPDATE technicians SET salary = (salary+salary*0.15) WHERE id_tech >= 103 AND id_tech <= 106;
UPDATE customers SET email = REPLACE(email, '@email.', '@gmail.') WHERE email LIKE '%@email.%';
UPDATE parts SET value = value+15 WHERE part_id = 1006;
UPDATE parts SET stock = stock+5 WHERE part_id = 1003;
UPDATE suppliers SET phone_no = 5486574251 WHERE supplier_id = 102;
UPDATE parts p JOIN repair_tickets_part rtp ON p.part_id = rtp.part_id
    SET p.stock = p.stock - rtp.quantity
    WHERE rtp.ticket_id = 1001;
UPDATE repair_tickets SET status = 2 WHERE ticket_id = @current_ticket_id;
UPDATE invoices SET diagnosis = 'Place new RAM' WHERE ticket_id = 10001;
UPDATE repair_tickets_part SET part_id = 1001 WHERE ticket_id = 10002 AND part_id = 1002;
UPDATE jobs SET date_finish=(CURDATE() + INTERVAL 1 DAY) WHERE job_id = 10003;


/* Statements for deletion. */
DELETE FROM technicians WHERE tech_id = 106;
DELETE rt, rtp, i FROM repair_tickets rt
    LEFT JOIN repair_tickets_part rtp ON rt.ticket_id = rtp.ticket_id
    LEFT JOIN invoices i ON rt.ticket_id = i.ticket_id
    WHERE rt.status = 3; -- Rejected invoice
DELETE FROM parts WHERE supplier_id = 103;
DELETE FROM repair_tickets WHERE rt.cust_id = 102;
DELETE FROM invoices WHERE ticket_id IN (
    SELECT ticket_id FROM jobs WHERE date_finish IS NOT NULL
);
DELETE FROM jobs WHERE job_id IN (
    SELECT job_id FROM jobs_technicians WHERE tech_id = 103
);
DELETE FROM repair_tickets WHERE ticket_id = 10001;
DELETE FROM repair_tickets WHERE ticket_id IN (
    SELECT ticket_id FROM repair_tickets WHERE computer_desc LIKE '%HP%'
);
DELETE FROM jobs WHERE job_id NOT IN (SELECT job_id FROM invoices);
DELETE FROM parts WHERE stock = 0 AND part_id NOT IN (
    SELECT part_id FROM repair_tickets_part
);


/* Alter table. */
ALTER TABLE customers AUTO_INCREMENT=101;
ALTER TABLE technicians ADD email VARCHAR(255) NOT NULL;
ALTER TABLE technicians DROP COLUMN email;
ALTER TABLE payments RENAME COLUMN amount TO total;
ALTER TABLE customers MODIFY COLUMN email VARCHAR(255) UNIQUE;
ALTER TABLE suppliers MODIFY COLUMN phone_no VARCHAR(9);
ALTER TABLE suppliers MODIFY COLUMN phone_no BIGINT;

/* Big statement to join all tables in the database. */
SELECT 
    rt.ticket_id AS 'Ticket ID',
    c.full_name AS 'Customer',
    rt.computer_desc AS 'Computer',
    rt.issue,
    i.diagnosis,
    t.full_name AS 'Diagnosis author',
    COUNT(DISTINCT jt.tech_id) AS 'Amount of techs',
    SUM(DISTINCT rtp.quantity) AS 'Amount of parts',
    GROUP_CONCAT(DISTINCT CONCAT(p.name, ' (x', rtp.quantity, ')')) AS 'Parts used',
    GROUP_CONCAT(DISTINCT s.full_name) AS 'Suppliers',
    j.date_start AS 'Job start date',
    j.date_finish AS 'Job date finish',
    f.cust_comment AS 'Customer review',
    f.rating
FROM
    repair_tickets rt
        LEFT JOIN repair_tickets_part rtp ON rtp.ticket_id = rt.ticket_id
        LEFT JOIN parts p ON p.part_id = rtp.part_id
        LEFT JOIN suppliers s ON s.supplier_id = p.supplier_id
        INNER JOIN invoices i ON i.ticket_id = rt.ticket_id
        INNER JOIN technicians t ON t.tech_id = i.tech_id
        INNER JOIN customers c ON c.cust_id = rt.cust_id
        INNER JOIN jobs j ON j.ticket_id = rt.ticket_id
        LEFT JOIN jobs_technicians jt ON jt.job_id = j.job_id
        LEFT JOIN payments py ON py.job_id = j.job_id
        LEFT JOIN feedbacks f ON f.job_id = j.job_id
GROUP BY rt.ticket_id;


/* Statements with left, right, inner, outer joins */
-- Gets ALL tickets and customer related to each (if exists)
SELECT rt.ticket_id, rt.issue, rt.date_submitted, c.full_name, c.email
    FROM repair_tickets rt
    LEFT JOIN customers c ON rt.cust_id = c.cust_id;

-- Gets ALL parts and supplier related to it (if exists)
SELECT s.full_name AS supplier, p.name AS part_name, p.stock
    FROM suppliers s
    RIGHT JOIN parts p ON s.supplier_id = p.supplier_id;

-- Gets ALL tickets, with its respective customer and status description
SELECT rt.ticket_id, c.cust_id, c.full_name AS 'customer', rt.computer_desc AS 'computer', rt.issue, s.description AS 'status'
    FROM repair_tickets rt
    JOIN ticket_status s ON rt.status = s.status
    JOIN customers c ON c.cust_id = rt.cust_id;

-- Gets ALL parts and the tickets they were used in (if any)
SELECT p.part_id, p.name, s.full_name AS supplier, GROUP_CONCAT(DISTINCT rtp.ticket_id ORDER BY rtp.ticket_id) AS 'Ticket IDs', SUM(rtp.quantity) AS 'Total Quantity Used'
    FROM parts p
    LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id
    LEFT JOIN repair_tickets_part rtp ON p.part_id = rtp.part_id
    GROUP BY p.part_id
UNION -- Behaves like FULL OUTER JOIN
-- Gets ALL repair tickets and parts used (if any)
SELECT p.part_id, p.name, s.full_name AS 'Supplier', GROUP_CONCAT(DISTINCT rtp.ticket_id ORDER BY rtp.ticket_id) AS 'Ticket IDs', SUM(rtp.quantity) AS 'Total Quantity Used'
    FROM parts p
    RIGHT JOIN suppliers s ON p.supplier_id = s.supplier_id
    RIGHT JOIN repair_tickets_part rtp ON p.part_id = rtp.part_id
    GROUP BY p.part_id;

-- Gets ALL technicians and the jobs they’ve worked on, even showing jobs without technicians or feedback
SELECT t.tech_id, t.full_name AS 'Technician', j.job_id, rt.ticket_id AS 'Ticket ID',
       rt.issue AS 'Ticket Issue', j.date_start, j.date_finish, 
       f.rating AS 'Customer Rating', f.cust_comment AS 'Customer Feedback'
    FROM jobs j
        RIGHT JOIN jobs_technicians jt ON j.job_id = jt.job_id
        RIGHT JOIN technicians t ON jt.tech_id = t.tech_id
        LEFT JOIN repair_tickets rt ON j.ticket_id = rt.ticket_id
        LEFT JOIN feedbacks f ON f.job_id = j.job_id
    WHERE f.rating >= 3 OR f.rating IS NULL
    GROUP BY t.tech_id, j.job_id, rt.ticket_id, f.rating
    ORDER BY t.tech_id, j.date_start;


/* Statements with aggregate functions and group by and without having. */
-- Total parts for each repair ticket
SELECT rt.ticket_id, COUNT(rtp.part_id) AS total_parts
    FROM repair_tickets rt
    LEFT JOIN repair_tickets_part rtp ON rt.ticket_id = rtp.ticket_id
    GROUP BY rt.ticket_id;

-- Amount of tickets per customer
SELECT c.full_name, COUNT(rt.ticket_id) AS ticket_count
    FROM customers c
    LEFT JOIN repair_tickets rt ON c.cust_id = rt.cust_id
    GROUP BY c.full_name;

-- Total stock per supplier
SELECT s.full_name AS supplier_name, SUM(p.stock) AS total_stock
    FROM suppliers s
    LEFT JOIN parts p ON s.supplier_id = p.supplier_id
    GROUP BY s.full_name;

-- Amount of parts per supplier
SELECT s.full_name AS 'Supplier', COUNT(p.part_id) AS 'Parts Count'
    FROM suppliers s
    LEFT JOIN parts p ON s.supplier_id = p.supplier_id
    GROUP BY s.full_name;

-- Min, max and average value of parts per supplier
SELECT s.full_name as 'Supplier', MIN(p.value) AS 'Min Value', MAX(p.value) AS 'Max Value', AVG(p.value) AS 'Avg Value'
    FROM parts p
    LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id
    GROUP BY p.supplier_id;

-- Amount of jobs per status
SELECT ts.description, COUNT(j.job_id) AS 'Job Count'
    FROM ticket_status ts
    LEFT JOIN repair_tickets rt ON ts.status = rt.status
    LEFT JOIN jobs j ON rt.ticket_id = j.ticket_id
    GROUP BY ts.description;

-- Total paid by customer
SELECT c.full_name, MIN(i.amount) AS 'Cheapest repair', MAX(i.amount) AS 'Most expensive repair', SUM(i.amount) AS 'TOTAL'
    FROM customers c
    LEFT JOIN repair_tickets rt ON rt.cust_id = c.cust_id
    LEFT JOIN invoices i ON i.ticket_id = rt.ticket_id
    GROUP BY c.full_name;


/* Statements with aggregate functions and group by and with having. */
-- Gets all customers and amount of tickets IF it's more than one
SELECT c.full_name, COUNT(rt.ticket_id) AS 'Total Tickets'
    FROM customers c
    LEFT JOIN repair_tickets rt ON c.cust_id = rt.cust_id
    GROUP BY c.full_name
    HAVING COUNT(rt.ticket_id) > 1;

-- Gets technician and salary IF it's higher than the average salary
SELECT t.full_name, t.salary
    FROM technicians t
    GROUP BY t.tech_id
    HAVING t.salary > (SELECT AVG(salary) FROM technicians);

-- Gets supplier and amount of parts IF it's more than 1
SELECT s.full_name AS 'Supplier', COUNT(p.part_id) AS 'Parts Count'
    FROM suppliers s
    LEFT JOIN parts p ON s.supplier_id = p.supplier_id
    GROUP BY s.full_name
    HAVING COUNT(p.part_id) > 1;

-- Gets job and amount IF it's more than 200
SELECT j.job_id, SUM(p.amount) AS 'Total Payment'
    FROM jobs j
    INNER JOIN payments p ON j.job_id = p.job_id
    GROUP BY j.job_id
    HAVING SUM(p.amount) > 200;

-- Gets ticket and amount of parts used IF it's more than 1
SELECT rt.ticket_id, COUNT(rtp.part_id) AS 'Parts Used'
    FROM repair_tickets rt
    LEFT JOIN repair_tickets_part rtp ON rt.ticket_id = rtp.ticket_id
    GROUP BY rt.ticket_id
    HAVING COUNT(rtp.part_id) > 1;

-- Gets part name and value IF it's more than 50
SELECT p.name, p.value
    FROM parts p
    GROUP BY p.part_id
    HAVING p.value > 50;

-- Gets jobs IF it hasn't been paid yet
SELECT j.job_id
    FROM jobs j
    LEFT JOIN payments p ON j.job_id = p.job_id
    GROUP BY j.job_id
    HAVING COUNT(p.job_id) = 0;

