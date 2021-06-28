package eu.cosbi.utils;

public class Quadruple<X, Y, Z, K> {
    X first;
    Y second;
    Z third;
    K fourth;

    public Quadruple(X f, Y s, Z z, K k) {
	first = f;
	second = s;
	third = z;
	fourth = k;
    }

    public X first() {
	return first;
    }

    public Quadruple<X, Y, Z, K> first(X newFirst) {
	first = newFirst;
	return this;
    }

    public Y second() {
	return second;
    }

    public Quadruple<X, Y, Z, K> second(Y newSecond) {
	second = newSecond;
	return this;
    }

    public Z third() {
	return third;
    }

    public Quadruple<X, Y, Z, K> third(Z newThird) {
	third = newThird;
	return this;
    }

    public K fourth() {
	return fourth;
    }

    public Quadruple<X, Y, Z, K> fourth(K newFourth) {
	fourth = newFourth;
	return this;
    }

    @Override
    public String toString() {
	return "<" + first + ", " + second + ", " + third + ", " + fourth + ">";
    }
}
