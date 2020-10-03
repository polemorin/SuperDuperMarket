package Servlets;

import SDMCommon.MarketArea;
import SDMCommon.SDManager;
import SDMCommon.User;
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
import java.util.Map;

@WebServlet("/goToZone")
public class GoToZoneServlet extends HttpServlet {

        public static final String USERNAME = "username";
        private final String ErrorPage = "/Pages/mainWindow/mainPage.html";
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            response.setContentType("application/json");

            SDManager manager = ServletUtils.getSDMManager(getServletContext());
            String zoneName= request.getParameter("id");

            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
                MarketArea marketArea = sdManager.getMarketArea(zoneName);
                String json = gson.toJson(marketArea);
                out.println(json);
                out.flush();
            }


        }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        SDManager manager = ServletUtils.getSDMManager(getServletContext());
        String zoneName= request.getParameter("id");

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            SDManager sdManager = ServletUtils.getSDMManager(getServletContext());
            MarketArea marketArea = sdManager.getMarketArea(zoneName);
            String json = gson.toJson(marketArea);
            out.println(json);
            out.flush();
        }


    }


    }

