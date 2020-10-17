package Servlets;

import JSObjects.CustomerLevelOrderJS;
import JSObjects.StoreJS;
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

@WebServlet("/addStore")
public class AddStoreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String userName = SessionUtils.getUsername(req);
            String storeJSON = req.getParameter("StoreToAdd");
            StoreJS storeJS;
            String zoneName = req.getParameter("ZoneName");
            storeJS = gson.fromJson(storeJSON, StoreJS.class);
            if (storeJS == null) {
                resp.sendError(-1, "Store not valid.");
            } else {
                synchronized (this) {
                    try {
                        sdManager.addStoreToMarket(storeJS, zoneName,userName);
                        out.println("Store was added to market");
                        out.flush();
                    }
                    catch (Exception e){
                        out.println("Error "+e.getMessage()+" store was not added.");
                        out.flush();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
