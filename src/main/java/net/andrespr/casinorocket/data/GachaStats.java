package net.andrespr.casinorocket.data;

import net.minecraft.nbt.NbtCompound;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class GachaStats {

    private static String playerName;
    public int totalCoinsUsed = 0;
    public int copperUsed = 0;
    public int ironUsed = 0;
    public int goldUsed = 0;
    public int diamondUsed = 0;
    public int common = 0;
    public int uncommon = 0;
    public int rare = 0;
    public int ultrarare = 0;
    public int legendary = 0;
    public int bonus = 0;

    public GachaStats(String playerName) {
        GachaStats.playerName = playerName;
    }

    public void recordUse(String coinKey, String rarity) {
        totalCoinsUsed++;

        switch (coinKey.toLowerCase()) {
            case "copper" -> copperUsed++;
            case "iron" -> ironUsed++;
            case "gold" -> goldUsed++;
            case "diamond" -> diamondUsed++;
        }

        switch (rarity.toLowerCase()) {
            case "common" -> common++;
            case "uncommon" -> uncommon++;
            case "rare" -> rare++;
            case "ultrarare" -> ultrarare++;
            case "legendary" -> legendary++;
            case "bonus" -> bonus++;
        }
    }

    public void recordBonus() {
        bonus++;
    }

    // === SAVE TO NBT ===
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("totalCoinsUsed", totalCoinsUsed);
        nbt.putInt("copperUsed", copperUsed);
        nbt.putInt("ironUsed", ironUsed);
        nbt.putInt("goldUsed", goldUsed);
        nbt.putInt("diamondUsed", diamondUsed);
        nbt.putInt("common", common);
        nbt.putInt("uncommon", uncommon);
        nbt.putInt("rare", rare);
        nbt.putInt("ultrarare", ultrarare);
        nbt.putInt("legendary", legendary);
        nbt.putInt("bonus", bonus);
        return nbt;
    }

    // === LOAD FROM NBT ===
    public static GachaStats fromNbt(NbtCompound nbt) {
        GachaStats stats = new GachaStats(playerName);
        stats.totalCoinsUsed = nbt.getInt("totalCoinsUsed");
        stats.copperUsed = nbt.getInt("copperUsed");
        stats.ironUsed = nbt.getInt("ironUsed");
        stats.goldUsed = nbt.getInt("goldUsed");
        stats.diamondUsed = nbt.getInt("diamondUsed");
        stats.common = nbt.getInt("common");
        stats.uncommon = nbt.getInt("uncommon");
        stats.rare = nbt.getInt("rare");
        stats.ultrarare = nbt.getInt("ultrarare");
        stats.legendary = nbt.getInt("legendary");
        stats.bonus = nbt.getInt("bonus");
        return stats;
    }

    // === GETTERS ===

    public int getTotalCoinsUsed() { return totalCoinsUsed; }

    public int getRarityCount(String rarity) {
        return switch (rarity.toLowerCase(Locale.ROOT)) {
            case "common" -> common;
            case "uncommon" -> uncommon;
            case "rare" -> rare;
            case "ultrarare" -> ultrarare;
            case "legendary" -> legendary;
            case "bonus" -> bonus;
            default -> 0;
        };
    }

    public Map<String, Integer> getRarityCounts() {
        Map<String, Integer> m = new LinkedHashMap<>();
        m.put("common", common);
        m.put("uncommon", uncommon);
        m.put("rare", rare);
        m.put("ultrarare", ultrarare);
        m.put("legendary", legendary);
        m.put("bonus", bonus);
        return m;
    }

    public int getCoinCount(String coinKey) {
        return switch (coinKey.toLowerCase(Locale.ROOT)) {
            case "copper" -> copperUsed;
            case "iron" -> ironUsed;
            case "gold" -> goldUsed;
            case "diamond" -> diamondUsed;
            default -> 0;
        };
    }

    public String getPlayerName() {
        return playerName;
    }

    // === SETTERS ===

    public void setPlayerName(String name) {
        playerName = name;
    }

}