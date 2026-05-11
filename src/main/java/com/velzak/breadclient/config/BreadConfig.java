package com.velzak.breadclient.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "breadclient")
public class BreadConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean flyEnabled = false;

    @ConfigEntry.Gui.Tooltip
    public int hudColor = 0xFFFFFF;
}