![image-20240428225953772](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240428225953772.png)

$\{A,B,C\}$ 有 $A\rightarrow BC$，$\therefore A$ 为主键。

$\{A,D,E\}$ 有 $E\rightarrow A$

所以他们的交集 $\{A\}$ 正好是 $\{A,B,C\}$ 的主键，所以是 lossless 的。

![image-20240428231242054](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240428231242054.png)

$\{A,B,C\}$ 只保留了 $A\rightarrow BC$，后者  $\{A,D,E\}$ 只保留了 $E\rightarrow A$。显然没有任何和 $D$ 有关的依赖，所以 $B\rightarrow D$ 永远无法被推出。所以不是 dependency-preserving 的。

![image-20240428231427168](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240428231427168.png)

$A$ 能推出所有， $\{A\}$ 为主键。同理 $\{E\}$ 为主键。$\{C,D\},\{B,C\}$ 为主键。

$\{B\}$ 不为主键，所以分解为：$\{B,D\},\{A,B,C,E\}$。对于交集 $\{B\}$，此为 $\{B,D\}$ 的主键。所以符合要求。

![image-20240429124309035](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240429124309035.png)

 $\{A,B,C\},\{B,D\},\{E,A\},\{C,D,E\}$

$\{A,B,C\}$ 连接 $\{B,D\}$ ，$\{B\}$ 为 $\{B,D\}$ 主键。得到 $\{A,B,C,D\}$，于是继续连接 $\{C,D,E\}$，最后连接 $\{E,A\}$ 即可。显然是第三范式。

![image-20240429125557556](C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240429125557556.png)

<img src="C:\Users\xieji\AppData\Roaming\Typora\typora-user-images\image-20240429125612165.png" alt="image-20240429125612165" style="zoom:67%;" />

- a. $B^+=\{A,B,C,D,E\}$。

- b. $A\rightarrow BCD,BC\rightarrow DE,\therefore A\rightarrow BCDE,AG\rightarrow ABCDEG$。显然没有子集符合要求。

- c. $D$ 在 $A\rightarrow BCD$ 中重复（因为 $B\rightarrow D$），所以变成 $A\rightarrow BC$。同理删去 $D$ 得到 $BC\rightarrow E$。

  答案为 $A\rightarrow BC,BC\rightarrow E, B\rightarrow D,D\rightarrow A$。

- d. $\{A,B,C\},\{B,C,E\},\{B,D\},\{A,D\},\{A,G\}$

- e. $\{A,G\},\{A,B,C\},\{B,D\},\{A,E\}$
