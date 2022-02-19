package de.baderrabea.cashmachine.model;


public class AccountDetails
{
	private final String iban;
	private final String bic;
	
	
	public AccountDetails(String iban, String bic) {
		this.iban = iban;
		this.bic = bic;
	}
	
	public String getIban() {
		return iban;
	}
	
	public String getBic() {
		return bic;
	}
	

	
}
