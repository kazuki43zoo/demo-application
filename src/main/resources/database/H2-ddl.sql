DROP TABLE account_authentication_histories;
DROP TABLE account_password_lock;
DROP TABLE account_password_histories;
DROP TABLE account_authorities;
DROP TABLE account;

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
    remote_address varchar(39) not null,
    session_id varchar(128),
    agent varchar(256),
    tracking_id varchar(32),
    constraint pk_account_credential_history primary key (account_uuid,created_at)
);

COMMIT;