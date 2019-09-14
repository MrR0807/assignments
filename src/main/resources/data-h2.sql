INSERT INTO bank_user (email, password) VALUES
('test@test.com', '$2a$04$ShL0W16R0.bKg1r/Ku.JKOJb3T8gzeWtY4BlNIZ2Gi15Qe15NzvLa'),
('test2@test.com', '$2a$04$ShL0W16R0.bKg1r/Ku.JKOJb3T8gzeWtY4BlNIZ2Gi15Qe15NzvLa'),
('test3@test.com', '$2a$04$ShL0W16R0.bKg1r/Ku.JKOJb3T8gzeWtY4BlNIZ2Gi15Qe15NzvLa');

INSERT INTO account (email, balance) VALUES
('test@test.com', 100),
('test2@test.com', 200),
('test3@test.com', 300);

INSERT INTO account_record (created, action, amount, email) VALUES
('2019-09-14 12:00:00', 'WITHDRAW', '100', 'test@test.com'),
('2019-09-14 12:00:00', 'DEPOSIT', '100', 'test@test.com');