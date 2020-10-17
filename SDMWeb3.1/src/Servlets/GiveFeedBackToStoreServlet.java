package Servlets;

import JSObjects.CustomerLevelOrderJS;
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

@WebServlet("/giveFeedBackToStore")
public class GiveFeedBackToStoreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String userName = SessionUtils.getUsername(req);
            String feedBackJSON = req.getParameter("FeedBack");
            Feedback customerFeedBack;
            String zoneName = req.getParameter("ZoneName");
            String StoreID = req.getParameter("StoreID");
            customerFeedBack = gson.fromJson(feedBackJSON, Feedback.class);
            if (customerFeedBack == null) {
                resp.sendError(-1, "FeedBack not valid.");
            } else {
                synchronized (this) {
                    try {
                        sdManager.placeFeedBackInMarket(customerFeedBack, zoneName,StoreID);
                        out.println("FeedBack received");
                        out.flush();
                    }
                    catch (Exception e){
                        out.println("Error "+e.getMessage()+" feedback was not made.");
                        out.flush();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
