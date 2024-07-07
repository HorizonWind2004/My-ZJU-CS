#include <iostream>
#include <cassert>
#include "myvector.h"
#include <ctime>

int main()
{
    // Test basic push_back functionality and capacity doubling
    Vector<int> v;
    int true_capacity = 1;
    for (int i = 1; i <= 16; ++i)
    {
        v.push_back(i);
        assert(v.size() == i);
        assert(v.capacity() == true_capacity);
        if (i == true_capacity)
        {
            true_capacity *= 2;
        }
    }

    // Test copy constructor
    Vector<int> v2(v);
    assert(v2.size() == v.size());
    assert(v2.capacity() == v.capacity());
    for (int i = 0; i < v.size(); ++i)
    {
        assert(v2[i] == v[i]);
    }

    // Test clear and empty functions
    v.clear();
    assert(v.size() == 0);
    assert(v.capacity() != 0);
    assert(v.empty() == true);

    // Test exception handling with at() function
    try
    {
        v.at(0);
    }
    catch (std::out_of_range &e)
    {
        std::cerr << e.what() << std::endl;
    }

    // Test constructor with size and default values
    Vector<char> v3(10);
    assert(v3.size() == 10);
    assert(v3.capacity() == 10);
    for (int i = 0; i < v3.size(); ++i)
    {
        assert(v3[i] == 0);
        v3[i] = 'a' + i;
    }

    assert(v3.front() == 'a');
    assert(v3.back() == 'j');

    // Test vector of pairs
    Vector<std::pair<int, int>> v4;
    for (int i = 0; i < 10; ++i)
    {
        v4.push_back(std::make_pair(i, i));
    }
    for (int i = 0; i < 10; ++i)
    {
        assert(v4[i].first == i);
        assert(v4[i].second == i);
    }

    // Time test for inserting 10 million elements
    Vector<int> v5;
    clock_t start = clock();
    for (int i = 0; i < 10000000; ++i)
    {
        v5.push_back(i);
    }
    clock_t end = clock();
    std::cout << "Time for push_back 1e7 elements: " << (double)(end - start) / CLOCKS_PER_SEC << "s" << std::endl;

    std::cout << "Accept!" << std::endl;

    return 0;
}
