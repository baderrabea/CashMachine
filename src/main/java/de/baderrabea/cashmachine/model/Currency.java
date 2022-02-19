package de.baderrabea.cashmachine.model;


public abstract class Currency {
	private double factor;
	private String sign;
	
	public Currency(double factor, String sign){
		this.factor = factor;
		this.sign = sign;
	}
	
	
	public double convertFromEuro(double amount)
	{
		return Math.round(100 * (amount * factor)) / 100;
	}
	
	public double convertToEuro(double amount)
	{
		return Math.round(100 * (amount / factor)) / 100;
	}
	
	public String getCurrencyCode() {
		return sign;
	}
	
	public double getFactor()
	{
		return factor;
	}
}
