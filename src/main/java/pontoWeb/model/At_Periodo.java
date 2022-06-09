package pontoWeb.model;

public class At_Periodo {

	private int id_at;
	private int id_periodo;

	public At_Periodo(int id_at, int id_periodo) {
		this.id_at = id_at;
		this.id_periodo = id_periodo;
	}

	public int getId_at() {
		return id_at;
	}

	public void setId_at(int id_at) {
		this.id_at = id_at;
	}

	public int getId_periodo() {
		return id_periodo;
	}

	public void setId_periodo(int id_periodo) {
		this.id_periodo = id_periodo;
	}
}
