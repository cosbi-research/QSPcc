package eu.cosbi.utils;

public class Quintuple<X, Y, Z, K, W> {
    X first;
    Y second;
    Z third;
    K fourth;
    W fifth;

    public Quintuple(X f, Y s, Z z, K k, W w) {
	first = f;
	second = s;
	third = z;
	fourth = k;
	fifth = w;
    }

    public X first() {
	return first;
    }

    public Quintuple<X, Y, Z, K, W> first(X newFirst) {
	first = newFirst;
	return this;
    }

    public Y second() {
	return second;
    }

    public Quintuple<X, Y, Z, K, W> second(Y newSecond) {
	second = newSecond;
	return this;
    }

    public Z third() {
	return third;
    }

    public Quintuple<X, Y, Z, K, W> third(Z newThird) {
	third = newThird;
	return this;
    }

    public K fourth() {
	return fourth;
    }

    public Quintuple<X, Y, Z, K, W> fourth(K newFourth) {
	fourth = newFourth;
	return this;
    }

    public W fifth() {
	return fifth;
    }

    public Quintuple<X, Y, Z, K, W> fifth(W newFifth) {
	fifth = newFifth;
	return this;
    }

    @Override
    public String toString() {
	return "<" + first + ", " + second + ", " + third + ", " + fourth + ", " + fifth + ">";
    }

}
