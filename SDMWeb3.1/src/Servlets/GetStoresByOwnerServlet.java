package Servlets;

import ProductTypes.StoreProductInfo;
import SDMCommon.SDManager;
import SDMCommon.Store;
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
import java.util.List;

@WebServlet("/getStoresByOwner")
public class GetStoresByOwnerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            String ownerName = SessionUtils.getUsername(req);
            String zoneName = req.getParameter("ZoneName");
            if (zoneName == null) {
                resp.sendError(-1, "Zone name not valid.");
            } else {
                List<Store> storeInfosList = sdManager.getStoreByOwner(ownerName, zoneName);
                String json = gson.toJson(storeInfosList);
                out.println(json);
                out.flush();
            }
        } catch (Exception e) {

        }
    }
}
