package com.geekhub.hw8;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/file/create")
public class CreateFileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newFile = req.getParameter("path") + req.getParameter("filename");
        Path newPath = Paths.get(newFile);
        String message = "";
        try {
            Files.createFile(newPath);
        } catch (FileAlreadyExistsException x) {
            message = "error: file already exists";
        } catch (IOException x) {
            message = "error: "+x;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/dir/view?dir="+newPath.getParent() +
                                                                    "&message="+message);
        rd.forward(req,resp);
    }
}
