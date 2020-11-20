package stc.inno;

import org.slf4j.bridge.SLF4JBridgeHandler;
import stc.inno.ConnectionManager.ConnectionManager;
import stc.inno.ConnectionManager.ConnectionManagerJdbcImpl;
import stc.inno.dao.MobileDao;
import stc.inno.dao.MobileDaoJdbcImpl;
import stc.inno.pojo.Mobile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    Logger logger = Logger.getLogger("Main");
    public static void main(String[] args) throws SQLException, IOException {



        ConnectionManager connectionManager = ConnectionManagerJdbcImpl.getInstance();

        MobileDao         mobileDao         = new MobileDaoJdbcImpl(connectionManager);
        mobileDao.renewDatabase();
        Main main = new Main();
        main.method1(mobileDao);
    }


    public void method1(MobileDao mobileDao) throws IOException {
/*        logger.setUseParentHandlers(false);
        final FileHandler fileHandler = new FileHandler("logs/%ujava.log");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);*/

        Mobile mobile = new Mobile(null, "Iphone 2", 25000, "Apple");
        Long   aLong  = mobileDao.addMobile(mobile);
        mobile = mobileDao.getMobileById(aLong);
        logger.info("Начальный объект: {" + mobile + "}");
        mobile.setPrice(getPrice(mobile.getPrice(), 2));
        mobileDao.updateMobileById(mobile);
        mobile = mobileDao.getMobileById(aLong);
        logger.severe("Итоговый объект: {" + mobile + "}");
    }

    public int getPrice(int oldPrice, int multiply) {
        return oldPrice * multiply;
    }
}
