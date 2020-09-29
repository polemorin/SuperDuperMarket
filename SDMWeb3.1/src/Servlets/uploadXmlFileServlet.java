package Servlets;

import SDMCommon.SDManager;
import Utils.ServletUtils;
import Utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.servlet.http.Part;

@WebServlet("/loadFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class uploadXmlFileServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();
        String username = SessionUtils.getUsername(request);
        Part name = request.getPart("mapName");
        String nameValue = readFromInputStream(name.getInputStream());

        SDManager manager = ServletUtils.getSDMManager(getServletContext());

        try {
            manager.loadInfoFromXML(fileContent,username);
            response.getOutputStream().println("File load successfully!");
        } catch (Exception e) {
            e.getMessage();
            response.getOutputStream().println(e.getMessage());
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();
        String username = SessionUtils.getUsername(request);

        SDManager manager = ServletUtils.getSDMManager(getServletContext());

        try {
            manager.loadInfoFromXML(fileContent,username);
            response.getOutputStream().println("File load successfully!");
        } catch (Exception e) {
            e.getMessage();
            response.getOutputStream().println(e.getMessage());
        }

    }
    private String readFromInputStream(InputStream inputStream) {

        return new Scanner(inputStream).useDelimiter("\\Z").next();

    }
}
