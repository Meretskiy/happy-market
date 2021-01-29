create table products (
    id          bigserial primary key,
    title       varchar(255),
    price       int,
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
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

create table order_items (
     id                      bigserial primary key,
     title                   varchar(255),
     quantity                int,
     price_per_item          int,
     price                   int
);