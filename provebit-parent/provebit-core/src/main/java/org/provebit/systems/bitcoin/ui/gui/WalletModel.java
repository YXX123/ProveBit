package org.provebit.systems.bitcoin.ui.gui;

import java.util.Observable;

public class WalletModel extends Observable {

	public WalletModel() {
		
	}
	
	private void notifyChange() {
		setChanged();
		notifyObservers();
	}
}