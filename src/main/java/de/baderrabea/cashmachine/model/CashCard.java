package de.baderrabea.cashmachine.model;

public class CashCard {

	private AccountDetails details;

	public CashCard(AccountDetails details) 
	{
		this.details = details;
	}
	
	public AccountDetails getDetails() {
		return details;
	}
	
}
