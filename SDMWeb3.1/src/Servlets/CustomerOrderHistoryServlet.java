package Servlets;

import SDMCommon.*;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/updateCustomerOrderHistoryTable")
public class CustomerOrderHistoryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String username = SessionUtils.getUsername(req);
            User user = sdManager.getUsers().get(username);
            List<CustomerLevelOrder> CLO = null;
            if(user instanceof Customer) {
                Customer cu = (Customer) user;
                CLO = cu.getOrderHistory();
            }
            String json = gson.toJson(CLO);
            out.println(json);
            out.flush();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String username = SessionUtils.getUsername(req);
            User user = sdManager.getUsers().get(username);
            List<CustomerLevelOrder> CLO = null;
            if(user instanceof Customer) {
                Customer cu = (Customer) user;
                CLO = cu.getOrderHistory();
            }
            String json = gson.toJson(CLO);
            out.println(json);
            out.flush();
        }
    }
}
