package com.tonic.plugins.warpcutter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("warpcutterplugin")
public interface WarpCutterConfig extends Config {

    @ConfigSection(
            name = "Warp Cutter",
            description = "Settings for the Warp Cutter plugin",
            position = 0
    )
    String general = "warpchopper";
    
    @ConfigItem(
            keyName = "location",
            name = "Location",
            description = "Select woodcutting location",
            position = 1,
            section = general
    )
    default Location location() {
        return Location.DRAYNOR_VILLAGE;
    }
    
    @ConfigItem(
            keyName = "treeType",
            name = "Tree type",
            description = "Select tree type - automatically filtered by location",
            position = 2,
            section = general
    )
    default Trees treeType()
    {
        return location().getAvailableTrees().get(0);
    }
    
    @ConfigItem(
			keyName = "treeType",
            name = "",
            description = "",
            section = general,
            hidden = true)
    default boolean treeType(Trees tree)
    {
        return !location().getAvailableTrees().contains(tree);
    }


    @ConfigItem(
            keyName = "treeRadius",
            name = "Tree Radius",
            description = "The distance to search for trees",
            position = 3,
            section = general
    )
    default int treeRadius() {
        return 10;
    }

    @ConfigItem(
            keyName = "powerCut",
            name = "Drop logs",
            description = "Enable to drop logs as they are cut",
            position = 4,
            section = general
    )
    default boolean powerCut() {
        return true;
    }
    
    @ConfigItem(
            keyName = "hardwoodPatch",
            name = "Hardwood Patch",
            description = "Use this for the Hardwood farm patch",
            position = 5,
            section = general
    )
    default boolean hardwoodPatch() {
        return false;
    }
}