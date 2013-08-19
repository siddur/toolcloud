package siddur.tool.core;

/**
 *	windows:\r\n
 *	linux: \n  
 */
public class NewLineFilter implements LogFilter{

	@Override
	public String doFilter(String log) {
		return log.replace("\r\n", "\n").replace("\n", "<br/>");
	}

}
