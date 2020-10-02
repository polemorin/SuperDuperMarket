package Servlets;

import SDMCommon.SDMZoneInfo;
import SDMCommon.SDManager;
import SDMCommon.Transaction;
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

@WebServlet("/updateTransactions")
public class updateTransactionTableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            List<Transaction> transactionTableValues = sdManager.getallTransactions(SessionUtils.getUsername(req));
            String json = gson.toJson(transactionTableValues);
            out.println(json);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            List<Transaction> transactionTableValues = sdManager.getallTransactions(SessionUtils.getUsername(req));
            String json = gson.toJson(transactionTableValues);
            out.println(json);
            out.flush();
        }
    }
}
