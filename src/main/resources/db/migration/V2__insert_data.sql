INSERT INTO POSITION (id,name)
values (1, 'администратор'),
       (2, 'менеджер'),
       (3, 'продавец-консультант');

INSERT INTO units_of_measurement(id, name)
values (1, 'килограмм'),
       (2, 'литр'),
       (3, 'шт');

INSERT INTO employee (full_name, position_id, salary, address, phone_number)
values ('Табылдиева Шоола' ,1, 50000, 'Айтматова 34', '708540610' ),
       ('Пак Ксения' ,2, 35000, 'Джал 5', '544212325' ),
       ('Алтынбеков Даурен' ,2, 20000, 'Малдыбаева 54', '708423165' ),
       ('Айтбеков Болотбек' ,3, 28250, 'Советская 98', '778964512' );

INSERT INTO finished_product(name, unit_of_measurement_id, amount, quantity)
values ('Горячий шоколад', 2 , 60, 10),
       ('Апельсиновый сок', 2, 10, 140),
       ('Лимонный фреш', 2, 5, 200);

INSERT INTO raw_material(name, unit_of_measurement_id, quantity, amount)
values ('Апельсин', 3, 100 , 8000),
       ('Лимон', 3, 150 , 63000),
       ('Вода', 2, 90 , 1350),
       ('Какао', 1, 40 , 400),
       ('Молоко', 2, 80 , 6000);