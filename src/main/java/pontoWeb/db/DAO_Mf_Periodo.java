package pontoWeb.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_Mf_Periodo {

	private ConnectionFactoryDB connection;

	public DAO_Mf_Periodo(ConnectionFactoryDB connection) throws SQLException {
		this.connection = connection;
	}

	public void insert(int idMF, int idPeriodo) throws SQLException {
		String querysql = "INSERT INTO \"schemaControlePonto\".mf_periodo(id, id_mf, id_periodo) VALUES (default, ?, ?);";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ps.setInt(1, idMF);
		ps.setInt(2, idPeriodo);
		ps.execute();
		ps.close();
	}
	
	public List<Integer> getPeriods(int idMF) throws SQLException {
		List<Integer> listIds = new ArrayList<Integer>();
		String querysql = "SELECT id_periodo FROM \"schemaControlePonto\".mf_periodo WHERE id_mf = ?;";
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql);
		ps.setInt(1, idMF);
		ResultSet rs;
		rs = ps.executeQuery();
		while (rs.next()) {
			listIds.add(rs.getInt(1));
		}		
		ps.close();
		return listIds;
	}
}
