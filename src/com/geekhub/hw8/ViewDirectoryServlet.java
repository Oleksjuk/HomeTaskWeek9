package com.geekhub.hw8;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/dir/view", initParams = {
        @WebInitParam(name = "root", value = "D:\\")
})
public class ViewDirectoryServlet extends HttpServlet {

    private static Path ROOT_PATH;
    private static Path NEW_PATH;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ROOT_PATH = Paths.get(config.getInitParameter("root"));
        NEW_PATH = ROOT_PATH;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");

        String dir = req.getParameter("dir") != null ? req.getParameter("dir") : "";
        if (!dir.isEmpty()) {
            NEW_PATH = Paths.get(dir);
            ROOT_PATH = NEW_PATH.getParent() != null ? NEW_PATH.getParent() : NEW_PATH;
        }

        if (null != NEW_PATH.getParent()) {
            appendLink(sb, "...", NEW_PATH.getParent());
            sb.append("<br>");
        }

        if (!Files.isDirectory(NEW_PATH)) {
            sb.append("</html>");
            RequestDispatcher rd = req.getRequestDispatcher("/file/view?filename=" + NEW_PATH);
            rd.forward(req, resp);
        } else {
            getSortedDirAndFiles(NEW_PATH).stream()
                    .forEach(path -> {
                        appendLink(sb, path.getFileName().toString(), path);
                        appendDeleteLink(sb, path);
                        sb.append("<br>");
                    });
        }

        addMessageIfExist(sb, req);
        addFileCreationForm(sb);

        sb.append("</html>");
        resp.getWriter().write(sb.toString());
    }

    private List<Path> getSortedDirAndFiles(Path path) throws IOException {
        List<Path> sortedItem = new ArrayList<>();
        Files.list(path)
                .sorted()
                .filter(path0 -> Files.isDirectory(path0))
                .forEach(sortedItem::add);

        Files.list(path)
                .sorted()
                .filter(path0 -> !Files.isDirectory(path0))
                .forEach(sortedItem::add);

        return sortedItem;
    }

    private void addFileCreationForm(StringBuilder sb) {
        sb.append("<form action='/file/create'>");
        sb.append("<input type='hidden' name='path' value='" + ROOT_PATH + "'/>");
        sb.append("<input type='text' name='filename' placeholder='file name'/>");
        sb.append("<input type='submit' value='create file'/>");
        sb.append("</form>");
    }

    private void addMessageIfExist(StringBuilder sb, HttpServletRequest req) {
        String message = req.getParameter("message");
        if (message != null && !message.isEmpty()) {
            sb.append("message: <i>" + message + "</i>");
        }
    }

    private void appendLink(StringBuilder sb, String text, Path path) {
        if (Files.isDirectory(path)) {
            text = String.format("[%s]", text);
        }
        sb.append(String.format("<a href='/dir/view?dir=%s'>%s</a>", path.toString(), text));
    }

    private void appendDeleteLink(StringBuilder sb, Path path) {
        sb.append(String.format("<a href='/file/remove?filename=%s' title='delete'>[x]</a>", path.toString()));
    }

}
