package pontoWeb.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOMarcacoesFeitas {

	private ConnectionFactory connection;

	public DAOMarcacoesFeitas(ConnectionFactory connection) throws SQLException {
		this.connection = connection;
	}

	public Integer insert() throws SQLException {
		String querysql = "INSERT INTO \"schemaControlePonto\".marcacoes_feitas (id) VALUES (DEFAULT);";
		String generatedColumns[] = { "id" };
		PreparedStatement ps = this.connection.getConnection().prepareStatement(querysql, generatedColumns);		
		int affectedRows = ps.executeUpdate();
		long id = -1;
		if (affectedRows > 0) {
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					id = rs.getLong(1);
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		ps.close();
		return (int) id;
	}
}
