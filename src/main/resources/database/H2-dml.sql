-- DELETE Demo Data
DELETE FROM account_password_histories
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'admin%');
DELETE FROM account_password_histories
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'guest%');
DELETE FROM account_password_histories
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'user%');

/*
DELETE FROM account_authorities
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'admin%');
DELETE FROM account_authorities
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'guest%');
DELETE FROM account_authorities
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'user%');
*/
DELETE FROM account WHERE account_id LIKE 'admin%';
DELETE FROM account WHERE account_id LIKE 'guest%';
DELETE FROM account WHERE account_id LIKE 'user%';

DELETE FROM break_time WHERE work_place_uuid LIKE '00000000-0000-0000-0000-00000000000%';
DELETE FROM work_place WHERE work_place_uuid LIKE '00000000-0000-0000-0000-00000000000%';

DELETE FROM fixed_holiday;
DELETE FROM happy_monday_holiday;
DELETE FROM seasonal_holiday;

-- INSERT Demo Data
-- ADMIN USER
INSERT INTO account
    VALUES('f87ddd5e-f3ec-11e3-9125-b2227cce2b54','admin01','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP(),'Administrator01','',true);
INSERT INTO account_authorities
    VALUES('f87ddd5e-f3ec-11e3-9125-b2227cce2b54','ADMIN');
INSERT INTO account_authorities
    VALUES('f87ddd5e-f3ec-11e3-9125-b2227cce2b54','USER');
INSERT INTO account_authorities
    VALUES('f87ddd5e-f3ec-11e3-9125-b2227cce2b54','EMPLOYEE');
INSERT INTO account_password_histories
    VALUES('f87ddd5e-f3ec-11e3-9125-b2227cce2b54','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP());

INSERT INTO account
    VALUES('f88a7cf8-f3ec-11e3-9125-b2227cce2b54','admin02','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP(),'Administrator02','',false);
INSERT INTO account_authorities
    VALUES('f88a7cf8-f3ec-11e3-9125-b2227cce2b54','ADMIN');
INSERT INTO account_authorities
    VALUES('f88a7cf8-f3ec-11e3-9125-b2227cce2b54','EMPLOYEE');
INSERT INTO account_password_histories
    VALUES('f88a7cf8-f3ec-11e3-9125-b2227cce2b54','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP());

-- GUEST USER
INSERT INTO account
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b54','guest01','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP(),'Guset01','',true);
INSERT INTO account_authorities
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b54','GUEST');
INSERT INTO account_authorities
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b54','EMPLOYEE');
INSERT INTO account_password_histories
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b54','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP());

INSERT INTO account
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b55','guest02','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP(),'Guset02','',true);
INSERT INTO account_authorities
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b55','GUEST');
INSERT INTO account_authorities
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b55','EMPLOYEE');
INSERT INTO account_password_histories
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b55','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP());

INSERT INTO account
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b56','guest03','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP(),'Guset03','',true);
INSERT INTO account_authorities
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b56','GUEST');
INSERT INTO account_authorities
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b56','EMPLOYEE');
INSERT INTO account_password_histories
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b56','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP());

-- ACCOUNTS MANAGER USER
INSERT INTO account
    VALUES('b655e0e6-f457-11e3-9c20-b2227cce2b54','user01','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP(),'User01','',true);
INSERT INTO account_authorities
    VALUES('b655e0e6-f457-11e3-9c20-b2227cce2b54','USER');
INSERT INTO account_authorities
    VALUES('b655e0e6-f457-11e3-9c20-b2227cce2b54','ACCOUNTMNG');
INSERT INTO account_authorities
    VALUES('b655e0e6-f457-11e3-9c20-b2227cce2b54','EMPLOYEE');
INSERT INTO account_password_histories
    VALUES('b655e0e6-f457-11e3-9c20-b2227cce2b54','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP());

-- WORK PLACE SETTINGS
INSERT INTO work_place VALUES('00000000-0000-0000-0000-000000000000','Main Office','本社','09:00:00','17:45:00','00:30:00',null,null,0);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000000','00:00:00','01:00:00',null,null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000000','07:30:00','09:00:00',null,null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000000','12:00:00','13:00:00',null,null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000000','17:45:00','18:15:00',null,null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000000','21:45:00','22:00:00',null,null);

INSERT INTO work_place VALUES('00000000-0000-0000-0000-000000000001','Office A','勤務先Ａ','10:00:00','18:30:00','00:30:00',null,null,1);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000001','00:00:00','01:00:00',null,null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000001','07:30:00','09:00:00',null,null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000001','12:00:00','13:00:00',null,null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000001','18:45:00','19:15:00',null,null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000001','21:45:00','22:00:00',null,null);

INSERT INTO fixed_holiday VALUES( 1,  1, 'New Year''s Day ', '元日', null, 1948, 9999);
INSERT INTO fixed_holiday VALUES( 1,  2, 'Year-start Holiday', '年始休み', null, 0, 9999);
INSERT INTO fixed_holiday VALUES( 1,  3, 'Year-start Holiday', '年始休み', null, 0, 9999);
INSERT INTO fixed_holiday VALUES( 2, 11, 'Foundation Day', '建国記念の日', 1, 1967, 9999);
INSERT INTO fixed_holiday VALUES( 4, 29, 'Shōwa Day', '昭和の日', 1, 2007, 9999);
INSERT INTO fixed_holiday VALUES( 5,  3, 'Constitution Memorial Day', '憲法記念日', 3, 1948, 9999);
INSERT INTO fixed_holiday VALUES( 5,  4, 'Greenery Day', 'みどりの日 ', 2, 2007, 9999);
INSERT INTO fixed_holiday VALUES( 5,  5, 'Children''s Day', 'こどもの日', 1, 1948, 9999);
INSERT INTO fixed_holiday VALUES( 8, 11, 'Marine Day', '山の日', 1, 2016, 9999);
INSERT INTO fixed_holiday VALUES(11,  3, 'Culture Day', '文化の日', 1, 1948, 9999);
INSERT INTO fixed_holiday VALUES(11, 23, 'Labour Thanksgiving Day', '勤労感謝の日', 1, 1948, 9999);
INSERT INTO fixed_holiday VALUES(12, 23, 'The Emperor''s Birthday', '天皇誕生日', 1, 1990, 9999);
INSERT INTO fixed_holiday VALUES(12, 30, 'Year-end Holiday', '年末休み', null, 0, 9999);
INSERT INTO fixed_holiday VALUES(12, 31, 'Year-end Holiday', '年末休み', null, 0, 9999);

INSERT INTO happy_monday_holiday VALUES( 1, 2, 'Coming of Age Day', '成人の日', 2000, 9999);
INSERT INTO happy_monday_holiday VALUES( 7, 3, 'Marine Day', '海の日', 2003, 9999);
INSERT INTO happy_monday_holiday VALUES( 9, 3, 'Respect for the Aged Day', '敬老の日', 2003, 9999);
INSERT INTO happy_monday_holiday VALUES(10, 2, 'Health and Sports Day', '体育の日', 2000, 9999);

INSERT INTO seasonal_holiday VALUES( '2014-09-23', 'Autumnal Equinox Day', '秋分の日');
INSERT INTO seasonal_holiday VALUES( '2015-03-21', 'Vernal Equinox Day', '春分の日');

COMMIT;
