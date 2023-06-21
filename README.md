# HotelManagement
* [General Info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Usage](#usage)

## General Info
This Backend Project is a simple Hotel Management System. It is connects to a MySQL Database which consists 
out of 3 tables:
* Room: Contains all the rooms in the hotel
* Guests: Contains all the guests that have stayed in the hotel and for how long
* Employees: Contains all the employees that work in the hotel

There are 2 more Tables, which are needed for the authentication:
* users: Contains all the users
* authorities: Contains all the roles of the users

Through the API you can access all the data in the database and also add, update and delete data.

## Technologies
Project is created with:
* Java version: 17
* Maven version: 3.8.1
* MySQL version: 8.0.26

## Setup
To run this project, you need to have a MySQL Database running on your machine.
You can find the SQL-Script in the GitHub Repository with the name [Database.sql](Database.sql).
Copy the script and run it in your MySQL Workbench.

## Usage
All the endpoints which are available are listed in the [restapi.yaml](src/main/resources/restapi.yaml) file.
#### Authentication
To use certain endpoints you need to authenticate yourself. 
The endpoints that need authentication are:
* Admin Role: ` /users/**`
* User Role: 
  * ` /employee/**`
  * ` /guests/**`

Here is the default user for the authentication and Admin-Role:
```
Username: admin
Password: adminPassword
```
Through the ` /users/**` endpoint you can add, update and delete users and their roles.

