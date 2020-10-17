package Servlets;

import JSObjects.StoreJS;
import SDMCommon.Feedback;
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

@WebServlet("/sendNewStoreAlert")
public class SendNewStoreAlertServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String storeJSON = req.getParameter("Store");
            String zoneName = req.getParameter("ZoneName");
            String areaManagerUserName = req.getParameter("AreaManagerUserName");
            String storeOwnerName = SessionUtils.getUsername(req);
            StoreJS storeJS;
            storeJS = gson.fromJson(storeJSON, StoreJS.class);
            if (storeJS == null) {
                resp.sendError(-1, "alert not valid.");
            } else {
                synchronized (this) {
                    try {
                        sdManager.addNewStoreAlertToUser(storeJS,zoneName,areaManagerUserName,storeOwnerName);
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
