package Servlets;

import JSObjects.StoreJS;
import JSObjects.StoreLevelOrderJS;
import SDMCommon.SDManager;
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

@WebServlet("/sendOrderAlert")
public class SendOrderAlertServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String userName = SessionUtils.getUsername(req);
            String storeOrderJS = req.getParameter("StoreOrder");
            StoreLevelOrderJS storeLevelOrderJS;
            String zoneName = req.getParameter("ZoneName");
            storeLevelOrderJS = gson.fromJson(storeOrderJS, StoreLevelOrderJS.class);
            if (storeLevelOrderJS == null) {
                resp.sendError(-1, "Alert not valid.");
            } else {
                synchronized (this) {
                    try {
                        sdManager.addOrderAlertToUser(storeLevelOrderJS, zoneName,userName);
                    }
                    catch (Exception e){
                        out.println("Error "+e.getMessage()+" alert was not sent.");
                        out.flush();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
