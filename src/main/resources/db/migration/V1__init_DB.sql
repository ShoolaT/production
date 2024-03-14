create sequence hibernate_sequence start 1 increment 1;

create table employee
(
    id           bigserial not null,
    full_name    varchar(255),
    position_id  bigint,
    salary       float     not null,
    address      varchar(255),
    phone_number varchar(255),
    primary key (id)
);
create table finished_product
(
    id                     bigserial not null,
    name                   varchar(255),
    unit_of_measurement_id bigint,
    quantity               float(53) not null,
    amount                 float(53) not null,
    primary key (id)
);
create table ingredient
(
    id              bigserial not null,
    product_id      bigint,
    raw_material_id bigint,
    quantity        float(53) not null,
    primary key (id)
);
create table product_production
(
    id          bigserial not null,
    employee_id bigint,
    product_id  bigint,
    date        DATE,
    quantity    float(53) not null,
    primary key (id)
);
create table position
(
    id   bigserial not null,
    name varchar(255),
    primary key (id)
);
create table product_sale
(
    id          bigserial not null,
    employee_id bigint,
    product_id  bigint,
    quantity    float(53) not null,
    cost      float(53) not null,
    date        DATE,
    primary key (id)
);
create table raw_material
(
    id                     bigserial not null,
    name                   varchar(255),
    unit_of_measurement_id bigint,
    quantity               float(53) not null,
    amount                 float(53) not null,
    primary key (id)
);
create table raw_material_purchase
(
    id              bigserial not null,
    employee_id     bigint,
    raw_material_id bigint,
    quantity        float(53) not null,
    amount          float(53) not null,
    date            DATE,
    primary key (id)
);
create table units_of_measurement
(
    id   bigserial not null,
    name varchar(255),
    primary key (id)
);
create table budget
(
    id              bigserial not null,
    amount        float(53) not null,
    percent       float(53),
    primary key (id)
);
alter table if exists employee
    add constraint FKbc8rdko9o9n1ri9bpdyxv3x7i foreign key (position_id) references position;
alter table if exists finished_product
    add constraint FK3obcvwifx24u8n5f5ndnbaido foreign key (unit_of_measurement_id) references units_of_measurement;
alter table if exists ingredient
    add constraint FK69xydlugx1kqwmos7eyupgwt2 foreign key (product_id) references finished_product;
alter table if exists ingredient
    add constraint FKg1e1pm5jma4unp25n2bjwkkjw foreign key (raw_material_id) references raw_material;
alter table if exists product_production
    add constraint FKbvmgu29i8pvbt9ybl6u8nim96 foreign key (employee_id) references employee;
alter table if exists product_production
    add constraint FKgtncrbxsiaiw16k0xflebfc4k foreign key (product_id) references finished_product;
alter table if exists product_sale
    add constraint FKilumy7oqc730orq52ufkuonu7 foreign key (employee_id) references employee;
alter table if exists product_sale
    add constraint FKg13sfw1y4if8tvor08gnlli55 foreign key (product_id) references finished_product;
alter table if exists raw_material
    add constraint FKdqag848aqyjckpoybgglwx5pv foreign key (unit_of_measurement_id) references units_of_measurement;
alter table if exists raw_material_purchases
    add constraint FK90olsu114jjgkbupjxb7cjh7t foreign key (employee_id) references employee;
alter table if exists raw_material_purchases
    add constraint FK6bfay1mfjmfet9c810femp6a2 foreign key (raw_material_id) references raw_material;
