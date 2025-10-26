-- liquibase formatted sql
-- changeset app:004-align-sequences-context runAlways:true
-- Align sequences after initial seed (id values inserted explicitly in previous changeset)
SELECT setval('app_user_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM app_user), false);
SELECT setval('engine_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM engine), false);
SELECT setval('aircraft_equipment_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM aircraft_equipment), false);
SELECT setval('feature_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM feature), false);
SELECT setval('tech_passport_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM tech_passport), false);
SELECT setval('aircraft_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM aircraft), false);
SELECT setval('deal_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM deal), false);
SELECT setval('deal_status_history_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM deal_status_history), false);

