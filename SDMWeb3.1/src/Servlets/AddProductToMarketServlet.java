package Servlets;

import JSObjects.CustomerLevelOrderJS;
import SDMCommon.SDManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/addProductToStore")
public class AddProductToMarketServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        try (
                PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String userName = SessionUtils.getUsername(req);
            String productName = req.getParameter("ProductName");
            String productCategory = req.getParameter("Category");
            String zoneName = req.getParameter("ZoneName");
            String storeMapString = req.getParameter("StoreMap");
            Type type = new TypeToken<Map<Integer, Double>>(){}.getType();
            Map<Integer,Double> storeMap = gson.fromJson(storeMapString,type);

            synchronized (this) {
                try {
                    sdManager.addProductToMarket(storeMap,productName, productCategory,zoneName);
                    out.println("Product was added.");
                    out.flush();
                }
                catch (Exception e){
                    out.println("Error "+e.getMessage()+" product wasn't added.");
                    out.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
