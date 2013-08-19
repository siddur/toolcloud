package siddur.tool.cloud;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.common.miscellaneous.Constants;
import siddur.tool.core.LogCache;

public class ConsoleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		
		LogCache logCache = LogCache.getLogCache(req.getParameter(Constants.TICKET));
		if(logCache != null){
			List<String> logs = logCache.getLogs();
			StringBuilder sb = new StringBuilder();
			for(String log : logs){
				sb.append("<div>");
				sb.append(log);
				sb.append("</div>");
			}
			OutputStream o = resp.getOutputStream();
			o.write(sb.toString().getBytes());
			o.flush();
			o.close();
		}
	}
}
