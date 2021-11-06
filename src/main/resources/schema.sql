create database jdbc_demo;

use jdbc_demo;

drop  table if exists name;
drop  table if exists mobile;
drop  table if exists email;
drop  table if exists employee;

select * from name;

create table employee(
emp_id bigint auto_increment primary key ,
crt_tms TIMESTAMP not null ,
exp_tms TIMESTAMP);

create table name (
name_id bigint auto_increment primary key ,
emp_id bigint not null,
emp_fn varchar(100) not null , 
emp_ln varchar(100) not null ,
crt_tms      TIMESTAMP not null ,
exp_tms      TIMESTAMP);
ALTER TABLE name ADD constraint name_id_employee_id_fk  FOREIGN KEY (emp_id) REFERENCES employee(emp_id);


create table mobile(
mobile_id  bigint auto_increment primary key,
emp_id bigint not null,
mob_num varchar(10),
prefered char,
block char,
crt_tms      TIMESTAMP not null,
exp_tms      TIMESTAMP);
ALTER TABLE mobile ADD constraint mobile_id_employee_id_fk  FOREIGN KEY (emp_id) REFERENCES employee(emp_id);

create table email(
email_id  bigint auto_increment primary key,
emp_id bigint not null,
email_add varchar(50),
prefered char,
block char,
crt_tms      TIMESTAMP not null,
exp_tms      TIMESTAMP);
ALTER TABLE email ADD constraint email_id_employee_id_fk  FOREIGN KEY (emp_id) REFERENCES employee(emp_id);


commit;
####
