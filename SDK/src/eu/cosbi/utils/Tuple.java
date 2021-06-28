package eu.cosbi.utils;

public class Tuple<X, Y> {
    X first;
    Y second;

    public Tuple(X f, Y s) {
	first = f;
	second = s;
    }

    public X first() {
	return first;
    }

    public void first(X newFirst) {
	first = newFirst;
    }

    public Y second() {
	return second;
    }

    public void second(Y newSecond) {
	second = newSecond;
    }

    @Override
    public String toString() {
	return "<" + first + ", " + second + ">";
    }
}
