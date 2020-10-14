package Servlets;

import JSObjects.CustomerLevelOrderJS;
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

@WebServlet("/placeOrderInMarket")
public class PlaceOrderInMarketServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String userName = SessionUtils.getUsername(req);
            String customerOrderJSON = req.getParameter("CustomerOrder");
            CustomerLevelOrderJS customerLevelOrderJS ;
            String zoneName = req.getParameter("ZoneName");
            customerLevelOrderJS = gson.fromJson(customerOrderJSON, CustomerLevelOrderJS.class);
            if (customerLevelOrderJS == null) {
                resp.sendError(-1, "Order not valid.");
            } else {
                synchronized (this) {
                    try {
                        sdManager.placeOrderInMarket(customerLevelOrderJS, zoneName,userName);
                        out.println("Order was confirmed!");
                        out.flush();
                    }
                    catch (Exception e){
                        out.println("Error "+e.getMessage()+" order was not made.");
                        out.flush();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
