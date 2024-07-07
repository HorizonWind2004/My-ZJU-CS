#include <iostream>
#include <vector>
using namespace std;

class vec3 {
public:
  vec3(int x=0, int y=0, int z=0)
  {
    v[0] = x;
    v[1] = y;
    v[2] = z;
  }
  int operator[] (const int &index) const
  {
    return v[index];
  }
  vec3& operator+=(const vec3& rhs)
  {
    for (int i = 0; i < 3; ++i)
      v[i] += rhs.v[i];
    return *this;
  }
private:
  int v[3];
};

vec3 operator+(const vec3& v1, const vec3& v2)
{
  return vec3(v1[0] + v2[0], v1[1] + v2[1], v1[2] + v2[2]);
}

ostream & operator<<(ostream& out, const vec3& v)
{
  out << '(' << v[0] << ' ' << v[1] << ' ' << v[2] << ')';
  return out;
}

int main()
{
  vec3 v1(1,2,3), v2(4,5,6);
  vec3 v = v1 + v2;
  v += v2;
  cout << v << endl;
}