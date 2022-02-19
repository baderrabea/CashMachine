package de.baderrabea.cashmachine.model;

public class Transaction {

	private Account<? extends Currency> sourceAccount;
	private String targetIban;
	private int amount;
	
	public Transaction(Account<? extends Currency> quellAccount, String targetIban, int amount)
	{
		this.sourceAccount = quellAccount;
		this.targetIban = targetIban;
		this.amount = amount;
	}

	public Account<? extends Currency> getSourceAccount() {
		return sourceAccount;
	}

	public String getTargetIban() {
		return targetIban;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public String toString() {
		return targetIban;
	}
}