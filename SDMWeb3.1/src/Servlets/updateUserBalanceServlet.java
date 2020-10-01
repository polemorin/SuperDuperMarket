package Servlets;

import SDMCommon.SDManager;
import Utils.ServletUtils;
import Utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/updateBalance")
public class updateUserBalanceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        SDManager sdmManager = ServletUtils.getSDMManager(getServletContext());
        String username = SessionUtils.getUsername(req);
        double currentBalance =sdmManager.getUsers().get(username).getFunds();
        try(PrintWriter out = resp.getWriter()){
            out.println(currentBalance);
            out.flush();
        }catch (Exception ignored){

        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        SDManager sdmManager = ServletUtils.getSDMManager(getServletContext());
        String username = SessionUtils.getUsername(req);
        double currentBalance =sdmManager.getUsers().get(username).getFunds();
        try(PrintWriter out = resp.getWriter()){
            out.println(currentBalance);
            out.flush();
        }catch (Exception ignored){

        }
    }
}
