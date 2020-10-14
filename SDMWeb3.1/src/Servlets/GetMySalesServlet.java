package Servlets;

import JSObjects.StoreLevelOrderJS;
import SDMCommon.SDManager;
import SDMSale.Sale;
import Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getSales")
public class GetMySalesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String storeOrderJSON = req.getParameter("StoreOrders");
            StoreLevelOrderJS[] storeOrderJS ;
            String OrderType = req.getParameter("OrderType");
            String storeName = req.getParameter("StoreName");
            String zoneName = req.getParameter("ZoneName");
            storeOrderJS = gson.fromJson(storeOrderJSON, StoreLevelOrderJS[].class);
            if (storeOrderJS == null) {
                resp.sendError(-1, "Order not valid.");
            } else {//Synchronize????????
                Sale[] saleProductJSArray = null;
                if(OrderType.equals("Static")) {
                    saleProductJSArray = sdManager.getMyStaticSales(storeOrderJS, zoneName);
                }
                else{
                    saleProductJSArray = sdManager.getMyDynamicSales(storeOrderJS, zoneName);
                }
                String json = gson.toJson(saleProductJSArray);
                out.println(json);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
