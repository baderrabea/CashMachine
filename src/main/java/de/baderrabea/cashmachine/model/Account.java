package de.baderrabea.cashmachine.model;


public class Account<T extends Currency>{
	private AccountDetails details;
	private double overdraft;
	private double bankDeposit;
	private T currency;
	private final int pin;
	

	public Account(AccountDetails details, double overdraft, double bankDeposit, T currency, int pin)
	{
		this.details = details;
		this.bankDeposit = bankDeposit;
		this.overdraft = overdraft;
		this.currency = currency;
		this.pin = pin;
	}
	
	public void withdraw(double amountEuro) throws IllegalWithdrawException
	{
		if(currency.convertToEuro(bankDeposit + overdraft) < amountEuro)
		{
			throw new IllegalWithdrawException("Account was overdrawn. Withdrawn amount is too high.");
		}
		else
		{
			this.bankDeposit = this.bankDeposit - currency.convertFromEuro(amountEuro);
			System.out.println("The new Balance is: " + bankDeposit);
		}
	}
	
	public void deposit(int amount)
	{
		bankDeposit = 
				bankDeposit + Math.round(Math.pow(10.0, 2) 
						* (amount * currency.getFactor())) / Math.pow(10.0, 2);
	}

	
	// Getter setter area
	public double getOverdraft() {
		return overdraft;
	}

	public T getCurrency() {
		return currency;
	}

	public void setOverdraft(double overdraft) {
		this.overdraft = overdraft;
	}

	public double getBankDeposit() {
		return bankDeposit;
	}

	public void setBankDeposit(double bankDeposit) {
		this.bankDeposit = bankDeposit;
	}

	public AccountDetails getDetails()
	{
		return details;
	}
	
	public int getPin() {
		return pin;
	}
	
	public String toString(){
		return details.getIban();
	}
}

			



