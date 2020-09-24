package Servlets;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "testServlet")
public class testServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            String msg = request.getParameter("username") + " this is post request";
            System.out.println(msg);
            String json = gson.toJson(msg);
            out.println(json);
            out.flush();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            String msg = request.getParameter("username") + " this is get request";
            System.out.println(msg);
            String json = gson.toJson(msg);
            out.println(json);
            out.flush();
        }
    }
}
