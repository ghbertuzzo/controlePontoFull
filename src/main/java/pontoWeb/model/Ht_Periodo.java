package pontoWeb.model;

public class Ht_Periodo {

	private int id_ht;
	private int id_periodo;

	public Ht_Periodo(int id_ht, int id_periodo) {
		this.id_ht = id_ht;
		this.id_periodo = id_periodo;
	}

	public int getId_ht() {
		return id_ht;
	}

	public void setId_ht(int id_ht) {
		this.id_ht = id_ht;
	}

	public int getId_periodo() {
		return id_periodo;
	}

	public void setId_periodo(int id_periodo) {
		this.id_periodo = id_periodo;
	}

}
