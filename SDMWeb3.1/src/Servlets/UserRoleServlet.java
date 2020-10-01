package Servlets;

import SDMCommon.SDManager;
import SDMCommon.StoreOwner;
import Utils.ServletUtils;
import Utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/userRole")
public class UserRoleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
        String userRole;
        if(sdManager.getUsers().get(SessionUtils.getUsername(req)) instanceof StoreOwner){
            userRole = "StoreOwner";
        }else{
            userRole = "Customer";
        }
        try(PrintWriter out = resp.getWriter()){
            out.print(userRole);
            out.flush();
        }catch (Exception ignored){

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
        String userRole;
        if(sdManager.getUsers().get(SessionUtils.getUsername(req)) instanceof StoreOwner){
            userRole = "StoreOwner";
        }else{
            userRole = "Customer";
        }
        try(PrintWriter out = resp.getWriter()){
            out.println(userRole);
            out.flush();
        }catch (Exception ignored){

        }
    }
}
