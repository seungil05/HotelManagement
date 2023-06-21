use m295_projekt;

CREATE TABLE Rooms (
    roomId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    floor int NOT NULL,
    roomNumber varchar(255) NOT NULL,
    capacity int NOT NULL,
    price int NOT NULL
);

CREATE TABLE Guests (
    guestId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstName varchar(100) NOT NULL,
    lastName varchar(100) NOT NULL,
    emailAddress varchar(255) NOT NULL,
    roomId int,
    visitTime int NOT NULL,
    foreign key (roomId) references Rooms(roomId)
);

CREATE TABLE Employees (
    employeeId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstName varchar(100) NOT NULL,
    lastName varchar(100) NOT NULL,
    job varchar(255) NOT NULL
);

create table users(
	username varchar(50) not null primary key,
	password varchar(500) not null,
	enabled boolean not null
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);