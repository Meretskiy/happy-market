create table users (
   id                      bigserial primary key,
   username                varchar(30) not null unique,
   password                varchar(80) not null,
   email                   varchar(50) unique,
   created_at              timestamp default current_timestamp,
   updated_at              timestamp default current_timestamp
);

create table roles (
   id                      bigserial primary key,
   name                    varchar(50) not null unique,
   created_at              timestamp default current_timestamp,
   updated_at              timestamp default current_timestamp
);

CREATE TABLE users_roles (
     user_id    bigint not null references users (id),
     role_id    bigint not null references roles (id),
     primary key (user_id, role_id)
);

insert into roles (name)
values
('ROLE_USER'),
('ROLE_ADMIN');

insert into users (username, password, email)
values
('bob', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'bob_johnson@gmail.com'),
('john', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'john_johnson@gmail.com');

insert into users_roles (user_id, role_id)
values
(1, 1),
(2, 2);

create table products (
      id                      bigserial primary key,
      title                   varchar(255),
      price                   int,
      created_at              timestamp default current_timestamp,
      updated_at              timestamp default current_timestamp
);

insert into products(title, price)
values
('Milk', 95),
('Bread', 75),
('Cheese', 950),
('Pasta', 105),
('Sausages', 280),
('Eggs', 110),
('Tomato', 165),
('Cucumbers', 150),
('Zucchini', 145),
('Potato', 45),
('Beet', 60),
('Water', 100),
('Pork', 360),
('Beef', 540),
('Fish', 390),
('Apples', 99),
('Pears', 135),
('Bananas', 89),
('Kiwi', 170),
('Cola', 128),
('Oranges', 180);

create table orders (
    id                      bigserial primary key,
    owner_id                bigint not null references users (id),
    price                   int,
    created_at              timestamp default current_timestamp,
    updated_at              timestamp default current_timestamp
);

create table order_items (
     id                      bigserial primary key,
     order_id                bigint not null references orders (id),
     product_id              bigint not null references products (id),
     title                   varchar(255),
     quantity                int,
     price_per_product       int,
     price                   int,
     created_at              timestamp default current_timestamp,
     updated_at              timestamp default current_timestamp
);