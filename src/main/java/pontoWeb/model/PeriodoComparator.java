package pontoWeb.model;

import java.util.Comparator;

public class PeriodoComparator implements Comparator<Periodo> {

	@Override
	public int compare(Periodo periodo1, Periodo periodo2) {
		if (periodo1.getEntrada().getHour() == periodo2.getEntrada().getHour()) {
			if (periodo1.getEntrada().getMinute() == periodo2.getEntrada().getMinute()) {
				return Integer.compare(periodo1.getEntrada().getSecond(), periodo2.getEntrada().getSecond());
			} else {
				return Integer.compare(periodo1.getEntrada().getMinute(), periodo2.getEntrada().getMinute());
			}
		} else {
			return Integer.compare(periodo1.getEntrada().getHour(), periodo2.getEntrada().getHour());
		}
	}

}
