package com.tonic.plugins.warpcutter;

import com.google.inject.Provides;
import com.tonic.Logger;
import com.tonic.api.entities.TileObjectAPI;
import com.tonic.api.threaded.Delays;
import com.tonic.api.widgets.BankAPI;
import com.tonic.api.widgets.InventoryAPI;
import com.tonic.data.wrappers.ItemContainerEx;
import com.tonic.services.ClickManager;
import com.tonic.services.breakhandler.BreakHandler;
import com.tonic.services.pathfinder.Walker;
import com.tonic.util.VitaPlugin;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.gameval.InventoryID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;

@PluginDescriptor(
        name = "Warp Cutter",
        description = "A simple woodcutting plugin.",
        tags = {"warp", "cutter", "woodcutting", "wc"}
)
public class WarpCutterPlugin extends VitaPlugin
{

    @Inject
    private Client client;

    @Inject
    private WarpCutterConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private WarpCutterOverlay overlay;

    @Inject
    private WarpCutterMouseListener mouseListener;
    
    @Inject
    private MouseManager mouseManager;

    @Inject
    BreakHandler breakHandler;
    
    @Provides
    WarpCutterConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(WarpCutterConfig.class);
    }

	public final String version = "0.2.1";
    public int logsCut = 0;
	public int expGained = 0;
	public StopWatch stopWatch;
    public String status = "Initializing...";
    public String loc = "Unknown";
    public String tree = "Unknown";
    public boolean paintExpanded = true;
    public Rectangle toggleButtonBounds;
    public Rectangle closeButtonBounds;
    List<String> actions = List.of("Chop", "Chop down");
    private WorldPoint bank;
    private WorldPoint treeArea;
    
    @Override
    protected void startUp()
    {
        if (overlayManager != null)
        {
            overlayManager.add(overlay);
        }
        if (mouseManager != null)
        {
            mouseManager.registerMouseListener(mouseListener);
        }
        if (client.getGameState() == GameState.LOGGED_IN)
        {
            stopWatch = StopWatch.start();
            updateLocationPoints();
        }
        breakHandler.register(this);
        startBreaks();
    }

@Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        if (mouseManager != null)
        {
            mouseManager.unregisterMouseListener(mouseListener);
        }
        stopBreaks();
    }

    @Subscribe
    private void onChatMessage(ChatMessage m)
    {
        if (m.getMessage().contains("You get some"))
        {
            logsCut++;
            expGained += (int) config.treeType().getExp();
        }
    }
    
    private void updateLocationPoints()
    {
        if (config == null) return;
        
        Location location = config.location();
        if (location == null) return;
        
        bank = location.getBankPoint();
        Trees selectedTree = config.treeType();
        treeArea = location.getTreeLocation(selectedTree);
        loc = location.getName();
        tree = selectedTree.getName();
    }
    
    private String getBankNameForLocation()
    {
        Location location = config.location();
        switch (location) {
            case WOODCUTTING_GUILD:
            case HOSIDIUS_HOUSE:
                return "Bank chest";
            default:
                return "Bank booth";
        }
    }
    
    public void togglePaint()
    {
        paintExpanded = !paintExpanded;
    }
        
    public void hidePaint()
    {
        paintExpanded = false;
    }
    
    

@Override
    public void loop()
    {
        var player = client.getLocalPlayer();
        
        if (player == null || client.getGameState() != GameState.LOGGED_IN)
        {
            status = "Not logged in";
            return;
        }

        if (player.getAnimation() != -1) {
            return;
        }
        
        Location location = config.location();
        Trees selectedTree = config.treeType();
        
        if (!location.hasTree(selectedTree)) {
            Trees firstAvailable = location.getAvailableTrees().get(0);
            status = "Auto-switched to " + firstAvailable.getName() + " (trees not available at " + location.getName() + ")";
            return;
        }

        if (config.powerCut()) {
            status = "Dropping logs";
            ItemContainerEx container = new ItemContainerEx(InventoryID.INV);
           var logs = container.getAll(config.treeType().getItemID());
            for (var log : logs) {
                ClickManager.queueClickBox(log.getClickBox());
                InventoryAPI.interact(log, "Drop");
                Delays.wait(100);
            }
            return;
        }
        
        if (!config.powerCut())
        {
            
            if (!InventoryAPI.isFull() && player.getWorldLocation().distanceTo(treeArea) >= 5)
            {
                status = "Walking to " + selectedTree.getName() + " trees";
                Walker.walkTo(treeArea);
                return;
            }
            
            if (InventoryAPI.isFull())
            {
                if (BankAPI.isOpen() && InventoryAPI.contains(config.treeType().getItemID()))
                {
                    status = "Banking " + config.treeType().getName();
                    var item = InventoryAPI.getItem(config.treeType().getItemID());
                    ClickManager.queueClickBox(item.getClickBox());
                    //Keep this 28 Stupid AI else it keeps 1 Item.
                    BankAPI.deposit(config.treeType().getItemID(), 28);
                    Delays.waitUntil(() ->!InventoryAPI.contains(config.treeType().getItemID()), 5);
                    return;
                }
                
                if (player.getWorldLocation().distanceTo(bank) <= 5 && !BankAPI.isOpen())
                {
                    var bankObject = TileObjectAPI.search()
                            .withName(getBankNameForLocation())
                            .nearest();

                    status = "Finding bank";
                    if (bankObject != null && bankObject.hasAction("Bank"))
                    {
                        status = "Opening bank";
                        ClickManager.queueClickBox(bankObject.getShape());
                        TileObjectAPI.interact(bankObject, "Bank");
                        Delays.waitUntil(BankAPI::isOpen, 5);
                        return;
                    }
                }
                
                if (player.getWorldLocation().distanceTo(bank) > 5)
                {
                    status = "Walking to bank";
                    Walker.walkTo(bank);
                    return;
                }
            }
        }
        
        status = "Finding tree";
        updateLocationPoints();
        if (config.hardwoodPatch())
        {
            var patchIds = List.of(30480, 30481, 30482);
            var patch = TileObjectAPI.get(x -> patchIds.contains(x.getId()) &&
                   x.distanceTo(player.getWorldLocation()) <= config.treeRadius());
            
            if (patch != null)
            {
                status = "Chopping " + config.treeType().getName();
                ClickManager.queueClickBox(patch.getShape());
                TileObjectAPI.interact(patch, 0);
                Delays.waitUntil(player::isInteracting, 3);
                return;
            }
            return;
        }
        
        var tree = TileObjectAPI.search()
                .withName(config.treeType().getObjectName())
                .within(config.treeRadius())
                .nearest();
        
        if (tree != null && actions.contains(tree.getActions()[0])) {
            
            status = "Chopping " + tree.getName();
            ClickManager.queueClickBox(tree.getShape());
            TileObjectAPI.interact(tree, "Chop down");
            Delays.waitUntil(player::isInteracting, 3);
        }
    }
    
    public void startBreaks()
    {
        breakHandler.start(this);
    }
    
    public void stopBreaks()
    {
        breakHandler.stop(this);
    }
}