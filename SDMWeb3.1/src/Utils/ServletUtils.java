package Utils;

import SDMCommon.SDManager;

import javax.servlet.ServletContext;
import java.awt.*;

public class ServletUtils {

    private static final String SDM_MANAGER_ATTRIBUTE_NAME = "sdmManager";
    private static final Object sdmManagerLock = new Object();

    public static SDManager getSDMManager(ServletContext servletContext) {

        synchronized (sdmManagerLock) {
            if (servletContext.getAttribute(SDM_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(SDM_MANAGER_ATTRIBUTE_NAME, new SDManager());
            }
        }
        return (SDManager) servletContext.getAttribute(SDM_MANAGER_ATTRIBUTE_NAME);
    }
}
