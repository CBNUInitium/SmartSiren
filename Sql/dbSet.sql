CREATE SCHEMA IF NOT EXISTS SmartSirenSchema;

CREATE TABLE SmartSirenSchema.Reporter(
	UserID INT PRIMARY KEY AUTO_INCREMENT,
    UserName VARCHAR(50),
    LastLocation VARCHAR(50),
    IsDeleted BOOLEAN,
    ConfidenceLevel FLOAT
);

CREATE TABLE SmartSirenSchema.DisasterReport(
	ReportID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(50),
    Location VARCHAR(50),
    Content TEXT,
    Category VARCHAR(50),
    IsReported BOOL,
    ReportTime DATETIME,
    UserID INT,
    FOREIGN KEY (UserID) REFERENCES SmartSirenSchema.Reporter(UserID)
);

CREATE TABLE SmartSirenSchema.Disaster(
	DisasterID INT PRIMARY KEY AUTO_INCREMENT,
    DisasterName VARCHAR(50),
	Confidence FLOAT,
    Location VARCHAR(50),
    InProgress INT,
	Category VARCHAR(50),
    IsActivate BOOLEAN,
    DisasterTime DATETIME,
    ReportID INT,
    FOREIGN KEY (ReportID) REFERENCES DisasterReport(ReportID)
);
