package pontoWeb.model;

import java.time.LocalTime;

public class Periodo {

	private LocalTime entrada;
	private LocalTime saida;

	public Periodo(LocalTime entrada, LocalTime saida) {
		super();
		this.entrada = entrada;
		this.saida = saida;
	}

	public LocalTime getEntrada() {
		return entrada;
	}

	public void setEntrada(LocalTime entrada) {
		this.entrada = entrada;
	}

	public LocalTime getSaida() {
		return saida;
	}

	public void setSaida(LocalTime saida) {
		this.saida = saida;
	}

}
