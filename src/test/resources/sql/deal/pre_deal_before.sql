-- Пользователи
INSERT INTO app_user (id, email, phone, hashed_password, first_name, last_name)
VALUES (200, 'buyer@test.com', '111', 'hash', 'Buyer', 'User'),
       (201, 'seller@test.com', '222', 'hash', 'Seller', 'User')
ON CONFLICT (id) DO NOTHING;

-- Самолёт
INSERT INTO engine (id, name, type, power)
VALUES (200, 'TurboJet', 'JET', 1400)
ON CONFLICT (id) DO NOTHING;

INSERT INTO aircraft_equipment (id, manufacturer, model, variant, description, engine_count, engine_id,
                                max_seats, max_takeoff_weight_kg, range_km, cruise_speed_knots, pressurized)
VALUES (200, 'Airbus', 'A320', 'NEO', 'Deal test type', 2, 200, 180, 75000, 6000, 830, true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO tech_passport (id, flight_hours, manufacture_year, empty_weight_kg, fuel_capacity_l,
                           length_m, wingspan_m, height_m, noise_cert, created_at, updated_at)
VALUES (200, 1000, 2021, 42000, 19000, 37.5, 35.8, 12.0, 'Stage4', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO aircraft (id, type_id, tech_passport_id, owner_id, serial_number, registration_number, listed_price, currency)
VALUES (200, 200, 200, 201, 'SN-DEAL-001', 'RA-DEAL-001', 1000000, 'USD')
ON CONFLICT (id) DO NOTHING;

INSERT INTO deal (id, deal_number, buyer_id, seller_id, aircraft_id, status_code, created_at, updated_at)
VALUES (1, 1488, 200, 201, 200, 'NEW', now(), now())
ON CONFLICT (id) DO NOTHING;

-- Align sequences after inserts
SELECT setval('app_user_id_seq', (SELECT COALESCE(MAX(id),0)+1 FROM app_user), false);
SELECT setval('engine_id_seq', (SELECT COALESCE(MAX(id),0)+1 FROM engine), false);
SELECT setval('aircraft_equipment_id_seq', (SELECT COALESCE(MAX(id),0)+1 FROM aircraft_equipment), false);
SELECT setval('tech_passport_id_seq', (SELECT COALESCE(MAX(id),0)+1 FROM tech_passport), false);
SELECT setval('aircraft_id_seq', (SELECT COALESCE(MAX(id),0)+1 FROM aircraft), false);
SELECT setval('deal_id_seq', (SELECT COALESCE(MAX(id),0)+1 FROM deal), false);
