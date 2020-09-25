package Servlets;

import SDMCommon.SDManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {

    public static final String USERNAME = "username";
    private final String SIGN_UP_URL = "index.html";
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
                response.sendRedirect(SIGN_UP_URL);
            } else {
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (!manager.isUsernameAvailable(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

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
                            response.sendRedirect(SIGN_UP_URL);
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
// response.setContentType("application/json");
//         SDManager manager = ServletUtils.getSDMManager(getServletContext());
//         String usernameFromSession = null; //SessionUtils.getUsername(request);
//         if (usernameFromSession == null) {
//         //user is not logged in yet
//         String usernameFromParameter = request.getParameter(USERNAME);
//         if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
//         //no username in session and no username in parameter -
//         //redirect back to the index page
//         //this return an HTTP code back to the browser telling it to load
//         response.sendRedirect(SIGN_UP_URL);
//         } else {
//         //normalize the username value
//         usernameFromParameter = usernameFromParameter.trim();
//
//synchronized (this) {
//        if (true) { //!manager.isUsernameAvailable(usernameFromParameter)
//        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
//        // request.setAttribute("username error", errorMessage);
//        // getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
//        Gson gson = new Gson();
//        String jsonResponse = gson.toJson(errorMessage);
//        try (PrintWriter out = response.getWriter()) {
//        out.print(jsonResponse);
//        out.flush();
//        }
//        } else {
//        //add the new user to the users list
//        //manager.addUser(usernameFromParameter);
//        //set the username in a session so it will be available on each request
//        //the true parameter means that if a session object does not exists yet
//        //create a new one
//        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
//
//        //redirect the request to the chat room - in order to actually change the URL
//        response.sendRedirect("Pages/mainPage.html");
//        Gson gson = new Gson();
//        String jsonResponse = gson.toJson("");
//        try (PrintWriter out = response.getWriter()) {
//        out.print(jsonResponse);
//        out.flush();
//        }
//        }
//        }
//        }
//        } else {
//        //user is already logged in
//        response.sendRedirect("Pages/mainPage.html");
//        }