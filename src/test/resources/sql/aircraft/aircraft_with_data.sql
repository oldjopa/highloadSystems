INSERT INTO app_user (id, email, phone, hashed_password, first_name, last_name)
VALUES (100, 'owner@test.com', '123456', 'hash', 'Owner', 'Test')
ON CONFLICT DO NOTHING;

INSERT INTO engine (id, name, type, power)
VALUES (100, 'TurboFan', 'JET', 1200)
ON CONFLICT DO NOTHING;

INSERT INTO aircraft_equipment (id, manufacturer, model, variant, description, engine_count, engine_id,
                                max_seats, max_takeoff_weight_kg, range_km, cruise_speed_knots, pressurized)
VALUES (100, 'Boeing', '737', 'MAX', 'Integration type', 2, 100, 180, 80000, 6000, 850, true)
ON CONFLICT DO NOTHING;

INSERT INTO tech_passport (id, flight_hours, manufacture_year, empty_weight_kg, fuel_capacity_l,
                           length_m, wingspan_m, height_m, noise_cert, created_at, updated_at)
VALUES (100, 0, 2020, 41000, 20000, 39.5, 35.8, 12.5, 'Stage4', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO aircraft (id, type_id, tech_passport_id, owner_id, serial_number, registration_number, listed_price, currency)
VALUES (10, 100, 100, 100, 'SN-001', 'RA-001', 50000000, 'USD')
ON CONFLICT DO NOTHING;