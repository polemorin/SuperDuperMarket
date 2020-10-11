package Servlets;

import ProductTypes.ProductTableInfo;
import SDMCommon.SDMZoneInfo;
import SDMCommon.SDManager;
import SDMCommon.User;
import Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/updateProductDetailsTable")
public class updateProductDetailsTableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String zoneName = req.getParameter("ZoneName");
            if(zoneName == null){
                resp.sendError(-1,"Zone name not valid.");
            }
            else{
                List<ProductTableInfo> productTableInfosTableValues = sdManager.getAllProductFromZoneInfo(zoneName);
                String json = gson.toJson(productTableInfosTableValues);
                out.println(json);
                out.flush();
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String zoneName = req.getParameter("ZoneName");
            if(zoneName == null){
                resp.sendError(-1,"Zone name not valid.");
            }
            else{
                List<ProductTableInfo> productTableInfosTableValues = sdManager.getAllProductFromZoneInfo(zoneName);
                String json = gson.toJson(productTableInfosTableValues);
                out.println(json);
                out.flush();
            }

        }
    }
}
