package Servlets;

import ProductTypes.ProductTableInfo;
import ProductTypes.StoreProduct;
import ProductTypes.StoreProductInfo;
import SDMCommon.SDManager;
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

@WebServlet("/getStoreProducts")
public class GetStoreProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String storeName = req.getParameter("StoreName");
            String zoneName = req.getParameter("ZoneName");
            if (storeName == null) {
                resp.sendError(-1, "Store name not valid.");
            } else {
                List<StoreProductInfo> storeProductInfosList = sdManager.getStoreProducts(storeName, zoneName);
                String json = gson.toJson(storeProductInfosList);
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
