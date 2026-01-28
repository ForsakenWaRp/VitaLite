package com.tonic.plugins.warpcutter;

import net.runelite.api.ItemID;

public enum Trees
{
    TREE("Tree", ItemID.LOGS, "Tree", 25),
    OAK("Oak", ItemID.OAK_LOGS, "Oak tree", 37.5),
    WILLOW("Willow", ItemID.WILLOW_LOGS, "Willow tree", 67.5),
    MAPLE("Maple", ItemID.MAPLE_LOGS, "Maple tree", 100),
    TEAK("Teak", ItemID.TEAK_LOGS, "Teak tree", 85),
    MAHOGANY("Mahogany", ItemID.MAHOGANY_LOGS, "Mahogany tree",125),
    YEW("Yew", ItemID.YEW_LOGS, "Yew tree", 175),
    MAGIC("Magic", ItemID.MAGIC_LOGS, "Magic tree",250),
    REDWOOD("Redwood", ItemID.REDWOOD_LOGS, "Redwood tree",380);

    private final String name;
    private final int itemID;
    private final String objectName;
    private final double exp;

    Trees(String name, int itemID, String objectName, double exp) {
        this.name = name;
        this.itemID = itemID;
        this.objectName = objectName;
        this.exp = exp;
    }

    public String getName() {
        return name;
    }

    public int getItemID() {
        return itemID;
    }

    public String getObjectName() {
        return objectName;
    }
    
    public double getExp () {
        return exp;
    }
    
}