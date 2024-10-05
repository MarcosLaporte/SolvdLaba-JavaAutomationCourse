USE COMPUTER_REPAIR_SERVICE;

DROP TABLE IF EXISTS feedbacks;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS jobs_technicians;
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS repair_tickets_part;
DROP TABLE IF EXISTS parts;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS invoices;
DROP TABLE IF EXISTS repair_tickets;
DROP TABLE IF EXISTS ticket_status;
DROP TABLE IF EXISTS technicians;
DROP TABLE IF EXISTS customers;

CREATE TABLE IF NOT EXISTS customers (
    cust_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_no BIGINT NOT NULL,
    address VARCHAR(255) NOT NULL,
    zip INT NOT NULL
);
ALTER TABLE customers AUTO_INCREMENT=101;

CREATE TABLE IF NOT EXISTS technicians (
    tech_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    salary FLOAT NOT NULL CHECK (salary > 0)
);
ALTER TABLE technicians AUTO_INCREMENT=101;

CREATE TABLE IF NOT EXISTS ticket_status (
    status INT PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);
INSERT INTO ticket_status (status, description) VALUES
    (0, 'PENDING INVOICE'),
    (1, 'INVOICE SENT'),
    (2, 'INVOICE ACCEPTED'),
    (3, 'INVOICE REJECTED'),
    (4, 'WORK IN PROGRESS'),
    (5, 'READY'),
    (6, 'PAID');

CREATE TABLE IF NOT EXISTS repair_tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    cust_id INT NOT NULL,
    computer_desc VARCHAR(255) NOT NULL,
    issue varchar(255) NOT NULL,
    date_submitted DATE NOT NULL,
    status INT NOT NULL DEFAULT 0,

    FOREIGN KEY (cust_id) REFERENCES customers(cust_id),
    FOREIGN KEY (status) REFERENCES ticket_status(status)
);
ALTER TABLE repair_tickets AUTO_INCREMENT=10001;

CREATE TABLE IF NOT EXISTS invoices (
    inv_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL UNIQUE,
    tech_id INT NOT NULL,
    diagnosis VARCHAR(255) NOT NULL,
    amount DOUBLE NOT NULL CHECK (amount >= 0),

    FOREIGN KEY (ticket_id) REFERENCES repair_tickets (ticket_id),
    FOREIGN KEY (tech_id) REFERENCES technicians(tech_id)
);
ALTER TABLE invoices AUTO_INCREMENT=10001;

CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_no BIGINT NOT NULL,
    address VARCHAR(255) NOT NULL
);
ALTER TABLE suppliers AUTO_INCREMENT=101;

CREATE TABLE IF NOT EXISTS parts (
    part_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    value DOUBLE NOT NULL CHECK (value >= 0),
    stock INT NOT NULL CHECK (stock >= 0),

    FOREIGN KEY (supplier_id) REFERENCES suppliers (supplier_id)
);
ALTER TABLE parts AUTO_INCREMENT=1001;

CREATE TABLE IF NOT EXISTS repair_tickets_part (
    ticket_id INT NOT NULL,
    part_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),

    PRIMARY KEY (ticket_id, part_id),
    FOREIGN KEY (ticket_id) REFERENCES repair_tickets(ticket_id),
    FOREIGN KEY (part_id) REFERENCES parts(part_id)
);

DELIMITER $$
CREATE TRIGGER check_ticket_status
    BEFORE INSERT ON repair_tickets_part
    FOR EACH ROW
    BEGIN
        DECLARE ticket_status INT;

        SELECT status INTO ticket_status FROM repair_tickets 
        WHERE ticket_id = NEW.ticket_id;

        IF ticket_status <> 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Cannot assign a new part to this Repair Ticket because its invoice has already been sent.';
        END IF;
    END$$
DELIMITER ;


CREATE TABLE IF NOT EXISTS jobs (
    job_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL UNIQUE,
    date_start DATE NOT NULL,
    date_finish DATE DEFAULT NULL,

    -- CHECK (date_finish >= date_start),

    FOREIGN KEY (ticket_id) REFERENCES repair_tickets(ticket_id)
);
ALTER TABLE jobs AUTO_INCREMENT=10001;

CREATE TABLE IF NOT EXISTS jobs_technicians (
    job_id INT NOT NULL,
    tech_id INT NOT NULL,
    task VARCHAR(255) NOT NULL,
    done BOOLEAN NOT NULL DEFAULT 0,

    PRIMARY KEY (job_id, tech_id),
    FOREIGN KEY (job_id) REFERENCES jobs(job_id),
    FOREIGN KEY (tech_id) REFERENCES technicians(tech_id)
);

CREATE TABLE IF NOT EXISTS payments (
    job_id INT PRIMARY KEY,
    pay_date DATE NOT NULL,
    amount DOUBLE NOT NULL CHECK (amount >= 0),

    FOREIGN KEY (job_id) REFERENCES jobs(job_id)
);

CREATE TABLE IF NOT EXISTS feedbacks (
    job_id INT PRIMARY KEY,
    cust_comment VARCHAR(255) NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    date_submit DATE NOT NULL,

    FOREIGN KEY (job_id) REFERENCES jobs(job_id)
);