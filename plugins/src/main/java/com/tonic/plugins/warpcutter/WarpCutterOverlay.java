package com.tonic.plugins.warpcutter;

import com.tonic.api.widgets.WidgetAPI;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class WarpCutterOverlay extends Overlay
{

	private final WarpCutterPlugin plugin;

	private static final Font TITLE_FONT = FontManager.getRunescapeFont().deriveFont(Font.BOLD, 20);
	private static final Font STATS_FONT = FontManager.getRunescapeFont().deriveFont(Font.PLAIN, 16);
	private static final Color ACCENT_COLOR = new Color(255, 214, 0);
	private static final Color BACKGROUND_COLOR = new Color(20, 20, 30, 220);
	private static final Color BORDER_COLOR = new Color(100, 100, 120, 200);
	private static final Color PROGRESS_COLOR = new Color(46, 213, 115);
	private static final Color WARNING_COLOR = new Color(255, 107, 107);
	
	private long animationTime = 0;
	private final float[] pulseValues = new float[6];

    @Inject
    private WarpCutterOverlay(WarpCutterPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(OverlayPriority.HIGHEST);
    }


	@Override
	public Dimension render(Graphics2D graphics)
	{
		animationTime = System.currentTimeMillis();
		updateAnimations();
		renderStats(graphics);
		return null;
	}

	private void renderStats(Graphics2D graphics)
	{
		if (plugin.stopWatch == null) return;
		
		String titleString = "🌳 Warp Chopper V" + plugin.version;
		String locationString = "📍 Location: " + plugin.loc;
	//	String treeString = "🪲 Tree: " + plugin.tree;
		String runtimeString = "⏱ Runtime: " + plugin.stopWatch.toElapsedString();
		String statusString = "🎯 Status: " + plugin.status;
		String logsString = "📦  Logs: " + plugin.logsCut + " (" + plugin.stopWatch.getHourlyRate(plugin.logsCut) + "/hr)";
		String expString = "✨ Experience: " + plugin.expGained + " (" + plugin.stopWatch.getHourlyRate(plugin.expGained) + "/hr)";
		
		Rectangle slotBounds = null;
		Widget box = WidgetAPI.get(162, 34);

		if (box != null)
		{
			slotBounds = new Rectangle(box.getBounds());
			
			int buttonX = slotBounds.x;
			int buttonY = slotBounds.y;
			int buttonWidth = 110;
			int buttonHeight = 25;
			plugin.toggleButtonBounds = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
			
			if (!plugin.paintExpanded)
			{
				renderToggleButton(graphics, true);
				return;
			}
			
			renderToggleButton(graphics, false);
			
			int width = 260;
			int height = 210;
			
			// Position paint at slotBounds.x with bottom at +1
			int x = slotBounds.x;
			int y = slotBounds.y + slotBounds.height - height + 1; // bottom of paint is +1 from box bottom
			
			Rectangle bounds = new Rectangle(x, y, width, height);
		
			renderModernOverlay(graphics, bounds, titleString, locationString, runtimeString, statusString, logsString, expString);
		}
	}
	
	private void renderToggleButton(Graphics2D graphics, boolean isCollapsed)
	{
		Rectangle bounds = plugin.toggleButtonBounds;
		
		// Button background
		Color bgColor = isCollapsed ? new Color(20, 20, 30, 200) : new Color(255, 214, 0, 100);
		graphics.setColor(bgColor);
		graphics.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 8, 8);
		
		// Button border
		graphics.setColor(isCollapsed ? ACCENT_COLOR : new Color(100, 100, 100));
		graphics.setStroke(new BasicStroke(2f));
		graphics.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 8, 8);
		
		// Button text
		graphics.setFont(TITLE_FONT.deriveFont(Font.PLAIN, 12));
		FontMetrics fm = graphics.getFontMetrics();
		String text = isCollapsed ? "▶ Warp Cutter" : "▼ Warp Cutter";
		int textWidth = fm.stringWidth(text);
		int textX = bounds.x + (bounds.width - textWidth) / 2;
		int textY = bounds.y + (bounds.height - fm.getHeight()) / 2 + fm.getAscent();
		
		graphics.setColor(Color.WHITE);
		graphics.drawString(text, textX, textY);
	}
	
	private void renderModernOverlay(Graphics2D graphics, Rectangle bounds, String title, String location, String runtime, String status, String logs, String exp)
	{
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// Draw modern rounded background with gradient
		RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
			bounds.x, bounds.y, bounds.width, bounds.height, 15, 15
		);
		
		GradientPaint gradient = new GradientPaint(
			bounds.x, bounds.y, new Color(30, 30, 45, 240),
			bounds.x, bounds.y + bounds.height, new Color(15, 15, 25, 240)
		);
		graphics.setPaint(gradient);
		graphics.fill(roundedRect);
		
		// Draw border with glow effect
		graphics.setStroke(new BasicStroke(2f));
		graphics.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 100));
		graphics.draw(roundedRect);
		
		// Set close button bounds (top right corner)
		int closeX = bounds.x + bounds.width - 20;
		int closeY = bounds.y + 5;
		int closeSize = 20;
		plugin.closeButtonBounds = new Rectangle(closeX, closeY, closeSize, closeSize);
		
		// Draw close button
		graphics.setColor(new Color(255, 107, 107));
		graphics.fillOval(closeX, closeY, closeSize, closeSize);
		graphics.setColor(Color.WHITE);
		graphics.setFont(TITLE_FONT.deriveFont(Font.BOLD, 10));
		FontMetrics closeMetrics = graphics.getFontMetrics();
		int closeTextX = closeX + (closeSize - closeMetrics.stringWidth("X")) / 2;
		int closeTextY = closeY + (closeSize - closeMetrics.getHeight()) / 2 + closeMetrics.getAscent();
		graphics.drawString("X", closeTextX, closeTextY);
		
		// Draw title with special styling
		graphics.setFont(TITLE_FONT);
		FontMetrics titleMetrics = graphics.getFontMetrics();
		int titleWidth = titleMetrics.stringWidth(title);
		int titleX = bounds.x + (bounds.width - titleWidth) / 2;
		int titleY = bounds.y + 30;
		
		// Title shadow
		graphics.setColor(new Color(0, 0, 0, 150));
		graphics.drawString(title, titleX + 2, titleY + 2);
		
		// Animated title color
		Color animatedColor = getAnimatedColor(ACCENT_COLOR, pulseValues[0]);
		graphics.setColor(animatedColor);
		graphics.drawString(title, titleX, titleY);
		
		// Draw separator line
		graphics.setStroke(new BasicStroke(1f));
		GradientPaint lineGradient = new GradientPaint(
			bounds.x + 20, titleY + 10, new Color(100, 100, 120, 100),
			bounds.x + bounds.width - 20, titleY + 10, new Color(50, 50, 70, 100)
		);
		graphics.setPaint(lineGradient);
		graphics.drawLine(bounds.x + 20, titleY + 10, bounds.x + bounds.width - 20, titleY + 10);
		
		// Draw stats with progress indicators
		graphics.setFont(STATS_FONT);
		int textX = bounds.x + 20;
		int textY = titleY + 35;
		int lineHeight = 22;
		
		renderStatLine(graphics, location, textX, textY, new Color(100, 150, 255), pulseValues[0]);
		textY += lineHeight;
		
