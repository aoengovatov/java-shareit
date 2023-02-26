CREATE TABLE IF NOT EXISTS users
(
    id    bigint generated by default as identity not null,
    name  varchar(255) not null,
    email varchar(255) not null,
        constraint pk_user primary key (id),
        constraint uq_user_email unique (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id    bigint generated by default as identity not null,
    name  varchar(255),
    description  varchar(255),
    is_available boolean,
    owner_id bigint not null,
        constraint pk_item primary key (id),
        constraint fk_item_on_owner foreign key (owner_id) references users (id)
);

CREATE TABLE IF NOT EXISTS booking
(
    id          bigint generated by default as identity not null,
    start_date  TIMESTAMP WITH TIME ZONE not null,
    end_date    TIMESTAMP WITH TIME ZONE not null,
    item_id     bigint not null,
    booker_id   bigint not null,
    status      varchar(255),
        constraint pk_booking primary key (id),
        constraint fk_item_booking foreign key (item_id) references items (id),
        constraint fk_user_booking foreign key (booker_id) references users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id          bigint generated by default as identity not null,
    text        varchar(1000),
    item_id     bigint not null,
    author      varchar(255),
    created     TIMESTAMP WITH TIME ZONE not null,
        constraint pk_comment primary key (id),
        constraint fk_item_comment foreign key (item_id) references items (id)
);