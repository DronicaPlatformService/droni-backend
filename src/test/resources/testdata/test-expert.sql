-- Create test users
INSERT INTO users (phone_number, email, name, profile_image, nickname, zone_code, address, address_detail,
                   refresh_token, notification_enabled, marketing_enabled, created_at, updated_at, deleted_at, provider,
                   oauth_id)
VALUES ('1234567890', 'test1@example.com', 'Test User1', 'http://example.com/profile1.jpg', 'Tester1', '10001',
        '123 Test St', 'Apt 1', 'token1', true, false, '2023-01-01 12:00:00', '2023-01-01 12:00:00', NULL, 'GOOGLE',
        'oauth1'),
       ('1234567891', 'test2@example.com', 'Test User2', 'http://example.com/profile2.jpg', 'Tester2', '10002',
        '456 Test St', 'Apt 2', 'token2', true, false, '2023-02-01 12:00:00', '2023-02-01 12:00:00', NULL, 'NAVER',
        'oauth2'),
       ('1234567892', 'test3@example.com', 'Test User3', 'http://example.com/profile3.jpg', 'Tester3', '10003',
        '789 Test St', 'Apt 3', 'token3', true, false, '2023-03-01 12:00:00', '2023-03-01 12:00:00', NULL, 'KAKAO',
        'oauth3');

INSERT INTO business_license (license_id, company_name, representative, company_address, company_address_detail)
VALUES (101, 'DroneOps Inc.', 'John Doe', '123 Business St', 'Suite 101'),
       (102, 'AerialViews Ltd.', 'Jane Smith', '456 Enterprise Ave', 'Floor 2'),
       (103, 'SkyTech Solutions', 'Jim Brown', '789 Corporate Blvd', 'Unit 303');

-- Insert test rows into expert table
INSERT INTO expert (user_id, complete_request, introduction, license_id, created_at, deleted_at)
VALUES ((SELECT user_id FROM users WHERE email = 'test1@example.com'), 0, 'Expert in drone operations and safety.', 101,
        '2023-01-01 12:00:00', NULL),
       ((SELECT user_id FROM users WHERE email = 'test2@example.com'), 0,
        'Specialist in drone photography and videography.', 102, '2023-02-01 12:00:00', NULL),
       ((SELECT user_id FROM users WHERE email = 'test3@example.com'), 0,
        'Experienced in drone maintenance and repair.', 103, '2023-03-01 12:00:00', NULL);