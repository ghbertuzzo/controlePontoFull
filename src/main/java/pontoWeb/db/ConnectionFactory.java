package pontoWeb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	private Connection connection;
	
	public ConnectionFactory() throws SQLException, ClassNotFoundException {
		this.connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/dbControlePonto", "postgres", "admin");
	}

	public Connection getConnection() throws SQLException {
		return this.connection;
	}

}
