package com.geekhub.hw8;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/file/view")
public class ViewFileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String fileName = req.getParameter("filename");

        sb.append("<html>");
        sb.append("<h1>File viewer:</h1>");
        if (fileName!=null){
            Path path = Paths.get(fileName);
            sb.append(String.format("<a href='view?dir=%s'>%s</a>",
                                        path.getParent().toString(),
                                        "[exit]"));
            sb.append("<br>");
            if (Files.exists(path) && Files.isReadable(path)) {
                List<String> fileContent = Files.readAllLines(path, Charset.defaultCharset());
                String text = fileContent.stream()
                        .collect(Collectors.joining());
                sb.append("<textarea name='textarea' rows=10 cols=50>" + text + "</textarea>");
            } else {
                String message = "file: <i>" + fileName  + "</i> read failed.";
                RequestDispatcher rd = req.getRequestDispatcher("/dir/view?dir="+path.getParent().toString()
                                                                        +"&message="+message);
                rd.forward(req,resp);
            }
        }

        sb.append("</html>");
        resp.getWriter().write(sb.toString());
    }

}
