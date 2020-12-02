create table message(
    id bigint auto_increment primary key,
    mfrom varchar(15) not null,
    to varchar(15) not null,
    body varchar(500)
);

create table message_status (
    messageId bigint primary key,
    status varchar(10) not null,
    constraint fk_message_message_status foreign key(id) references message(id)
);

