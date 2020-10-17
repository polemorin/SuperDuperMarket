package Servlets;

import JSObjects.StoreLevelOrderJS;
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

@WebServlet("/sendFeedbackAlert")
public class SendFeedBackAlertServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String feedBackJSON = req.getParameter("FeedBack");
            String zoneName = req.getParameter("ZoneName");
            String storeID = req.getParameter("StoreID");
            Feedback customerFeedBack;
            customerFeedBack = gson.fromJson(feedBackJSON, Feedback.class);
            if (customerFeedBack == null) {
                resp.sendError(-1, "Feedback not valid.");
            } else {
                synchronized (this) {
                    try {
                        sdManager.addFeedbackAlertToUser(customerFeedBack,zoneName,storeID);
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
