#include<iostream>
int main(){
    int a = 1;
    int &b = a;
    int c = b;
    a++;
    b++;
    std::cout << c << std::endl;
    std::cout << a << std::endl;
    
}