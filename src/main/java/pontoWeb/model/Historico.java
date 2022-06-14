package pontoWeb.model;

public class Historico {

	private java.sql.Date date;
	private int id;
	private int id_ht;
	private int id_mf;
	private int id_he;
	private int id_at;

	public Historico(java.sql.Date date, int id, int id_ht, int id_mf, int id_he, int id_at) {
		this.date = date;
		this.id = id;
		this.id_ht = id_ht;
		this.id_mf = id_mf;
		this.id_he = id_he;
		this.id_at = id_at;
	}

	public java.sql.Date getDate() {
		return date;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_ht() {
		return id_ht;
	}

	public void setId_ht(int id_ht) {
		this.id_ht = id_ht;
	}

	public int getId_mf() {
		return id_mf;
	}

	public void setId_mf(int id_mf) {
		this.id_mf = id_mf;
	}

	public int getId_he() {
		return id_he;
	}

	public void setId_he(int id_he) {
		this.id_he = id_he;
	}

	public int getId_at() {
		return id_at;
	}

	public void setId_at(int id_at) {
		this.id_at = id_at;
	}

}
