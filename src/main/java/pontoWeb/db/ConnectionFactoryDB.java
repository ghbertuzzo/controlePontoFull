package pontoWeb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactoryDB {
	
	private Connection connection;
	
	public ConnectionFactoryDB() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		this.connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/dbControlePonto", "postgres", "admin");
	}

	public Connection getConnection() throws SQLException {
		return this.connection;
	}

}
