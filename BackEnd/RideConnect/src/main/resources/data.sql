INSERT INTO app_user (name, email, password) VALUES
('Aarav Sharma', 'aarav.sharma@example.com', 'password123'),
('Diya Patel', 'diya.patel@example.com', 'password123'),
('Ishaan Gupta', 'ishaan.gupta@example.com', 'password123'),
('Meera Iyer', 'meera.iyer@example.com', 'password123'),
('Karan Singh', 'karan.singh@example.com', 'password123'),
('Priya Deshmukh', 'priya.deshmukh@example.com', 'password123'),
('Rohan Joshi', 'rohan.joshi@example.com', 'password123'),
('Ananya Reddy', 'ananya.reddy@example.com', 'password123'),
('Sahil Kumar', 'sahil.kumar@example.com', 'password123'),
('Sneha Nair', 'sneha.nair@example.com', 'password123');


INSERT INTO driver ( user_id, rating, available, current_location) VALUES
( 1, 4.5, true, ST_GeomFromText('POINT(73.9282 18.5085)', 4326)), -- Hadapsar
( 2, 4.2, true, ST_GeomFromText('POINT(73.9795 18.4964)', 4326)), -- Loni Kalbhor
( 3, 4.8, true, ST_GeomFromText('POINT(73.9257 18.5126)', 4326)), -- Magarpatta
( 4, 4.1, true, ST_GeomFromText('POINT(73.9567 18.5514)', 4326)), -- Kharadi
( 5, 3.9, true, ST_GeomFromText('POINT(73.8736 18.5167)', 4326)), -- Camp
( 6, 4.0, true, ST_GeomFromText('POINT(73.8530 18.5301)', 4326)), -- Shivajinagar
( 7, 4.7, true, ST_GeomFromText('POINT(73.9274 18.5018)', 4326)), -- Hadapsar
( 8, 4.3, true, ST_GeomFromText('POINT(73.9257 18.5137)', 4326)), -- Magarpatta
( 9, 4.4, true, ST_GeomFromText('POINT(73.8736 18.5171)', 4326)), -- Camp
( 10, 4.6, true, ST_GeomFromText('POINT(73.9565 18.5518)', 4326)); -- Kharadi


INSERT INTO user_roles (user_id, roles) VALUES
(1, 'RIDER'),
(2, 'RIDER'),
(2, 'DRIVER'),
(3, 'RIDER'),
(3, 'DRIVER'),
(4, 'RIDER'),
(4, 'DRIVER'),
(5, 'RIDER'),
(5, 'DRIVER'),
(6, 'RIDER');



INSERT INTO rider ( user_id, rating) VALUES
( 1, 4.9);

INSERT INTO wallet ( user_id,balance) VALUES
(1,100),
(2,500);
