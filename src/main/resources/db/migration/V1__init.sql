create table users
(
    id  IDENTITY     NOT NULL PRIMARY KEY,
    username NVARCHAR(255) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL
);
create TABLE Account
(
    id IDENTITY NOT NULL PRIMARY KEY,
    account_full_id VARCHAR(255),
    account_type VARCHAR(255) NOT NULL,
    bank_id INTEGER NOT NULL,
    client_id BIGINT NOT NULL,
    balance DOUBLE PRECISION NOT NULL,
    withdraw_allowed BIT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES users (id)
);

CREATE TABLE Transaction
(
    id INTEGER AUTO_INCREMENT NOT NULL,
    date VARCHAR(255) NOT NULL,
    account_number VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    type_of_operation VARCHAR(255) NOT NULL,
    client_id BIGINT NOT NULL,
    CONSTRAINT PK_Transaction PRIMARY KEY (id)
);

create table roles
(
    id   INTEGER     NOT NULL PRIMARY KEY,
    name VARCHAR(80) not null
);
insert into roles(id, name)
values (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');


create table users_roles
(
    user_id bigint not null,
    role_id integer not null,
    primary key (user_id, role_id),
    foreign key (user_id) references users (id),
    foreign key (role_id) references roles (id)

);