package com.artesania.santander.paginator;

public class PageItem {
	
	//Logica del funcionamiento de los items del paginator (1,2,3)
	
	private int numero;
	private boolean actual;
	
	public PageItem(int numero, boolean actual) {
		this.numero = numero;
		this.actual = actual;
	}

	public int getNumero() {
		return numero;
	}

	public boolean isActual() {
		return actual;
	}
	

}
