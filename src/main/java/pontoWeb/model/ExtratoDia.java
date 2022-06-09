package pontoWeb.model;

public class ExtratoDia {
	
	private String date;
	private String atrasos;
	private String horaExtra;
	private String total;
	
	public ExtratoDia(String date,String atrasos,String horaExtra,String total) {
		this.date = date;
		this.atrasos = atrasos;
		this.horaExtra = horaExtra;
		this.total = total;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAtrasos() {
		return atrasos;
	}
	public void setAtrasos(String atrasos) {
		this.atrasos = atrasos;
	}
	public String getHoraExtra() {
		return horaExtra;
	}
	public void setHoraExtra(String horaExtra) {
		this.horaExtra = horaExtra;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	
}
