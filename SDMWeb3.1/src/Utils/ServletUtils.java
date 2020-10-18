package Utils;

import SDMCommon.ChatManager;
import SDMCommon.SDManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;

import static chat.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String SDM_MANAGER_ATTRIBUTE_NAME = "sdmManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

    private static final Object chatManagerLock = new Object();
    private static final Object sdmManagerLock = new Object();

    public static SDManager getSDMManager(ServletContext servletContext) {

        synchronized (sdmManagerLock) {
            if (servletContext.getAttribute(SDM_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(SDM_MANAGER_ATTRIBUTE_NAME, new SDManager());
            }
        }
        return (SDManager) servletContext.getAttribute(SDM_MANAGER_ATTRIBUTE_NAME);
    }
    public static ChatManager getChatManager(ServletContext servletContext) {
        synchronized (chatManagerLock) {
            if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
    }
    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
            }
        }
        return INT_PARAMETER_ERROR;
    }
}
