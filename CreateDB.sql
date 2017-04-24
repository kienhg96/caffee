CREATE TABLE employee (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
    name VARCHAR(100),
    phone VARCHAR(100),
    address VARCHAR(100),
    username VARCHAR(100) NOT NULL UNIQUE, 
    password VARCHAR(100)
);

CREATE TABLE tables (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    status INT NOT NULL
);

CREATE TABLE manager (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
    name VARCHAR(100),
    phone VARCHAR(100),
    address VARCHAR(100),
    username VARCHAR(100) NOT NULL UNIQUE, 
    password VARCHAR(100)
);

CREATE TABLE drink (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    price INT NOT NULL
);

alter table tables add column billId INT;

CREATE TABLE bill (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    createdDate DATE NOT NULL
);

CREATE TABLE billcontent (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    billId INT NOT NULL,
    drinkId INT NOT NULL,
    quantity INT NOT NULL,
    constraint fk_billcontent_bill foreign key (billId) references bill(id) ON DELETE CASCADE,
    constraint fk_billcontent_drink foreign key (drinkId) references drink(id) ON DELETE CASCADE
);

alter table bill modify createdDate DATETIME;
