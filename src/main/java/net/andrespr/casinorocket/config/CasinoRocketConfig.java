package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = "casinorocket")
public class CasinoRocketConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Category("item_gachapon")
    @ConfigEntry.Gui.TransitiveObject
    public ItemGachaponConfig itemGachapon = new ItemGachaponConfig();

}