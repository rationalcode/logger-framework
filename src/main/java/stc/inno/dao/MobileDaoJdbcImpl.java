package stc.inno.dao;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import stc.inno.ConnectionManager.ConnectionManager;
import stc.inno.pojo.Mobile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;


public class MobileDaoJdbcImpl implements MobileDao {
    Logger logger = LoggerFactory.getLogger(this.getClass());
     public static final String INSERT_INTO_MOBILE = "INSERT INTO mobile values (DEFAULT, ?, ?, ?)";
    public static final String SELECT_FROM_MOBILE = "SELECT * FROM mobile WHERE id = ?";
    public static final String UPDATE_MOBILE      = "UPDATE mobile SET model=?, price=?, manufacturer=? WHERE id=?";
    public static final String DELETE_FROM_MOBILE = "DELETE FROM mobile WHERE id=?";


    private final ConnectionManager connectionManager;

    public MobileDaoJdbcImpl(ConnectionManager connectionManager) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("").setLevel(Level.FINEST);
        this.connectionManager = connectionManager;
    }

    @Override
    public Long addMobile(Mobile mobile) {
        logger.trace("addMobile {}", mobile);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_MOBILE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, mobile.getModel());
            preparedStatement.setInt(2, mobile.getPrice());
            preparedStatement.setString(3, mobile.getManufacturer());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }

        } catch (SQLException e) {
            logger.error("addMobile" , e);
        }
        return 0L;
    }

    @Override
    public Mobile getMobileById(Long id) {
        logger.info("getMobileById");
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_MOBILE)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Mobile(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getInt(3),
                            resultSet.getString(4));
                }
            }
        } catch (SQLException e) {
            logger.error("getMobileById" , e);
        }
        return null;
    }

    @Override
    public boolean updateMobileById(Mobile mobile) {
        logger.info("updateMobileById");
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MOBILE)) {
            preparedStatement.setString(1, mobile.getModel());
            preparedStatement.setInt(2, mobile.getPrice());
            preparedStatement.setString(3, mobile.getManufacturer());
            preparedStatement.setInt(4, mobile.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.error("updateMobileById" , e);
        }
        return false;
    }

    @Override
    public boolean deleteMobileById(Long id) {
        logger.info("deleteMobileById");
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_MOBILE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("deleteMobileById" , e);
            return false;
        }
        return true;
    }

    @Override
    public void renewDatabase() throws SQLException {

        try (
                Connection connection = connectionManager.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute("-- Database: jdbcDB\n"
                              + "DROP TABLE IF EXISTS mobile;"
                              + "\n"
                              + "CREATE TABLE mobile (\n"
                              + "    id bigserial primary key,\n"
                              + "    model varchar(100) NOT NULL,\n"
                              + "    price integer NOT NULL,\n"
                              + "    manufacturer varchar(100) NOT NULL);"
                              + "\n"
                              + "INSERT INTO mobile (model, price, manufacturer)\n"
                              + "VALUES\n"
                              + "   ('P1', 100, 'Xiaomi'),\n"
                              + "   ('EDGE', 1, 'Micro'),\n"
                              + "   ('FRY1', 1001, 'Apple'),\n"
                              + "   ('FRY1', 1002, 'Apple'),\n"
                              + "   ('OGO', 10000, 'SAMSUNG');"
                              + "\n"
                              + "DROP TABLE IF EXISTS mobile;"
                              + "\n"
                              + "CREATE TABLE mobile (\n"
                              + "    id bigserial primary key,\n"
                              + "    model varchar(100) NOT NULL,\n"
                              + "    price integer NOT NULL,\n"
                              + "    manufacturer varchar(100) NOT NULL);"
                              + "\n"
                              + "INSERT INTO mobile (model, price, manufacturer)\n"
                              + "VALUES\n"
                              + "   ('P1', 100, 'Xiaomi'),\n"
                              + "   ('EDGE', 1, 'Micro'),\n"
                              + "   ('FRY1', 1001, 'Apple'),\n"
                              + "   ('FRY1', 1002, 'Apple'),\n"
                              + "   ('OGO', 10000, 'SAMSUNG');"
                              + "\n"
                              + "DROP PROCEDURE IF EXISTS insert_data(integer);"
                              + "\n"
                              + "CREATE OR REPLACE PROCEDURE insert_data(a integer)\n"
                              + "    LANGUAGE SQL\n"
                              + "AS\n"
                              + "    $$\n"
                              + "    UPDATE mobile SET price = price + 1 WHERE id = a\n"
                              + "$$;"
                              + "DROP FUNCTION IF EXISTS multiply(integer);"
                              + "\n"
                              + "CREATE OR REPLACE FUNCTION multiply(a INT) RETURNS INT AS $$\n"
                              + "BEGIN\n"
                              + "    RETURN a * 2;\n"
                              + "END\n"
                              + "$$ LANGUAGE 'plpgsql';");
        }
    }
}