//		renderStatLine(graphics, tree, textX, textY, new Color(255, 150, 100), pulseValues[1]);
//		textY += lineHeight;
		
		renderStatLine(graphics, status, textX, textY, getStatusColor(), pulseValues[2]);
		textY += lineHeight;
		
		renderStatLine(graphics, runtime, textX, textY, new Color(150, 150, 200), pulseValues[3]);
		textY += lineHeight;
		
		renderStatLine(graphics, logs, textX, textY, PROGRESS_COLOR, pulseValues[4]);
		textY += lineHeight;
		
		renderStatLine(graphics, exp, textX, textY, new Color(255, 195, 0), pulseValues[5]);
		
		// Draw mini progress bar at bottom
		//renderProgressBar(graphics, bounds.x + 20, bounds.y + bounds.height - 15, bounds.width - 40, 4);
	}
	
	private void renderStatLine(Graphics2D graphics, String text, int x, int y, Color baseColor, float pulse)
	{
		// Shadow effect
		graphics.setColor(new Color(0, 0, 0, 120));
		graphics.drawString(text, x + 1, y + 1);
		
		// Main text with animated color
		Color animatedColor = getAnimatedColor(baseColor, pulse);
		graphics.setColor(animatedColor);
		graphics.drawString(text, x, y);
	}
	
	private void renderProgressBar(Graphics2D graphics, int x, int y, int width, int height)
	{
		// Background
		graphics.setColor(new Color(50, 50, 60, 180));
		graphics.fillRect(x, y, width, height);
		
		// Animated progress
		float progress = ((animationTime / 1000f) % 10) / 10f;
		int progressWidth = (int) (width * progress);
		
		GradientPaint progressGradient = new GradientPaint(
			x, y, PROGRESS_COLOR,
			x + progressWidth, y, new Color(100, 255, 150)
		);
		graphics.setPaint(progressGradient);
		graphics.fillRect(x, y, progressWidth, height);
	}
	
	private Color getStatusColor()
	{
		String status = plugin.status.toLowerCase();
		if (status.contains("chopping") || status.contains("finding")) {
			return PROGRESS_COLOR;
		} else if (status.contains("walking") || status.contains("banking")) {
			return new Color(255, 195, 0);
		} else if (status.contains("error") || status.contains("not logged")) {
			return WARNING_COLOR;
		}
		return ACCENT_COLOR;
	}
	
	private Color getAnimatedColor(Color base, float pulse)
	{
		int r = Math.min(255, base.getRed() + (int)(30 * pulse));
		int g = Math.min(255, base.getGreen() + (int)(30 * pulse));
		int b = Math.min(255, base.getBlue() + (int)(30 * pulse));
		return new Color(r, g, b);
	}
	
	private void updateAnimations()
	{
		float time = animationTime / 1000f;
		for (int i = 0; i < pulseValues.length; i++) {
			pulseValues[i] = (float) Math.sin(time * 2 + i * 0.5) * 0.5f + 0.5f;
		}
	}
}
