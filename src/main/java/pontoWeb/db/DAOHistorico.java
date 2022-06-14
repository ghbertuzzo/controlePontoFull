package pontoWeb.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pontoWeb.model.Historico;

public class DAOHistorico {

	private ConnectionFactoryDB connection;
	
	public DAOHistorico(ConnectionFactoryDB connection) {
		this.connection = connection;
	}
	
	public List<Historico> getHistoricos(ConnectionFactoryDB connection) throws SQLException {
		this.connection = connection;
		List<Historico> historicos = new ArrayList<Historico>();
		String querysql = "SELECT date, id, id_ht, id_mf, id_he, id_at	FROM \"schemaControlePonto\".historico ORDER BY date;";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ResultSet rs;
		rs = ps.executeQuery();
		while (rs.next()) {
			Historico historico = new Historico(rs.getDate(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),rs.getInt(6));
			historicos.add(historico);
		}
		ps.close();
		return historicos;
	}
	
	public void insert(String date, int idHT, int idMF, int idHE, int idAT) throws SQLException {
		String querysql = "INSERT INTO \"schemaControlePonto\".historico(id, date, id_ht, id_mf, id_he, id_at) VALUES (default, ?, ?, ?, ?, ?);";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ps.setDate(1, java.sql.Date.valueOf(date));
		ps.setInt(2, idHT);
		ps.setInt(3, idMF);
		ps.setInt(4, idHE);
		ps.setInt(5, idAT);
		ps.execute();
		ps.close();
	}
	
	public Historico getHistoricoByDate(String date) throws SQLException {
		Historico historico = null;
		String querysql = "SELECT date, id, id_ht, id_mf, id_he, id_at	FROM \"schemaControlePonto\".historico WHERE date=?;";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ps.setDate(1, java.sql.Date.valueOf(date));
		ResultSet rs;
		rs = ps.executeQuery();
		while (rs.next()) {
			historico = new Historico(rs.getDate(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),rs.getInt(6));
		}
		ps.close();
		return historico;
	}
	
	public void delete(int id) throws SQLException {
		String querysql = "DELETE FROM \"schemaControlePonto\".historico WHERE id=?;";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);		
		ps.setInt(1, id);		
		ps.executeUpdate();
		ps.close();
	}
}
