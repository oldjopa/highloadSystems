-- liquibase formatted sql
-- changeset app:001-create-core-tables logicalFilePath:db/changelog/changes/postgres/200925_create_tables.sql splitStatements:false endDelimiter:$$

CREATE TABLE IF NOT EXISTS role (
    name varchar(50) PRIMARY KEY,
    level int NOT NULL,
    description text
);

CREATE TABLE IF NOT EXISTS app_user (
    id bigserial PRIMARY KEY,
    email varchar(320) NOT NULL,
    phone varchar(32),
    hashed_password varchar(255) NOT NULL,
    first_name varchar(100),
    last_name varchar(100)
);

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_user_email') THEN
        ALTER TABLE app_user ADD CONSTRAINT uk_user_email UNIQUE (email);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_user_phone') THEN
        ALTER TABLE app_user ADD CONSTRAINT uk_user_phone UNIQUE (phone);
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS user_role (
    user_id bigint NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    role_name varchar(50) NOT NULL REFERENCES role(name) ON DELETE CASCADE,
    PRIMARY KEY(user_id, role_name)
);

DO $$ BEGIN
    CREATE TYPE engine_type AS ENUM ('PISTON','TURBOPROP','JET','ELECTRIC','OTHER');
EXCEPTION WHEN duplicate_object THEN NULL; END $$;

CREATE TABLE IF NOT EXISTS engine (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL,
    type engine_type NOT NULL,
    power double precision NOT NULL
);

CREATE TABLE IF NOT EXISTS aircraft_equipment (
    id bigserial PRIMARY KEY,
    manufacturer varchar(255) NOT NULL,
    model varchar(255) NOT NULL,
    variant varchar(255),
    description text,
    engine_count int NOT NULL,
    engine_id bigint NOT NULL REFERENCES engine(id),
    max_seats int NOT NULL,
    max_takeoff_weight_kg int NOT NULL,
    range_km int NOT NULL,
    cruise_speed_knots int NOT NULL,
    pressurized boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS feature (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS tech_passport (
    id bigserial PRIMARY KEY,
    flight_hours double precision NOT NULL,
    manufacture_year int NOT NULL,
    empty_weight_kg int,
    fuel_capacity_l int,
    length_m double precision,
    wingspan_m double precision,
    height_m double precision,
    noise_cert varchar(255)
);

CREATE TABLE IF NOT EXISTS tech_passport_feature (
    tech_passport_id bigint NOT NULL REFERENCES tech_passport(id) ON DELETE CASCADE,
    feature_id bigint NOT NULL REFERENCES feature(id) ON DELETE CASCADE,
    PRIMARY KEY(tech_passport_id, feature_id)
);

CREATE TABLE IF NOT EXISTS aircraft (
    id bigserial PRIMARY KEY,
    type_id bigint NOT NULL REFERENCES aircraft_equipment(id),
    tech_passport_id bigint REFERENCES tech_passport(id),
    owner_id bigint NOT NULL REFERENCES app_user(id),
    serial_number varchar(255) NOT NULL,
    registration_number varchar(255),
    listed_price numeric(19,2),
    currency varchar(3)
);

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_aircraft_serial') THEN
        ALTER TABLE aircraft ADD CONSTRAINT uk_aircraft_serial UNIQUE (serial_number);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_aircraft_reg') THEN
        ALTER TABLE aircraft ADD CONSTRAINT uk_aircraft_reg UNIQUE (registration_number);
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS deal_status (
    code varchar(30) PRIMARY KEY,
    name varchar(255) NOT NULL,
    description text,
    order_index int NOT NULL,
    is_terminal boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS deal (
    id bigserial PRIMARY KEY,
    deal_number varchar(255) NOT NULL,
    aircraft_id bigint NOT NULL REFERENCES aircraft(id),
    buyer_id bigint NOT NULL REFERENCES app_user(id),
    seller_id bigint NOT NULL REFERENCES app_user(id),
    status_code varchar(30) NOT NULL REFERENCES deal_status(code),
    is_active boolean NOT NULL DEFAULT true,
    closed_at timestamp with time zone,
    version bigint,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL
);

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname='uk_deal_number') THEN
        ALTER TABLE deal ADD CONSTRAINT uk_deal_number UNIQUE (deal_number);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_deal_status ON deal(status_code);
CREATE INDEX IF NOT EXISTS idx_deal_aircraft ON deal(aircraft_id);

CREATE TABLE IF NOT EXISTS deal_status_history (
    id bigserial PRIMARY KEY,
    deal_id bigint NOT NULL REFERENCES deal(id) ON DELETE CASCADE,
    status_code varchar(30) NOT NULL REFERENCES deal_status(code),
    changed_by bigint NOT NULL REFERENCES app_user(id),
    changed_at timestamp with time zone NOT NULL,
    comment text
);
CREATE INDEX IF NOT EXISTS idx_history_deal ON deal_status_history(deal_id);
CREATE INDEX IF NOT EXISTS idx_history_changed_at ON deal_status_history(changed_at);

-- changeset app:002-seed-statuses
INSERT INTO deal_status(code,name,description,order_index,is_terminal) VALUES
 ('NEW','New','Newly created',10,false),
 ('NEGOTIATION','Negotiation','Negotiation in progress',20,false),
 ('APPROVED','Approved','Approved for completion',30,false),
 ('COMPLETED','Completed','Deal completed successfully',40,true),
 ('CANCELLED','Cancelled','Deal cancelled',50,true)
ON CONFLICT (code) DO NOTHING;
