package siddur.tool.cloud.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.common.security.DoNotAuthenticate;
import siddur.common.web.Action;
import siddur.common.web.ActionMapper.Result;

public class UtilAction extends Action{
	private static final int ICON_WIDTH = 64;
	private static final int ICON_HEIGHT = 64;
	private static final String WORDS = "words";
	private static final Color BACKGROUND_COLOR = new Color(0xF87219);
	private static final Color FORGROUND_COLOR = new Color(0xFFFFFF);
	
	private static final Color BACKGROUND = new Color(0xF4E8E8);
	private static final Color FORGROUND = new Color(0x18DD7B);
	private static final int WIDTH = 60;
	private static final int HEIGHT = 25;
	public static final String AUTHENTICODE = "authenticode";
	private static final char[] chars = new char[10 + 26 + 26];
	
	static{
		//48-57 
		//65-90
		//97-122
		int index = 0;
		for (int i = 48; i <= 57; i++) {
			chars[index++] = (char)i;
		}
		for (int i = 65; i <= 90; i++) {
			chars[index++] = (char)i;
		}
		for (int i = 97; i <= 122; i++) {
			chars[index++] = (char)i;
		}
	}
	
	@DoNotAuthenticate
	public Result icon(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String content = req.getParameter(WORDS);
		BufferedImage bi = new BufferedImage(ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D)bi.getGraphics();
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		g.setColor(FORGROUND_COLOR);
		draw(content, g);
		g.dispose();
		ImageIO.write(bi, "png", resp.getOutputStream());
		resp.getOutputStream().close();
		return Result.ok();
	}
	
	public Result checkCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String input = req.getParameter(AUTHENTICODE);
		String origin = (String)req.getSession().getAttribute(AUTHENTICODE);
		if(input != null && input.equalsIgnoreCase(origin)){
			return Result.ajax("1");
		}
		else{
			return Result.ajax("0");
		}
	}
	
	public Result authenticode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String authenticode = randomString();
		req.getSession().setAttribute(AUTHENTICODE, authenticode);
		BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D)bi.getGraphics();
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		g.setColor(FORGROUND);
		Font f = g.getFont();
		f = f.deriveFont(18f);
		g.setFont(f);
		drawString(g, authenticode);
		g.dispose();
		
		ImageIO.write(bi, "png", resp.getOutputStream());
		resp.getOutputStream().close();
		return Result.ok();
	}
	
	
	private static String randomString(){
		Random r = new Random();
		char[] c = new char[4];
		c[0] = chars[r.nextInt(chars.length)];
		c[1] = chars[r.nextInt(chars.length)];
		c[2] = chars[r.nextInt(chars.length)];
		c[3] = chars[r.nextInt(chars.length)];
		return new String(c);
	}
	
	private static void drawString(Graphics2D g, String s){
		int x = 5;
		int y = 20;
		Random r = new Random();
		for (char ch : s.toCharArray()) {
			boolean b = r.nextBoolean();
			float f = b ? 0.1f : -0.1f;
			y = b ? 17 : 23;
			g.setTransform(AffineTransform.getRotateInstance(f));
			g.drawString(ch + "", x, y);
			x += 13;
		}
	}
	
	private void draw(String content, Graphics2D g){
		Font f = g.getFont();
		FontRenderContext frc = g.getFontRenderContext();
		Rectangle2D rect = f.getStringBounds(content, frc);
		
		int x=0,y=0;
		x =(int)(ICON_WIDTH - rect.getWidth())/2;
		y =(int)(ICON_HEIGHT - rect.getHeight() + 15)/2;
		
		g.drawString(content, x, y);
	}

}
