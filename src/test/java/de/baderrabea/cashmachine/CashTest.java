package de.baderrabea.cashmachine;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.baderrabea.cashmachine.model.Account;
import de.baderrabea.cashmachine.model.AccountDetails;
import de.baderrabea.cashmachine.model.CashCard;
import de.baderrabea.cashmachine.model.CashMachine;
import de.baderrabea.cashmachine.model.CashMachineException;
import de.baderrabea.cashmachine.model.Currency;
import de.baderrabea.cashmachine.model.Dollar;

import java.util.Map;
import java.util.TreeMap;

public class CashTest {

    CashMachine cashMachine;
    CashCard cashCard1;

    @BeforeEach
    void setUp() {

        AccountDetails accountDetails = new AccountDetails("DE330543223", "64050000");
        Currency currency = new Dollar(1, "$");
            
        Account account1 = new Account(accountDetails, 200, 200,currency, 9513);

        Map<String,Account<? extends Currency>> accounts = new TreeMap<>();

		accounts.put(String.valueOf(account1.getDetails().getIban()), account1);

        cashMachine = new CashMachine(accounts);

        cashCard1 = new CashCard(accountDetails);
    }

    @Test
    void overDraw() {

        try {
            cashMachine.insertCashCard(cashCard1);
            cashMachine.enterPin(9513);
            Assertions.assertThrows(CashMachineException.class,() ->{
                
                cashMachine.withdraw(500);

            });
        } catch (CashMachineException e) {
            e.printStackTrace();
        }
    }

 
}