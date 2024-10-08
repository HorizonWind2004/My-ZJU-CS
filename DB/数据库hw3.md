## 3.8 

![image-20240317000638811](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240317000638811.png)

![image-20240317000704351](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240317000704351.png)

(a): 

```sql
select ID
	from customer
	where ID in (select ID from depositor) and ID not in (select ID from borrower);
```

(b):

```sql
select c1.ID
	from customer c1, customer c2
	where c2.ID=='12345' and c1.ID<>'12345' and c1.customer_street = c2.customer_street and c1.customer_city = c2.customer_city;
```

(c):

```sql
select distinct account.branch_name
	from (account join depositor using(account_number)) join customer using(ID)
	where customer.customer_city = 'Harrison';
```

## 3.9

![image-20240317003056450](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240317003056450.png)

![image-20240317003136074](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240317003136074.png)

(a): 

```sql
select employee.ID, employee.person_name, employee.city
	from employee natural join works
	where works.company_name = 'First Bank Corporation';
```

(b):

```sql
select employee.ID, employee.person_name, employee.city
	from employee natural join works
	where works.company_name = 'First Bank Corporation' and works.salary > 10000;
```

(c):

```sql
select employee.ID
	from employee natural join works
	where works.company_name <> 'First Bank Corporation';
```

(d): 

```sql
with tmp (ID, salary) as
    (select employee.ID, works.salary
        from employee natural join works
        where works.company_name = 'Small Bank Corporation')
select t1.ID
	from works t1, tmp t2
	where t1.salary > all(select salary from t2);
```

(e):

```sql
select c1.company_name
    from company c1
    where not exists(
        select c2.city
        	from company c2
            where c2.company_name = 'Small Bank Corporation' and not exists(
                select c3.city
                	from company c3
                	where c3.company_name = c1.company_name and c3.city = c2.city
              )
    );
```

(f):

```sql
with tmp (name, number) as
    (select company_name, count(*) as number
        from works
        group by company_name)
select t1.name
	from tmp t1
	where t1.number = (select max(t2.number) from tmp t2);
```

(g): 

```sql
select w1.company_name
    from works w1
    group by w1.company_name
    having avg(w1.salary) > (
        select avg(w2.salary)
            from works w2
            where w2.company_name = 'First Bank Corporation'
    );
```

## 