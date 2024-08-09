show databases;
use healthflow;
show tables;
create table login(user_id int,username varchar(20),password varchar(20),role varchar(20));
insert into login values(1,"admin","encrypted_password","admin");
insert into login values(2,"Doctor1","encrypted_password","Doctor");
insert into login values(3,"Doctor2","encrypted_password","Doctor");
insert into login values(4,"staff1","encrypted_password","staff");
select * from login;
create table patient_registration(patient_Id int,Name varchar(20),Gender varchar(20),DOB int,Blood_group varchar(20),Address varchar(50),contact_no int);
insert into patient_registration values(101,"Joy","Male",26/10/2005,"B+","Juhu",8976564);
select * from patient_registration ;
insert into patient_registration values(102,"Mary","female",27/11/2003,"A+","Bandra",8978664);
insert into patient_registration values(103,"Rohit","Male",09/1/2000,"AB+","Kurla",9006564);
select * from patient_registration ;
create table Biling_Invoice(patien_Id int,bill_amount int,total_amt int );
insert into Biling_Invoicevalues values(101,3000,3500);
insert into Biling_Invoice values(102,2000,2200);
insert into Biling_Invoice values(103,6000,6800);
select * from Biling_Invoice;
insert into Biling_Invoicevalues values(101,3000,3500);
create table appointment(patient_Id int,appointment_type varchar(20),doctor_name varchar(20));
insert into appointment values(101,"checkup","Dr.Joshi");
insert into appointment values(102,"dialysis","Dr.Kadam");
select * from appointment;
create table Billing__Invoice(patient_Id int,invoice_date varchar(20),due_date varchar(20)); 
insert into Billing__Invoice values(101,24/10/2024,30/10/2024);
select * from Billing__In

