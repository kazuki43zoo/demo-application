DELETE FROM account_password_histories
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'admin%');
DELETE FROM account_password_histories
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'guest%');
DELETE FROM account_password_histories
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'user%');

DELETE FROM account_authorities
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'admin%');
DELETE FROM account_authorities
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'guest%');
DELETE FROM account_authorities
    WHERE account_uuid in (SELECT account_uuid FROM account WHERE account_id like 'user%');

DELETE FROM account WHERE account_id LIKE 'admin%';
DELETE FROM account WHERE account_id LIKE 'guest%';
DELETE FROM account WHERE account_id LIKE 'user%';

DELETE FROM break_time WHERE work_place_uuid = '00000000-0000-0000-0000-000000000000';
DELETE FROM work_place WHERE work_place_uuid = '00000000-0000-0000-0000-000000000000';

DELETE FROM break_time WHERE work_place_uuid = '00000000-0000-0000-0000-000000000001';
DELETE FROM work_place WHERE work_place_uuid = '00000000-0000-0000-0000-000000000001';

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

INSERT INTO account
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b54','guest01','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP(),'Guset01','',true);
INSERT INTO account_authorities
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b54','GUEST');
INSERT INTO account_authorities
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b54','EMPLOYEE');
INSERT INTO account_password_histories
    VALUES('f88a8086-f3ec-11e3-9125-b2227cce2b54','$2a$10$MPLzsWbMR4SEHpCwrq1oFOAJzFFJos1e2H5ffcY5vNKyKs6IMWBkG',CURRENT_TIMESTAMP());

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

INSERT INTO work_place VALUES('00000000-0000-0000-0000-000000000000','本社','09:00:00','17:45:00','00:30:00',0);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000000','00:00:00','01:00:00',null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000000','07:30:00','09:00:00',null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000000','12:00:00','13:00:00',null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000000','17:45:00','18:15:00',null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000000','21:45:00','22:00:00',null);

INSERT INTO work_place VALUES('00000000-0000-0000-0000-000000000001','勤務先Ａ','10:00:00','18:30:00','00:30:00',1);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000001','00:00:00','01:00:00',null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000001','07:30:00','09:00:00',null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000001','12:00:00','13:00:00',null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000001','18:45:00','19:15:00',null);
INSERT INTO break_time VALUES('00000000-0000-0000-0000-000000000001','21:45:00','22:00:00',null);

COMMIT;
