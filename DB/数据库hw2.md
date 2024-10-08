### 2.7 

Consider the bank database of Figure 2.18. Give an expression in the relational algebra for each of the following queries: 

- (a) Find the name of each branch located in “Chicago”. 

- (b) Find the ID of each borrower who has a loan in branch “Downtown”.

  <img src="C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240310164840480.png" alt="image-20240310164840480" style="zoom:50%;" />

(a): $\prod_{branch\_name}(\sigma_{branch\_city=\texttt{Chicago}}(branch))$

(b): $\prod_{ID}(borrower⋉_{borrower.loan\_number=loan.loan\_number}loan)$

### 2.12 

Consider the bank database of Figure 2.18. Assume that branch names and customer names uniquely identify branches and customers, but loans and accounts can be associated with more than one customer. 

- (a) What are the appropriate primary keys? 

​	$branch\_names, customer\_names, ID, loan\_number$.

- (b) Given your choice of primary keys, identify appropriate foreign key.

​	对于关系 branch: 选择 $\{branch\_name\}$ 为 primary key, 没有 foreign key.

​	对于关系 customer: 选择 $\{ID\}$ 为 primary key, 没有 foreign key.

​	对于关系 loan: 选择 $\{loan\_number\}$ 为 primary key, $\{branch\_name\}$ 为 foreign key.

​	对于关系 borrower 和 depositor: 选择 $\{ID, loan\_number\}$ 为 primary key, $\{ID, loan\_number\}$ 为 foreign key.

​	对于关系 account: 选择 $\{account\_number\}$ 为 primary key, $\{branch\_name\}$ 为 foreign key.

### 2.13

 Construct a schema diagram for the bank database of Figure 2.18.

<img src="C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240310193535520.png" alt="image-20240310193535520" style="zoom:80%;" />

### 2.15

Consider the bank database of Figure 2.18. Give an expression in the relational algebra for each of the following queries: 

- (a) Find each loan number with a loan amount greater than 10000.

​	$\prod_{loan\_number}(\sigma_{amount>10000}(loan))$

- (b) Find the ID of each depositor who has an account with a balance greater than 6000. 

​	$\prod_{ID}(depositor⋉_{despositor.account\_number=account.account\_number\land account.balance>6000}account)$

- (c) Find the ID of each depositor who has an account with a balance greater than $6000 at the “Uptown” branch.

​	$\prod_{ID}(depositor⋉_{despositor.account\_number=account.account\_number\land account.balance>6000\land account.branch\_name=\texttt{Uptown}}account)$