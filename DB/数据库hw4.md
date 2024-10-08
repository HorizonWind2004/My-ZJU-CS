## 3.10

![image-20240323224946881](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240323224946881.png)

![image-20240323225133448](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240323225133448.png)

(a): 

```sql
update employee
	set city = 'Newtown'
	where ID = '12345';
```

(b):

```sql
update works
	set salary = case
		when salary * 1.1 <= 100000 then salary * 1.1
		else salary * 1.03
	end
	where company_name = 'First Bank Corporation' and ID in (select ID from manages);
```

## 3.11

![image-20240323231102632](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240323231102632.png)

<img src="C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240323232106794.png" alt="image-20240323232106794" style="zoom: 67%;" />

(a):

```sql
select distinct s.id, s.name
    from student s, takes t, course c
    where s.id = t.id and t.course_id = c.course_id and c.dept_name = 'Comp. Sci.';
```

(b):

```sql
select s.id, s.name
    from student s
    where not exists (
        select *
            from takes t
            where t.id = s.id and t.year < 2017
    );
```

(c):

```sql
select d.dept_name, max(i.salary) as max_salary
    from department d, instructor i
    where d.dept_name = i.dept_name
    group by d.dept_name;
```

(d):

```sql
select min(tmp1)
    from (
        select max(i.salary) as tmp1
            from instructor i
            group by i.dept_name
    ) as answer;
```

## 3.15

![image-20240323233622783](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240323233622783.png)

![image-20240323233917718](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240323233917718.png)

(a):

```sql
select c1.ID, c1.customer_name
	from customer c1
	where not exists(
    	// 不存在 “该 customer 无账户位于的 brooklyn 的子公司”
        select b1.branch_name from branch b1
        	where b1.branch_city = 'Brooklyn' and not exists(
            	select depositor.ID from depositor, account
                	where depositor.ID == c1.ID and account.branch_name == b1.branch_name
                		  and account.account_number = depositor.account_number
            )
    );
```

(b):

```sql
select sum(amount) as answer
from loan;
```

(c):

```sql
select distinct b1.branch_name
    from branch b1
    where b1.assets > some (
        select b2.assets
        from branch b2
        where b2.branch_city = 'Brooklyn'
    );
```