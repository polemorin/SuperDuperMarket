package Servlets;

import Alerts.IUserAlert;
import JSObjects.StoreLevelOrderJS;
import SDMCommon.SDManager;
import SDMCommon.Store;
import SDMCommon.StoreOwner;
import SDMCommon.User;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.List;

@WebServlet("/getAlerts")
public class GetAlertForUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String userName = SessionUtils.getUsername(req);
            User user = sdManager.getUsers().get(userName);
            StoreOwner storeOwner = (StoreOwner)(user);
            List<IUserAlert> userAlerts;
            synchronized (this) {
                try {
                    userAlerts = sdManager.AlertsForUser(storeOwner);
                    if(userAlerts.size()!= 0) {
                        out.println(gson.toJson(userAlerts));
                    }else{
                        out.print("no alerts");
                    }
                    out.flush();
                    storeOwner.emptyAlertList();
                }
                catch (Exception e){
                    out.println("Error "+e.getMessage()+" alert was not sent.");
                    out.flush();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
