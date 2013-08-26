package siddur.tool.core;

/**
 *	windows:\r\n
 *	linux: \n  
 */
public class EscapeFilter implements LogFilter{

	@Override
	public String doFilter(String log) {
		return log.replace("\r\n", "\n")
				.replace("\n", "<br/>")
				.replace(" ", "&nbsp;")
				.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;");
	}

}
