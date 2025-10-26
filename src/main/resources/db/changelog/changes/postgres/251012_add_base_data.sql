-- liquibase formatted sql
-- changeset app:003-seed-base-data

-- ROLES
INSERT INTO role (name, level, description)
VALUES ('ADMIN', 100, 'Administrator with full access'),
       ('USER', 10, 'Regular user')
ON CONFLICT (name) DO NOTHING;

-- USERS
INSERT INTO app_user (id, email, phone, hashed_password, first_name, last_name)
VALUES (1, 'admin@example.com', '+10000000001', 'hashed_admin', 'Admin', 'User'),
       (2, 'buyer@example.com', '+10000000002', 'hashed_buyer', 'Buyer', 'User'),
       (3, 'seller@example.com', '+10000000003', 'hashed_seller', 'Seller', 'User')
ON CONFLICT (id) DO NOTHING;

-- USER ROLES
INSERT INTO user_role (user_id, role_name)
VALUES (1, 'ADMIN'),
       (2, 'USER'),
       (3, 'USER')
ON CONFLICT (user_id, role_name) DO NOTHING;

-- ENGINES
INSERT INTO engine (id, name, type, power)
VALUES (1, 'Pratt & Whitney PT6A', 'TURBOPROP', 750.0),
       (2, 'Lycoming IO-540', 'PISTON', 300.0)
ON CONFLICT (id) DO NOTHING;

-- AIRCRAFT EQUIPMENT
INSERT INTO aircraft_equipment (id, manufacturer, model, variant, description, engine_count, engine_id,
                                max_seats, max_takeoff_weight_kg, range_km, cruise_speed_knots, pressurized)
VALUES (1, 'Cessna', '208B Grand Caravan', 'EX', 'Turboprop utility aircraft', 1, 1, 14, 3970, 1200, 186, true),
       (2, 'Piper', 'PA-32R', 'Lance II', 'Single-engine piston', 1, 2, 6, 1600, 1500, 155, false)
ON CONFLICT (id) DO NOTHING;

-- FEATURES
INSERT INTO feature (id, name)
VALUES (1, 'Autopilot'),
       (2, 'De-icing system'),
       (3, 'Glass cockpit')
ON CONFLICT (id) DO NOTHING;

-- TECH PASSPORTS
INSERT INTO tech_passport (id, flight_hours, manufacture_year, empty_weight_kg, fuel_capacity_l,
                           length_m, wingspan_m, height_m, noise_cert, created_at, updated_at)
VALUES (1, 1500.0, 2015, 2200, 1300, 12.7, 15.9, 4.6, 'Stage 3', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- TECH PASSPORT FEATURES
INSERT INTO tech_passport_feature (tech_passport_id, feature_id)
VALUES (1, 1),
       (1, 2),
       (1, 3)
ON CONFLICT (tech_passport_id, feature_id) DO NOTHING;

-- AIRCRAFT
INSERT INTO aircraft (type_id, tech_passport_id, owner_id, serial_number, registration_number,
                      listed_price, currency)
VALUES (1, 1, 3, 'SN-123', 'RA-12345', 1500000.00, 'USD')
ON CONFLICT (id) DO NOTHING;

-- DEALS
INSERT INTO deal (id, deal_number, aircraft_id, buyer_id, seller_id, status_code,
                  is_active, closed_at, version, created_at, updated_at)
VALUES (1, 'D-001', 1, 2, 3, 'NEW', true, NULL, 1, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- DEAL STATUS HISTORY
INSERT INTO deal_status_history (id, deal_id, status_code, changed_by, changed_at, comment)
VALUES (1, 1, 'NEW', 1, NOW(), 'Initial creation')
ON CONFLICT (id) DO NOTHING;
