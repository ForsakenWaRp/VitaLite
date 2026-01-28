package com.tonic.plugins.warpcutter;

import net.runelite.client.input.MouseListener;

import javax.inject.Inject;
import java.awt.event.MouseEvent;

public class WarpCutterMouseListener implements MouseListener {
    @Inject
    private WarpCutterPlugin plugin;

    @Override
    public MouseEvent mouseClicked(MouseEvent e)
    {
        if (plugin.toggleButtonBounds != null && plugin.toggleButtonBounds.contains(e.getPoint()))
        {
            plugin.togglePaint();
            e.consume();
        }
        else if (plugin.closeButtonBounds != null && plugin.closeButtonBounds.contains(e.getPoint()))
        {
            plugin.hidePaint();
            e.consume();
        }
        return e;
    }
    
    @Override
    public MouseEvent mousePressed (MouseEvent mouseEvent) {
		return mouseEvent;
	}
    
    @Override
    public MouseEvent mouseReleased (MouseEvent mouseEvent) {
        return mouseEvent;
    }
    
    @Override
    public MouseEvent mouseEntered (MouseEvent mouseEvent) {
        return mouseEvent;
    }
    
    @Override
    public MouseEvent mouseExited (MouseEvent mouseEvent) {
        return mouseEvent;
    }
    
    @Override
    public MouseEvent mouseDragged (MouseEvent mouseEvent) {
        return mouseEvent;
    }
    
    @Override
    public MouseEvent mouseMoved (MouseEvent mouseEvent) {
        return mouseEvent;
    }
}