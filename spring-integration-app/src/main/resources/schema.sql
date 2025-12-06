-- Create the sequence first
CREATE SEQUENCE IF NOT EXISTS department_seq START WITH 1 INCREMENT BY 1;


CREATE TABLE IF NOT EXISTS DEPARTMENT (
    department_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL,
    department_address VARCHAR(255),
    department_code VARCHAR(100),
    status VARCHAR(50),
    code VARCHAR(50),
    retry INT
);
