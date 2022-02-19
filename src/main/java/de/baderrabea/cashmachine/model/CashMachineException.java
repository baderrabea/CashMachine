package de.baderrabea.cashmachine.model;

public class CashMachineException extends java.lang.Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8513859888036958501L;

	public CashMachineException(String msg) 
	{
		super(msg);
	}
	
	public CashMachineException(String msg, Throwable t) 
	{
		super(msg, t);
	}

}
