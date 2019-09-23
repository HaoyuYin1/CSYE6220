package part2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HeaderDisplayServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Enumeration<String> headerNames = req.getHeaderNames();
		PrintWriter printWriter = resp.getWriter();
		printWriter.print("<html>");
		printWriter.print("<body>");
		while(headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			Enumeration<String> header = req.getHeaders(name);
			String content = header.nextElement();
			printWriter.println(name + " : " + content + "<br>");
		}
		printWriter.print("</body>");
		printWriter.print("</html>");
		
		
	}
}
