#include <iostream>
#include <cassert>
#include <cmath>
#include <string>
#include <algorithm>
#include <fstream>
#include "fraction.h"
#define ll long long

int main() {
    // Various tests to validate functionality of the fraction class
    fraction defaultFraction;
    assert(defaultFraction.toString() == "0/1");

    fraction f1(2, 4);
    assert(f1.toString() == "1/2");

    fraction copyOfF1(f1);
    assert(copyOfF1.toString() == "1/2");

    fraction fromString1("3/4");
    assert(fromString1.toString() == "3/4");

    fraction fromString2("0.25");
    assert(fromString2.toString() == "1/4");

    fraction fromString3("2");
    assert(fromString3.toString() == "2/1");

    fraction f2(1, 3);
    fraction sum = f1 + f2;  // 1/2 + 1/3 = 3/6 + 2/6 = 5/6
    assert(sum.toString() == "5/6");

    fraction difference = f1 - f2;  // 1/2 - 1/3 = 3/6 - 2/6 = 1/6
    assert(difference.toString() == "1/6");

    fraction product = f1 * f2;  // 1/2 * 1/3 = 1/6
    assert(product.toString() == "1/6");

    fraction quotient = f1 / f2;  // 1/2 / 1/3 = 1/2 * 3/1 = 3/2
    assert(quotient.toString() == "3/2");

    assert(f1 == fraction(1, 2));
    assert(f1 == fraction(3, 6)); 
    assert(f1 != f2);
    assert(f1 < fraction(2, 3));
    assert(f1 <= fraction(1, 2));
    assert(f1 > fraction(1, 3));
    assert(f1 >= fraction(1, 2));
    assert((double)f1 == 0.5);

    std::cout << "Please enter num and den: ";
    fraction f4;
    std::cin >> f4;
    std::cout << f4 << std::endl;

    std::cout << "All tests passed! Now, try whether there is a runtime error when constructing a fraction whose denominator is 0." << std::endl;
    try {
        fraction f5(1, 0);
    } catch (const std::runtime_error& e) {
        std::cout << "Caught runtime error as expected: " << e.what() << std::endl;
    }
    
    return 0;
}
