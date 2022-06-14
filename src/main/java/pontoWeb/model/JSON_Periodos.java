package pontoWeb.model;

import java.util.ArrayList;
import java.util.List;

public class JSON_Periodos {

	private List<String> periodos;

	public JSON_Periodos() {
		this.periodos = new ArrayList<String>();
	}

	public List<String> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<String> periodos) {
		this.periodos = periodos;
	}

}
