INSERT INTO customers (full_name, email, phone_no, address, zip) VALUES
    ('John Doe', 'john.doe@example.com', 1234567890, '123 Main St, Springfield', 12345),
    ('Jane Smith', 'jane.smith@example.com', 9876543210, '456 Oak Ave, Metropolis', 54321),
    ('Alice Johnson', 'alice.j@example.com', 5556667777, '789 Pine Rd, Gotham', 67890);

INSERT INTO computers (owner_id, brand, model, sn) VALUES
    (1, 'Dell', 'XPS 13', 'SN12345'),
    (2, 'Apple', 'MacBook Pro', 'SN67890'),
    (3, 'HP', 'Pavilion', 'SN11223');

INSERT INTO technicians (full_name, salary) VALUES
    ('Michael Brown', 50000.00),
    ('Linda White', 55000.00),
    ('Kevin Green', 60000.00);

INSERT INTO suppliers (full_name, email, phone_no, address) VALUES
    ('Tech Supplies Co', 'info@techsupplies.com', 1231231234, '789 Industrial Blvd, Tech City'),
    ('PC Parts Inc.', 'sales@pcparts.com', 9879879876, '321 Commercial Dr, Partsville'),
    ('Gadget Corp', 'support@gadgetcorp.com', 4564564567, '456 Silicon St, Innoville');

INSERT INTO parts (supplier_id, name, description, value, stock) VALUES
    (1, 'SSD 500GB', 'Solid State Drive 500GB', 100.00, 50),
    (2, '8GB RAM', '8GB DDR4 RAM', 40.00, 100),
    (3, 'Power Supply 600W', '600W Power Supply', 75.00, 30),
    (1, 'Graphics Card GTX 1660', 'NVIDIA GTX 1660', 250.00, 20),
    (3, 'Motherboard ATX', 'ATX form factor motherboard', 120.00, 15);
