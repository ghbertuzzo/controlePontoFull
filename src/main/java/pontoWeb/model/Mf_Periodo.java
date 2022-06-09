package pontoWeb.model;

public class Mf_Periodo {

	private int id_mf;
	private int id_periodo;

	public Mf_Periodo(int id_mf, int id_periodo) {
		this.id_mf = id_mf;
		this.id_periodo = id_periodo;
	}

	public int getId_mf() {
		return id_mf;
	}

	public void setId_mf(int id_mf) {
		this.id_mf = id_mf;
	}

	public int getId_periodo() {
		return id_periodo;
	}

	public void setId_periodo(int id_periodo) {
		this.id_periodo = id_periodo;
	}

}
