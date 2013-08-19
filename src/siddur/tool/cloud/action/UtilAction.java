package siddur.tool.cloud.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
