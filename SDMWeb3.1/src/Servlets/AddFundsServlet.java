package Servlets;

import SDMCommon.SDMZoneInfo;
import SDMCommon.SDManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/addFunds")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class AddFundsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        SDManager sdmManager = ServletUtils.getSDMManager(getServletContext());
        String username = SessionUtils.getUsername(req);
        double amountToAdd;
        Date transActionDate;
        try {
            amountToAdd= Integer.parseInt(req.getParameter("fundsAmount"));
        }catch(Exception e){
            amountToAdd = 0;
        }
        try{
            transActionDate = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("addFundsDate"));
        }catch(Exception e){
            transActionDate = new Date();
        }
        sdmManager.getUsers().get(username).addFuds(amountToAdd,transActionDate);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDManager sdmManager = ServletUtils.getSDMManager(getServletContext());
        String username = SessionUtils.getUsername(req);
        double amountToAdd;
        Date transActionDate;
        try {
            amountToAdd= Integer.parseInt(req.getParameter("fundsAmount"));
        }catch(Exception e){
            amountToAdd = 0;
        }
        try{
            transActionDate = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("addFundsDate"));
        }catch(Exception e){
            transActionDate = new Date();
        }
        sdmManager.getUsers().get(username).addFuds(amountToAdd,transActionDate);
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();;
            String json = gson.toJson("sdf");
            out.println(json);
            out.flush();
        }
    }
}
