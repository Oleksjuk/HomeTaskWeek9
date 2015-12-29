package com.geekhub.hw8;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.*;

@WebServlet("/file/remove")
public class RemoveFileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getParameter("filename");
        if (null!=fileName && !fileName.isEmpty()) {
            Path removeFile = Paths.get(fileName);
            String message = "";
            try {
                Files.delete(removeFile);
            } catch (NoSuchFileException e) {
                message = "error: no such file";
            } catch (DirectoryNotEmptyException e) {
                message = "error: directory not empty";
            } catch (IOException e) {
                message = "error: file permission problems";
            }
            RequestDispatcher rd = req.getRequestDispatcher("/dir/view?dir="+removeFile.getParent() +
                                                            "&message="+message);
            rd.forward(req,resp);
        }
    }
}
