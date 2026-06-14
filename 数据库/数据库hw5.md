## 4.7

![image-20240401013259700](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240401013259700.png)

```sql
create table company ( 
    company_name varchar(50),
    city varchar(50),
    primary key (company_name)
);

create table employee ( 
    id integer,
    person_name varchar(50),
    street varchar(50),
    city varchar(50),
    primary key (id)
);

create table works (
    id integer,
    company_name varchar(50),
    salary numeric(10,2),
    primary key (id),
    foreign key (id) references employee(id),
    foreign key (company_name) references company(company_name)
);

create table manages ( 
    id integer,
    manager_id integer, 
    primary key (id), 
    foreign key (id) references employee (id), 
    foreign key (manager_id) references employee (id)
);

```

## 4.9

![image-20240401013313249](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240401013313249.png)

删除 manager 记录将连锁删除其下属员工记录，然后会删除下属的间接下属，最后其所有间接下属都会被删除。

## 4.12

![image-20240401013324792](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240401013324792.png)

（有参考其他资料）

角色进行授权可以确保即便授权操作者离职或账户被废止，其授权依然持续有效。在大多数情况下，基于角色的授权方式更加契合高效管理的企业环境，提供更为稳定和一致的权限维护机制。



```sql
create table departments (
    d_id int primary key,
    name varchar(255) not null,
    check (name != '')
);

create table employees (
    e_id int primary key,
    name varchar(255) not null,
    sex varchar(1) not null,
    d_id int,
    foreign key (d_id) references departments(d_id)
        on delete set null
        on update cascade,
    check (name != ''),
    check (sex in ('F', 'M'))
);
```

```ql
insert into departments (d_id, name) values (1, 'D1'), (2, 'D2'), (3, ''), (4, 'D4');
```

```sql
update departments 
	set d_id = 5 where d_id = 2;
select * from employees;
```

