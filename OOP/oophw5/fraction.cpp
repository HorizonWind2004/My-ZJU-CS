#include <cmath>
#include <string>
#include <algorithm>
#include <fstream>
#include "fraction.h"
#define ll long long

ll fraction::gcd(ll a, ll b)
{
    if (b == 0)
        return a;
    return fraction::gcd(b, a % b);
}
void fraction::simplify()
{
    ll g = gcd(num, den);
    num /= g;
    den /= g;
    if (den < 0)
    {
        num *= -1;
        den *= -1;
    }
}

fraction::fraction(ll num, ll den) : num(num), den(den)
{
    if (den == 0)  
        throw std::runtime_error("Denominator cannot be zero.");
    simplify();
}
fraction::fraction(const fraction &f) : num(f.num), den(f.den)
{
    simplify();
}
fraction::operator double()
{
    return (double)num / den;
}
fraction::fraction(const std::string &s)
{
    int pos_type1 = s.find('/');
    int pos_type2 = s.find('.');
    if (pos_type1 != -1)
    {
        num = std::stoll(s.substr(0, pos_type1));
        den = std::stoll(s.substr(pos_type1 + 1));
    }
    else if (pos_type2 != -1)
    {
        std::string s1 = s.substr(0, pos_type2);
        std::string s2 = s.substr(pos_type2 + 1);
        num = std::stoll(s1 + s2);
        den = pow(10, s2.size());
    }
    else
    {
        num = std::stoll(s);
        den = 1;
    }
    simplify();
}
fraction fraction::operator+(const fraction &f)
{
    return fraction(num * f.den + f.num * den, den * f.den);
}
fraction fraction::operator-(const fraction &f)
{
    return fraction(num * f.den - f.num * den, den * f.den);
}
fraction fraction::operator*(const fraction &f)
{
    return fraction(num * f.num, den * f.den);
}
fraction fraction::operator/(const fraction &f)
{
    return fraction(num * f.den, den * f.num);
}
std::string fraction::toString()
{
    return std::to_string(num) + "/" + std::to_string(den);
}
bool fraction::operator==(const fraction &f)
{
    return num == f.num && den == f.den;
}
bool fraction::operator!=(const fraction &f)
{
    return num != f.num || den != f.den;
}
bool fraction::operator<(const fraction &f)
{
    return num * f.den < f.num * den;
}
bool fraction::operator>(const fraction &f)
{
    return num * f.den > f.num * den;
}
bool fraction::operator<=(const fraction &f)
{
    return num * f.den <= f.num * den;
}
bool fraction::operator>=(const fraction &f)
{
    return num * f.den >= f.num * den;
}
fraction fraction::operator=(const fraction &f)
{
    num = f.num;
    den = f.den;
    return *this;
}
std::ostream &operator<<(std::ostream &os, const fraction &f)
{
    os << f.num << "/" << f.den;
    return os;
}
std::istream &operator>>(std::istream &is, fraction &f)
{
    is >> f.num >> f.den;
    f.simplify();
    return is;
}