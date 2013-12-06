package siddur.tool.jetty;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.webapp.WebAppContext;


import siddur.common.miscellaneous.Constants;
import siddur.common.util.FileSystemUtil;


public class JettyStart {
	public static void main(String[] args) throws Exception {
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "true");
		
		int port = 80;
		if(args.length > 0){
			if(StringUtils.isNumeric(args[0])){
				port = Integer.parseInt(args[0]);
				FileSystemUtil.setHome(new File(args[1]));
			}
			else{
				FileSystemUtil.setHome(new File(args[0]));
			}
		}
		
		WebAppContext app = new WebAppContext();
		String webroot = "web";
		app.setResourceBase(webroot);
		app.setDescriptor(webroot + "/WEB-INF/web.xml");
		
		RequestLogHandler requestLogHandler = new RequestLogHandler();
		NCSARequestLog requestLog = new NCSARequestLog("./logs/request-yyyy_mm_dd.log");
		requestLog.setRetainDays(90);
		requestLog.setAppend(true);
		requestLog.setExtended(false);
		requestLog.setLogTimeZone("GMT+8");
		requestLogHandler.setRequestLog(requestLog);
		app.setHandler(requestLogHandler);
		
		//must not change context path:Constants.WEBSITE_ROOT
		app.setContextPath(Constants.WEBSITE_ROOT);
		
		ResourceHandler rh = new ResourceHandler();
		rh.setDirectoriesListed(true);
		rh.setResourceBase(FileSystemUtil.getFileServer().getCanonicalPath());
		rh.setStylesheet("");
		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[]{app, rh});
		
		Server server = new Server(port);
		server.setHandler(handlers);
		
		server.start();
		server.join();
	}
}
