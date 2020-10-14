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
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet("/createBasicOrder")
public class CreateBasicOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String userName = SessionUtils.getUsername(req);
            String customerOrderJSON = req.getParameter("CustomerOrder");
            CustomerLevelOrderJS customerLevelOrderJS ;
            String OrderType = req.getParameter("OrderType");
            String storeName = req.getParameter("StoreName");
            String zoneName = req.getParameter("ZoneName");
            String date = req.getParameter("Date");
            LocalDate localDate = LocalDate.parse(date);
            String customerXLocation = req.getParameter("CustomerXLocation");
            String customerYLocation = req.getParameter("CustomerYLocation");
            Point customerLocation = new Point(Integer.parseInt(customerXLocation),Integer.parseInt(customerYLocation));

            customerLevelOrderJS = gson.fromJson(customerOrderJSON, CustomerLevelOrderJS.class);
            customerLevelOrderJS.setCustomerLocation(customerLocation);
            if (customerLevelOrderJS == null) {
                resp.sendError(-1, "Order not valid.");
            } else {//Synchronize????????
                if(OrderType.equals("Static")){
                    customerLevelOrderJS = sdManager.createBasicStaticCustomerOrder(customerLevelOrderJS,storeName,zoneName);
                }else {
                    synchronized (this) {
                        customerLevelOrderJS = sdManager.createBasicDynamicCustomerOrder(customerLevelOrderJS, zoneName, sdManager.getUsers().get(userName).getID(), localDate, customerLocation);
                    }
                }
                customerLevelOrderJS.setCustomerName(userName);
                customerLevelOrderJS.setCustomerID(sdManager.getUsers().get(userName).getID());
                String json = gson.toJson(customerLevelOrderJS);
                out.println(json);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
