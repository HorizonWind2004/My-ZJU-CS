#ifndef FRACTION_H
#define FRACTION_H
#include <cmath>
#include <string>
#include <fstream>
#define ll long long
class fraction
{
private:
    ll num, den;
    ll gcd(ll a, ll b);
    void simplify();

public:
    fraction(ll num = 0, ll den = 1);
    fraction(const fraction &f);
    fraction(const std::string &s);
    fraction operator+(const fraction &f);
    fraction operator-(const fraction &f);
    fraction operator*(const fraction &f);
    fraction operator/(const fraction &f);
    std::string toString();
    bool operator==(const fraction &f);
    bool operator!=(const fraction &f);
    bool operator<(const fraction &f);
    bool operator>(const fraction &f);
    bool operator<=(const fraction &f);
    bool operator>=(const fraction &f);
    fraction operator=(const fraction &f);
    operator double();
    friend std::ostream &operator<<(std::ostream &os, const fraction &f);
    friend std::istream &operator>>(std::istream &is, fraction &f);
};
#endif
