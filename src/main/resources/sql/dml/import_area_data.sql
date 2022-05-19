use happyhouse;

LOAD DATA LOCAL INFILE '{프로젝트 경로}/06_Happy_House_Algo/src/main/resources/data/sido.txt' 
INTO TABLE Sido CHARACTER SET utf8
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n';

select * from Sido;

----------------------------------------------------------------

LOAD DATA LOCAL INFILE '{프로젝트 경로}/06_Happy_House_Algo/src/main/resources/data/sigugun.txt' 
INTO TABLE Sigugun CHARACTER SET utf8
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n';

select * from Sigugun;

----------------------------------------------------------------

LOAD DATA LOCAL INFILE '{프로젝트 경로}/06_Happy_House_Algo/src/main/resources/data/upmyundong.txt' 
INTO TABLE Upmyundong CHARACTER SET utf8
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n';

select * from Upmyundong;

----------------------------------------------------------------
