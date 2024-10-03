INSERT INTO customers (full_name, email, phone_no, address, zip) VALUES
    ('Guy Montag', 'guymontag@salamander.com', 3058917290, '123 Main St, New York', 12345),
    ('Winston Smith', 'smithwinston@bb.com', 2425155029, '456 Oak Ave, Airstrip One', 54321),
    ('Evey Hammond', 'eveyhammond@email.com', 9227571255, '789 Pine Rd, Shooters Hill', 67890);

INSERT INTO technicians (full_name, salary) VALUES
    ('Anthony Stark', 99000.00),
    ('Peter Parker', 35000.00),
    ('Reed Richards', 60000.00),
    ('Bruce Wayne', 125000.00);

INSERT INTO suppliers (full_name, email, phone_no, address) VALUES
    ('Tech Supplies Co', 'info@techsupplies.com', 138685311, '789 Industrial Blvd, Tech City'),
    ('PC Parts Inc.', 'sales@pcparts.com', 986513168, '321 Commercial Dr, Partsville'),
    ('Gadget Corp', 'support@gadgetcorp.com', 8468468461, '456 Silicon St, Innoville');

INSERT INTO parts (supplier_id, name, description, value, stock) VALUES
    (101, 'SSD 500GB', 'Solid State Drive 500GB', 100.00, 50),
    (102, '8GB RAM', '8GB DDR4 RAM', 40.00, 100),
    (103, 'Power Supply 600W', '600W Power Supply', 75.00, 30),
    (101, 'Graphics Card GTX 1660', 'NVIDIA GTX 1660', 250.00, 20),
    (103, 'Motherboard ATX', 'ATX form factor motherboard', 120.00, 15);
