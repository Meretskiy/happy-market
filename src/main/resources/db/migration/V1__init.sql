create table products (id bigserial primary key, title varchar(255), price int);
insert into products(title, price)
values
('bread', 24),
('milk', 65),
('cheese', 320);
