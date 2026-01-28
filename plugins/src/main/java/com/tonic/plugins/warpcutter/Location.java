package com.tonic.plugins.warpcutter;

import net.runelite.api.coords.WorldPoint;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum Location
{
    DRAYNOR_VILLAGE("Draynor Village", new WorldPoint(3092, 3245, 0),
        Map.of(
            Trees.TREE, new WorldPoint(3085, 3279, 0),
            Trees.OAK, new WorldPoint(3096, 3264, 0),
            Trees.WILLOW, new WorldPoint(3088, 3234, 0)
        )),
    
    LUMBRIDGE("Lumbridge", new WorldPoint(3208, 3220, 2),
        Map.of(
            Trees.TREE, new WorldPoint(3229, 3248, 0),
            Trees.OAK, new WorldPoint(3192, 3277, 0)
        )),
    
    VARROCK_WEST("Varrock West", new WorldPoint(3169, 3423, 0),
        Map.of(
            Trees.TREE, new WorldPoint(3169, 3423, 0),
            Trees.OAK, new WorldPoint(3182, 3436, 0)
        )),
    
    VARROCK_EAST("Varrock East", new WorldPoint(3277, 3428, 0),
        Map.of(
            Trees.TREE, new WorldPoint(3277, 3428, 0),
            Trees.OAK, new WorldPoint(3250, 3420, 0)
        )),
    
    EDGEVILLE("Edgeville", new WorldPoint(3087, 3471, 0),
        Map.of(
            Trees.TREE, new WorldPoint(3087, 3471, 0),
            Trees.OAK, new WorldPoint(3098, 3470, 0),
            Trees.YEW, new WorldPoint(3087, 3469, 0)
        )),
    
    FALADOR("Falador", new WorldPoint(3014, 3345, 0),
        Map.of(
            Trees.TREE, new WorldPoint(3014, 3345, 0),
            Trees.OAK, new WorldPoint(3035, 3354, 0),
            Trees.WILLOW, new WorldPoint(2998, 3347, 0),
            Trees.YEW, new WorldPoint(3019, 3316, 0)
        )),
    
    SEERS_VILLAGE("Seers Village", new WorldPoint(2726, 3462, 0),
        Map.of(
            Trees.TREE, new WorldPoint(2726, 3462, 0),
            Trees.OAK, new WorldPoint(2734, 3459, 0),
            Trees.WILLOW, new WorldPoint(2725, 3491, 0),
            Trees.MAPLE, new WorldPoint(2728, 3498, 0),
            Trees.YEW, new WorldPoint(2713, 3460, 0)
        )),
    
    CATHERBY("Catherby", new WorldPoint(2801, 3435, 0),
        Map.of(
            Trees.TREE, new WorldPoint(2801, 3435, 0),
            Trees.OAK, new WorldPoint(2796, 3448, 0),
            Trees.WILLOW, new WorldPoint(2795, 3430, 0),
            Trees.YEW, new WorldPoint(2774, 3429, 0)
        )),
    
    WOODCUTTING_GUILD("Woodcutting Guild", new WorldPoint(1656, 3508, 0),
        Map.of(
            Trees.OAK, new WorldPoint(1665, 3514, 0),
            Trees.WILLOW, new WorldPoint(1670, 3501, 0),
            Trees.MAPLE, new WorldPoint(1654, 3495, 0),
            Trees.TEAK, new WorldPoint(1648, 3498, 0),
            Trees.MAHOGANY, new WorldPoint(1652, 3490, 0),
            Trees.YEW, new WorldPoint(1660, 3508, 0),
            Trees.MAGIC, new WorldPoint(1654, 3494, 0)
        )),
    
    HOSIDIUS_HOUSE("Hosidius House", new WorldPoint(1771, 3604, 0),
        Map.of(
            Trees.TREE, new WorldPoint(1771, 3604, 0),
            Trees.OAK, new WorldPoint(1766, 3693, 0),
            Trees.WILLOW, new WorldPoint(1772, 3667, 0),
            Trees.MAPLE, new WorldPoint(1757, 3692, 0)
        ));

    private final String name;
    private final WorldPoint bankPoint;
    private final Map<Trees, WorldPoint> treeLocations;

    Location(String name, WorldPoint bankPoint, Map<Trees, WorldPoint> treeLocations) {
        this.name = name;
        this.bankPoint = bankPoint;
        this.treeLocations = treeLocations;
    }

    public String getName() {
        return name;
    }

    public WorldPoint getBankPoint() {
        return bankPoint;
    }

    public WorldPoint getTreeLocation(Trees tree) {
        return treeLocations.get(tree);
    }

    public List<Trees> getAvailableTrees() {
        return Arrays.asList(treeLocations.keySet().toArray(new Trees[0]));
    }
    
    public boolean hasTree(Trees tree) {
        return treeLocations.containsKey(tree);
    }
    
    @Override
    public String toString() {
        return name;
    }
}