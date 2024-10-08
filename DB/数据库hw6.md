![image-20240407153407977](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240407153407977.png)

![image-20240407153449661](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240407153449661.png)

```sql
create trigger check1 after insert on depositor
referencing new row as nrow
for each row
begin
	insert into branch_cust select account.branch_name, nrow.customer_name
		from account
		where nrow.account_number = account.account_number
end;

create trigger check2 after insert on account
referencing new row as nrow
for each row
begin
	insert into branch_cust select nrow.branch_name, depositor.customer_name
		from depositor
		where nrow.account_number = depositor.account_number
end;
```

![image-20240407155449495](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240407155449495.png)

``` sql
create function avg_salary(query_name varchar(255))
return numeric(20,10)
begin
	declare answer numberic(20,10);
	select average into answer
		from (
        	select company_name, avg(salary) as average
            from works
            group by company_name;
        )
    where company_name = query_name;
	return answer;
end
select company_name
	from works
	where avg_salary(company_name) > avg_salary('First Bank');
```

![image-20240407161321161](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240407161321161.png)

```sql
create trigger cascade_delete after delete on s
referencing old row as orow
for each row
begin
    delete from r where B = orow.A;
end
```

