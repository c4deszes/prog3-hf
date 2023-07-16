package org.xpression.math;

/*
 * XVector holds the value of an N dimensional vector
 * this class is immutable, so it shouldn't be used in time critical applications
 */
public class XVector {

	private int n;
	private Double[] t;
	
	public XVector(String string) {
		String[] data = string.split(",");
		this.n = data.length;
		this.t = new Double[data.length];
		for(int i = 0;i<n;i++) {
			t[i] = new Double(data[i]);
		}
	}
	
	public XVector(int n) {
		this.n = n;
		this.t = new Double[this.n];
	}
	
	public XVector(double... d) {
		this.n = d.length;
		this.t = new Double[this.n];
		for(int i=0;i<this.n;i++) {
			this.t[i] = new Double(d[i]);
		}
	}
	
	public XVector(double x, double y) {
		this.n = 2;
		this.t = new Double[this.n];
		this.t[0] = new Double(x);
		this.t[1] = new Double(y);
	}
	
	public XVector(double x, double y, double z) {
		this.n = 3;
		this.t = new Double[this.n];
		this.t[0] = new Double(x);
		this.t[1] = new Double(y);
		this.t[2] = new Double(z);
	}
	
	public XVector(XVector vector) {
		this.n = vector.getDimension();
		this.t = new Double[this.n];
	}
	
	public int getDimension() {
		return n;
	}
	
	public Double[] get() {
		return t;
	}
	
	public double get(int i) {
		return t[i].doubleValue();
	}

	public XVector ADD(XVector v) {
		if(this.getDimension() != v.getDimension()) {
			throw new ArithmeticException("Cannot add " + this + " and " + v + " (dimension mismatch)");
		}
		XVector out = new XVector(this.getDimension());
		for(int i=0;i<out.getDimension();i++) {
			out.get()[i] = new Double(this.get(i) + v.get(i));
		}
		return out;
	}
	
	public XVector SUBSTRACT(XVector v) {
		if(this.getDimension() != v.getDimension()) {
			throw new ArithmeticException("Cannot add " + this + " and " + v + " (dimension mismatch)");
		}
		XVector out = new XVector(this.getDimension());
		for(int i=0;i<out.getDimension();i++) {
			out.get()[i] = new Double(this.get(i) - v.get(i));
		}
		return out;
	}
	
	public XVector MULTIPLY(Double n) {
		XVector out = new XVector(this.getDimension());
		for(int i=0;i<out.getDimension();i++) {
			out.get()[i] = new Double(this.get(i) * n.doubleValue());
		}
		return out;
	}
	
	public Double MULTIPLY(XVector v) {
		if(this.getDimension() != v.getDimension()) {
			throw new ArithmeticException("Cannot add " + this + " and " + v + " (dimension mismatch)");
		}
		double out = 0;
		for(int i=0;i<this.getDimension();i++) { 
			out += this.get(i) * v.get(i);
		}
		return new Double(out);
	}
	
	public XVector DIVIDE(Double n) {
		XVector out = new XVector(this.getDimension());
		for(int i=0;i<out.getDimension();i++) {
			out.get()[i] = new Double(this.get(i) / n.doubleValue());
		}
		return out;
	}
	
	public String toString() {
		String out = "(";
		for(int i=0;i<this.getDimension();i++) {
			if(i != 0) {
				out += ", ";
			}
			out += this.get(i);
		}
		out += ")";
		return out;
	}

}
