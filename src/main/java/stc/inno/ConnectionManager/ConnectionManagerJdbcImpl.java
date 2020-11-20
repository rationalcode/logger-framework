package stc.inno.ConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerJdbcImpl implements ConnectionManager {
    public static final ConnectionManager INSTANCE = new ConnectionManagerJdbcImpl();

    private ConnectionManagerJdbcImpl() {
    }

    public static ConnectionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public Connection getConnection() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5434/jdbcDB",
                    "postgres",
                    "qwerty");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public int get15() {
        return 15;
    }
}
