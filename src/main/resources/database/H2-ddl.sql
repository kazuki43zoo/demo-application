--DROP Tables for time-card
--DROP TABLE daily_attendance;
--DROP TABLE time_card;
--DROP TABLE break_time;
--DROP TABLE work_place;

-- DROP Tables for account
DROP TABLE account_authentication_histories;
DROP TABLE account_password_lock;
DROP TABLE account_password_histories;
DROP TABLE account_authorities;
DROP TABLE account;

-- CREATE Tables for account
CREATE TABLE IF NOT EXISTS account(
    account_uuid nvarchar(36),
    account_id nvarchar(128) not null,
    password char(60) not null,
    password_modified_at timestamp not null,
    first_name nvarchar(128) not null,
    last_name nvarchar(128),
    enabled boolean not null,
    constraint pk_account primary key (account_uuid),
    constraint uk_account_1 unique key (account_id)
);
CREATE TABLE IF NOT EXISTS account_authorities(
    account_uuid nvarchar(36),
    authority nvarchar(32),
    constraint pk_account_authorities primary key (account_uuid,authority)
);
CREATE TABLE IF NOT EXISTS account_password_histories(
    account_uuid nvarchar(36),
    password char(60),
    created_at timestamp not null,
    constraint pk_account_password_histories primary key (account_uuid,password)
);
CREATE TABLE IF NOT EXISTS account_password_lock (
    account_uuid nvarchar(36),
    failure_count smallint not null,
    modified_at timestamp not null,
    constraint pk_account_password_lock primary key (account_uuid)
);
CREATE TABLE IF NOT EXISTS account_authentication_histories(
    account_uuid nvarchar(36),
    created_at timestamp,
    authentication_type varchar(64) not null,
    authentication_result boolean not null,
    failure_reason nvarchar(256),
    remote_address varchar(39),
    session_id varchar(128),
    agent varchar(256),
    tracking_id varchar(32),
    constraint pk_account_authentication_histories primary key (account_uuid,created_at)
);

-- CREATE Tables for time-card
CREATE TABLE IF NOT EXISTS work_place(
    work_place_uuid nvarchar(36),
    work_place_name nvarchar(256) not null,
    work_place_name_ja nvarchar(256) not null,
    base_begin_time time not null,
    base_finish_time time not null,
    unit_time time not null,
    note nvarchar(512),
    note_ja nvarchar(512),
    display_order int,
    constraint pk_work_place primary key (work_place_uuid)
);
CREATE TABLE IF NOT EXISTS break_time(
    work_place_uuid nvarchar(36),
    begin_time time,
    finish_time time not null,
    note nvarchar(512),
    note_ja nvarchar(512),
    constraint pk_break_time primary key (work_place_uuid, begin_time)
);
CREATE TABLE IF NOT EXISTS time_card(
    account_uuid nvarchar(36),
    target_month date,
    work_place_uuid nvarchar(36),
    note nvarchar(512),
    constraint pk_time_card primary key (account_uuid, target_month)
);
CREATE TABLE IF NOT EXISTS daily_attendance(
    account_uuid nvarchar(36),
    target_date date,
    begin_time time,
    finish_time time,
    paid_leave boolean,
    special_work_code nvarchar(2),
    note nvarchar(512),
    work_place_uuid nvarchar(36),
    constraint pk_daily_attendance primary key (account_uuid, target_date)
);

COMMIT;