CREATE TABLE IF NOT EXISTS doctors (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  middle_name VARCHAR(100) NOT NULL,
  specialty VARCHAR(100) NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS offices (
  id BIGSERIAL PRIMARY KEY,
  number INTEGER NOT NULL,
  floor INTEGER NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS appointments (
  id BIGSERIAL PRIMARY KEY,
  doctor_id BIGINT NOT NULL,
  office_id BIGINT NOT NULL,
  patient_name VARCHAR(150) NOT NULL,
  appointment_time TIMESTAMP NOT NULL,
  status VARCHAR(50) DEFAULT 'PENDING',
  is_deleted BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
  CONSTRAINT fk_office FOREIGN KEY (office_id) REFERENCES offices(id)
);

-- DOCTORS
-- DOCTORS
INSERT INTO doctors (first_name, last_name, middle_name, specialty, is_deleted)
VALUES ('Gregory', 'House', 'John', 'Diagnóstico', false);

INSERT INTO doctors ( first_name, last_name, middle_name, specialty, is_deleted)
VALUES ( 'Meredith', 'Grey', 'Marie', 'Cirugía General', false);

INSERT INTO doctors ( first_name, last_name, middle_name, specialty, is_deleted)
VALUES ('Stephen', 'Strange', 'Vincent', 'Neurocirugía', false);

-- OFFICES
INSERT INTO offices (number, floor, is_deleted)
VALUES ( 101, 1, false);

INSERT INTO offices ( number, floor, is_deleted)
VALUES ( 202, 2, false);

INSERT INTO offices ( number, floor, is_deleted)
VALUES ( 303, 3, false);

-- APPOINTMENTS
INSERT INTO appointments ( doctor_id, office_id, patient_name, appointment_time, status, is_deleted)
VALUES ( 1, 1, 'Juan Pérez', '2025-05-10T08:00:00', 'PENDING', false);

INSERT INTO appointments ( doctor_id, office_id, patient_name, appointment_time, status, is_deleted)
VALUES ( 2, 2, 'Ana Gómez', '2025-05-10T09:00:00', 'PENDING', false);

INSERT INTO appointments ( doctor_id, office_id, patient_name, appointment_time, status, is_deleted)
VALUES ( 3, 3, 'Luis Ramírez', '2025-05-10T10:00:00', 'PENDING', false);