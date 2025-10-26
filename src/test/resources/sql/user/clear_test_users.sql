DELETE FROM user_role WHERE user_id IN (SELECT id FROM app_user WHERE email LIKE 'newuser%@example.com');
DELETE FROM app_user WHERE email LIKE 'newuser%@example.com';
