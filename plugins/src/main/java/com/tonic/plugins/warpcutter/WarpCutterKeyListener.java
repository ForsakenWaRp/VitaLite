package com.tonic.plugins.warpcutter;

import net.runelite.client.input.KeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

public class WarpCutterKeyListener implements KeyListener {
    @Inject
    private WarpCutterPlugin plugin;

    @Override
    public void keyTyped(KeyEvent e)
    {
        // Handle space bar for pause/resume
        if (e.getKeyChar() == ' ')
        {
           // plugin.togglePause();
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        // Handle F1 for paint toggle
        if (e.getKeyCode() == KeyEvent.VK_F1)
        {
            plugin.togglePaint();
            e.consume();
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        // Not needed
    }
}