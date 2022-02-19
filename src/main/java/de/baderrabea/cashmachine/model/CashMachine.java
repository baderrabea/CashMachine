package de.baderrabea.cashmachine.model;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CashMachine {

	private Map<String, Account<? extends Currency>> accountMap;
	private CashCard cashCard;
	private Account<? extends Currency> selectedAccount;
	private State state;
	private int pinSecurity;
	private Queue<Transaction> transactionQueue;

	public CashMachine(Map<String, Account<? extends Currency>> accs) {
		this.accountMap = accs;
		state = State.READY;
		transactionQueue = new LinkedList<Transaction>();
	}

	public void insertCashCard(CashCard cashCard) throws CashMachineException {
		switch (state) {
		case CARD_INSERTED:
		case PIN_WRONG:
		case PIN_CORRECT:
			throw new CashMachineException("Cash Machine is not ready for this operation.");
		case READY:

			this.cashCard = cashCard;

			selectedAccount = selectAccount(this.cashCard.getDetails().getIban());
			if (selectedAccount == null) {
				this.cashCard = null;
				throw new CashMachineException("Account is not available at this machine.");
			}
			state = State.CARD_INSERTED;
			System.out.println("New status is: " + state);

			pinSecurity = 0;
			break;

		default:
			throw new CashMachineException("Cash Machine does not support this state.");

		}
	}

	private Account<? extends Currency> selectAccount(String iban) {
		
		if(accountMap.containsKey(iban)){
			return accountMap.get(iban);
		}
		return null;
	}

	public void withdraw(double amount) throws CashMachineException {
		switch (state) {
		case CARD_INSERTED:
		case READY:
		case PIN_WRONG:
			throw new CashMachineException("Cash Machine is not ready for this operation.");
		case PIN_CORRECT:
			try {
				selectedAccount.withdraw(amount);
			} catch (IllegalWithdrawException e) {
				throw new CashMachineException(
						"Cash Machine is not ready for this operation because: " + e.getMessage(), e);
			}
			break;
		default:
			break;

		}
	}

	public String accountStatement() throws CashMachineException {
		switch (state) {
		case CARD_INSERTED:
		case PIN_CORRECT:
		case PIN_WRONG:
			StringBuilder sb = new StringBuilder();
			sb.append("Account information: ");
			sb.append("Account number: " + selectedAccount.getDetails().getIban());
			sb.append(" - ");
			sb.append("Balance: " + selectedAccount.getBankDeposit() + " "
					+ selectedAccount.getCurrency().getCurrencyCode());
			sb.append(" - ");
			sb.append("Overdraft: " + selectedAccount.getOverdraft() + " "
					+ selectedAccount.getCurrency().getCurrencyCode());
			
			System.out.println(sb);
			return sb.toString();
		case READY:
			throw new CashMachineException("Cash Machine is not ready for this operation.");
		default:
			break;

		}
		return null;
	}

	public void ejectCashCard() throws CashMachineException {
		switch (state) {
		case CARD_INSERTED:
		case PIN_CORRECT:
		case PIN_WRONG:
			selectedAccount = null;
			cashCard = null;
			state = State.READY;
			System.out.println("New status is: " + state);
			break;
		case READY:
			throw new CashMachineException("Cash Machine is not ready for this operation.");
		default:
			break;

		}
	}

	public void enterPin(int pin) throws CashMachineException {
		switch (state) {
		case CARD_INSERTED:
		case PIN_WRONG:
			enter(pin);
			break;
		case PIN_CORRECT:
		case READY:
			throw new CashMachineException("Cash Machine is not ready for this operation.");
		default:
			throw new CashMachineException("Cash Machine does not support this state.");
		}
	}

	public void enter(int pin) throws CashMachineException {
		pinSecurity++;
		if (selectedAccount.getPin() == pin) {
			state = State.PIN_CORRECT;
			System.out.println("New status is: " + state);
		} else {
			if (pinSecurity == 3) {
				ejectCashCard();
				throw new CashMachineException("Pin three times false. Card was confiscated.");
			}
			state = State.PIN_WRONG;
			System.out.println("New status is: " + state);
		}
	}

	public void pushTransaction(Transaction transaction) {
		transactionQueue.add(transaction);
	}

	public void proceedTransactionQueue() throws CashMachineException {
		while (!transactionQueue.isEmpty()) {
			Transaction trans = transactionQueue.poll();

			Account<? extends Currency> source = selectAccount(trans.getSourceAccount().getDetails().getIban());
			if (source == null) {
				throw new CashMachineException("Source Account not detected!");
			}

			Account<? extends Currency> target = selectAccount(trans.getTargetIban());
			if (target == null) {
				throw new CashMachineException("Target Account not detected!");
			}

			try {
				source.withdraw(trans.getAmount());
			} catch (IllegalWithdrawException e) {
				throw new CashMachineException(
						"Cash Machine is not ready for this operation because: " + e.getMessage(), e);
			}
			target.deposit(trans.getAmount());
		}
	}

	public State getState() {
		return state;
	}

	public CashCard getCashCard() {
		return cashCard;
	}

	public Account<? extends Currency> getSelectedAccount() {
		return selectedAccount;
	}
	
	public boolean isPinCorrect(){
		if(state.equals(State.PIN_CORRECT)){
			return true;
		}
		return false;
	}
	
	public int getPinSecurity(){
		return pinSecurity;
	}
}
