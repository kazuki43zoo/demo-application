DROP TABLE account_authorities;
DROP TABLE account;

CREATE TABLE IF NOT EXISTS account(
    account_uuid nvarchar(36),
    account_id nvarchar(128) not null,
    password nvarchar(60) not null,
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


COMMIT;