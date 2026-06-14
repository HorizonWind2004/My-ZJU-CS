#ifndef MYVECTOR_H
#define MYVECTOR_H

#include <stdexcept>
#include <cstring> // For memset and memcpy

template <class T>
class Vector
{
public:
    Vector();                   // Creates an empty vector
    Vector(int size);           // Creates a vector for holding 'size' elements
    Vector(const Vector &r);    // Copy constructor
    ~Vector();                  // Destructs the vector
    T &operator[](int index);   // Accesses the specified element without bounds checking
    T &at(int index);           // Accesses the specified element, throws an exception of
                                // type 'std::out_of_range' when index < 0 or >= m_nSize
    int size() const;           // Returns the size of the container
    int capacity() const;       // Returns the capacity of the container
    T front() const;            // Returns a reference to the first element
    T back() const;             // Returns a reference to the last element
    void push_back(const T &x); // Adds an element to the end
    void clear();               // Clears the contents
    bool empty() const;         // Checks whether the container is empty

private:
    void inflate();             // Expands the storage of the container to a new capacity
    T *m_pElements;             // Pointer to the dynamically allocated storage
    int m_nSize;                // The number of elements in the container
    int m_nCapacity;            // The total number of elements that can be held in the allocated storage
};

// Implementation of template functions

/**
 * Default constructor
 * Creates an empty vector
 */
template <typename T>
Vector<T>::Vector()
    : m_pElements(nullptr), m_nSize(0), m_nCapacity(0) {}

/**
 * Constructor with size
 * Creates a vector for holding 'size' elements, initializing them to zero
 * @param size Number of elements to allocate
 */
template <typename T>
Vector<T>::Vector(int size)
    : m_nSize(size), m_nCapacity(size)
{
    m_pElements = new T[size];
    std::memset(m_pElements, 0, size * sizeof(T));
}

/**
 * Copy constructor
 * Creates a new vector as a copy of another vector
 * @param r The vector to copy
 */
template <typename T>
Vector<T>::Vector(const Vector<T> &r)
    : m_nSize(r.m_nSize), m_nCapacity(r.m_nCapacity)
{
    m_pElements = new T[m_nSize];
    std::memcpy(m_pElements, r.m_pElements, m_nSize * sizeof(T));
}

/**
 * Destructor
 * Destroys the vector and releases allocated memory
 */
template <typename T>
Vector<T>::~Vector()
{
    delete[] m_pElements;
}

/**
 * Operator []
 * Accesses the specified element without bounds checking
 * @param index Index of the element to access
 * @return Reference to the specified element
 */
template <typename T>
T &Vector<T>::operator[](int index)
{
    return m_pElements[index];
}

/**
 * Accesses the specified element with bounds checking
 * Throws an exception of type 'std::out_of_range' when index < 0 or >= m_nSize
 * @param index Index of the element to access
 * @return Reference to the specified element
 * @throws std::out_of_range when index is out of bounds
 */
template <typename T>
T &Vector<T>::at(int index)
{
    if (index < 0 || index >= m_nSize)
    {
        throw std::out_of_range("Index out of range");
    }
    return m_pElements[index];
}

/**
 * Returns the size of the container
 * @return Number of elements in the container
 */
template <typename T>
int Vector<T>::size() const
{
    return m_nSize;
}

/**
 * Returns the capacity of the container
 * @return Total number of elements that can be held in the allocated storage
 */
template <typename T>
int Vector<T>::capacity() const
{
    return m_nCapacity;
}

/**
 * Returns a reference to the first element
 * Throws an exception if the vector is empty
 * @return Reference to the first element
 * @throws std::out_of_range when the vector is empty
 */
template <typename T>
T Vector<T>::front() const
{
    if (m_nSize == 0)
    {
        throw std::out_of_range("Empty vector");
    }
    return m_pElements[0];
}

/**
 * Returns a reference to the last element
 * Throws an exception if the vector is empty
 * @return Reference to the last element
 * @throws std::out_of_range when the vector is empty
 */
template <typename T>
T Vector<T>::back() const
{
    if (m_nSize == 0)
    {
        throw std::out_of_range("Empty vector");
    }
    return m_pElements[m_nSize - 1];
}

/**
 * Adds an element to the end of the vector
 * Expands the capacity if needed
 * @param x The element to add
 */
template <typename T>
void Vector<T>::push_back(const T &x)
{
    if (m_nSize == m_nCapacity)
    {
        if (m_nCapacity == 0)
        {
            m_nCapacity = 1;
            m_pElements = new T[m_nCapacity];
        }
        else
        {
            inflate();
        }
    }
    m_pElements[m_nSize++] = x;
}

/**
 * Clears the contents of the vector
 * Resets the size to zero but retains the capacity
 */
template <typename T>
void Vector<T>::clear()
{
    m_nSize = 0; // Not change its capacity!
}

/**
 * Checks whether the container is empty
 * @return True if the vector is empty, otherwise false
 */
template <typename T>
bool Vector<T>::empty() const
{
    return m_nSize == 0;
}

/**
 * Expands the storage of the container to a new capacity
 * Doubles the capacity of the container
 */
template <typename T>
void Vector<T>::inflate()
{
    m_nCapacity = m_nCapacity == 0 ? 1 : m_nCapacity * 2;
    T *new_pElements = new T[m_nCapacity], *old_pElements = m_pElements;
    std::memcpy(new_pElements, m_pElements, m_nSize * sizeof(T));
    delete[] old_pElements;
    m_pElements = new_pElements;
}

#endif