package Servlets;

import SDMCommon.SDManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import javafx.application.Application;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {

    public static final String USERNAME = "username";
    private final String LogInErrorPage = "/Pages/loginError/loginError.html";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        SDManager manager = ServletUtils.getSDMManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        if (usernameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                response.sendRedirect("index.html");
            } else {
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (!manager.isUsernameAvailable(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        request.setAttribute("username_error", errorMessage);
                        getServletContext().getRequestDispatcher(LogInErrorPage).forward(request, response);
                       // response.sendRedirect(LogInErrorPage);
                        Gson gson = new Gson();
                        String jsonResponse = gson.toJson(errorMessage);
                        try (PrintWriter out = response.getWriter()) {
                            out.print(jsonResponse);
                            out.flush();
                        }
                    } else {
                        //add the new user to the users list

                        String role = request.getParameter("roleName");
                        if(role == null) {
                            response.sendRedirect("index.html");
                        }
                        else {
                            manager.addUser(usernameFromParameter, role);
                            request.getSession(true).setAttribute(USERNAME, usernameFromParameter);

                            response.sendRedirect("Pages/mainWindow/mainPage.html");
                            Gson gson = new Gson();
                            String jsonResponse = gson.toJson("");
                            try (PrintWriter out = response.getWriter()) {
                                out.print(jsonResponse);
                                out.flush();
                            }
                        }
                    }
                }
            }
        } else {
            response.sendRedirect("Pages/mainWindow/mainPage.html");
        }
    }


}
