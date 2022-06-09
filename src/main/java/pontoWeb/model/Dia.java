package pontoWeb.model;

import java.util.Arrays;

public class Dia {

	private Boolean[] vectorDay;
	private final Integer SIZEVECTORDAY = 1440;

	public Dia() {
		this.vectorDay = new Boolean[SIZEVECTORDAY];
		Arrays.fill(this.vectorDay, Boolean.FALSE);
	}

	public Boolean[] getVectorDay() {
		return vectorDay;
	}

	public void setVectorDay(Boolean[] vetorDia) {
		this.vectorDay = vetorDia;
	}

}
