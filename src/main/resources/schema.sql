CREATE TABLE IF NOT EXISTS doctors (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  middle_name VARCHAR(100) NOT NULL,
  specialty VARCHAR(100) NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS offices (
  id SERIAL PRIMARY KEY,
  number INTEGER NOT NULL,
  floor INTEGER NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS appointments (
  id SERIAL PRIMARY KEY,
  doctor_id BIGINT NOT NULL,
  office_id BIGINT NOT NULL,
  patient_name VARCHAR(150) NOT NULL,
  appointment_time TIMESTAMP NOT NULL,
  status VARCHAR(50) DEFAULT 'PENDING',
  is_deleted BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
  CONSTRAINT fk_office FOREIGN KEY (office_id) REFERENCES offices(id)
);
