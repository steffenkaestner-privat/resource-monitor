DROP DATABASE ResourceMonitor;

CREATE DATABASE ResourceMonitor;

USE ResourceMonitor;

CREATE TABLE pc ( 
	id BIGINT AUTO_INCREMENT,
	name VARCHAR(128),
	PRIMARY KEY (id)
);

CREATE TABLE cpu (
	id BIGINT(20) AUTO_INCREMENT,
	frequency FLOAT(4,2),
	cores INT(11),
	pc BIGINT(20), 
	PRIMARY KEY (id), 
	FOREIGN KEY (pc) REFERENCES pc(id) 
);

CREATE TABLE ram (
	id BIGINT AUTO_INCREMENT,
	maxRam FLOAT(5,2),
	pc BIGINT,
	PRIMARY KEY (id),
	FOREIGN KEY (pc) REFERENCES pc(id)
);

CREATE TABLE drive (
	id BIGINT AUTO_INCREMENT,
	name VARCHAR(128),
	driveLetter CHAR(1),
	type VARCHAR(128),
	maxSpace FLOAT(7,2),
	pc BIGINT,
	PRIMARY KEY (id),
	FOREIGN KEY (pc) REFERENCES pc(id)
);

CREATE TABLE workload (
	id BIGINT AUTO_INCREMENT,
	workload FLOAT(4,2),
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

CREATE TABLE workloadCPU (
	workload BIGINT,
	cpu BIGINT,
	FOREIGN KEY (workload) REFERENCES workload(id),
	FOREIGN KEY (cpu) REFERENCES cpu(id),
	PRIMARY KEY (workload, cpu)
); 
CREATE TABLE workloadRAM (
	workload BIGINT,
	ram BIGINT,
	FOREIGN KEY (workload) REFERENCES workload(id),
	FOREIGN KEY (ram) REFERENCES ram(id),
	PRIMARY KEY (workload, ram)
);
CREATE TABLE workloadDrive (
	workload BIGINT,
	drive BIGINT,
	FOREIGN KEY (workload) REFERENCES workload(id),
	FOREIGN KEY (drive) REFERENCES drive(id),
	PRIMARY KEY (workload, drive)
);  