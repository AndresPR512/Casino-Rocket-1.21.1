package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = "casinorocket")
public class CasinoRocketConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Category("item_gachapon")
    @ConfigEntry.Gui.TransitiveObject
    public ItemGachaponConfig itemGachapon = new ItemGachaponConfig();

    @ConfigEntry.Category("pokemon_gachapon")
    @ConfigEntry.Gui.TransitiveObject
    public PokemonGachaponConfig pokemonGachapon = new PokemonGachaponConfig();

    @ConfigEntry.Category("gacha_machines")
    @ConfigEntry.Gui.TransitiveObject
    public GachaMachinesConfig gachaMachines = new GachaMachinesConfig();

    @ConfigEntry.Category("casino_chips_config")
    @ConfigEntry.Gui.TransitiveObject
    public CasinoChipsConfig casinoChips = new CasinoChipsConfig();

    @ConfigEntry.Category("plushies_gachapon")
    @ConfigEntry.Gui.TransitiveObject
    public PlushiesGachaponConfig plushiesGachapon = new PlushiesGachaponConfig();

    @ConfigEntry.Category("slot_machine")
    @ConfigEntry.Gui.TransitiveObject
    public SlotMachineConfig slotMachine = new SlotMachineConfig();

}