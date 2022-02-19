package de.baderrabea.cashmachine.ui;

import java.util.HashMap;
import java.util.Map;

import de.baderrabea.cashmachine.model.Account;
import de.baderrabea.cashmachine.model.AccountDetails;
import de.baderrabea.cashmachine.model.CashCard;
import de.baderrabea.cashmachine.model.Currency;
import de.baderrabea.cashmachine.model.Dollar;
import de.baderrabea.cashmachine.model.Euro;

public class MockFactory {

	public static Map<String, Account<? extends Currency>> createMockingAccountMap() {
		Map<String, Account<? extends Currency>> accountMap = new HashMap<>();
		accountMap.put("123456",
				new Account<>(new AccountDetails("123456", "BicEuro"), 20, 50, new Euro(1.00, "EUR"), 1234));
		accountMap.put("654321",
				new Account<>(new AccountDetails("654321", "BicDollar"), 500, 5000, new Dollar(1.24, "USD"), 5678));
		return accountMap;
	}

	public static CashCard createMockingCashCard(String iban) {
		switch (iban) {
		case "123456":
			return new CashCard(new AccountDetails("123456", "BicEuro"));
		case "654321":
			return new CashCard(new AccountDetails("654321", "BicDollar"));
		default:
			return new CashCard(new AccountDetails("0", "noCashCard"));
		}
	}

}
