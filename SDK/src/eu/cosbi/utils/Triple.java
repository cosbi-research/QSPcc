package eu.cosbi.utils;

import java.util.Iterator;

public class Triple<X, Y, Z> implements Iterable<X> {
    X first;
    Y second;
    Z third;

    public Triple(X f, Y s) {
	first = f;
	second = s;
	third = null;
    }

    public Triple(X f, Y s, Z k) {
	first = f;
	second = s;
	third = k;
    }

    public X first() {
	return first;
    }

    public Y second() {
	return second;
    }

    public Z third() {
	return third;
    }

    @Override
    public String toString() {
	return "<" + first + ", " + second + ", " + third + ">";
    }

    @Override
    public Iterator<X> iterator() {
	return new Iterator<X>() {
	    public int cnt = 0;

	    @Override
	    public boolean hasNext() {
		return cnt < 3;
	    }

	    @Override
	    public X next() {
		cnt++;
		if (cnt == 1)
		    return first;
		else if (cnt == 2)
		    return (X) second;
		else if (cnt == 3)
		    return (X) third;
		else
		    return null;
	    }
	};
    }
}
