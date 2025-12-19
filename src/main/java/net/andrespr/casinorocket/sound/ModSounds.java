package net.andrespr.casinorocket.sound;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    // === SOUND EFFECTS ===
    // GACHA MACHINE
    public static final SoundEvent INSERTING_COIN = registerSoundEvent("inserting_coin");
    // PRIZES
    public static final SoundEvent COMMON_PRIZE = registerSoundEvent("common_prize");
    public static final SoundEvent UNCOMMON_PRIZE = registerSoundEvent("uncommon_prize");
    public static final SoundEvent RARE_PRIZE = registerSoundEvent("rare_prize");
    public static final SoundEvent ULTRARARE_PRIZE = registerSoundEvent("ultrarare_prize");
    public static final SoundEvent LEGENDARY_PRIZE = registerSoundEvent("legendary_prize");
    public static final SoundEvent BONUS_PRIZE = registerSoundEvent("bonus_prize");
    public static final SoundEvent OPEN_PRIZE = registerSoundEvent("open_prize");
    // SLOT MACHINE
    public static final SoundEvent REELS_SPINNING = registerSoundEvent("reels_spinning");
    public static final SoundEvent JACKPOT = registerSoundEvent("jackpot");
    // WALLET
    public static final SoundEvent WALLET = registerSoundEvent("wallet");
    public static final SoundEvent WALLET2 = registerSoundEvent("wallet2");
    // BLACKJACK
    public static final SoundEvent WIN = registerSoundEvent("win");
    public static final SoundEvent DRAW = registerSoundEvent("draw");
    public static final SoundEvent LOSE = registerSoundEvent("lose");
    public static final SoundEvent CARD = registerSoundEvent("card");
    // BUTTON
    public static final SoundEvent BUTTON = registerSoundEvent("button");

    // === MUSIC DISCS ===
    // First Generation Remasters
    public static final SoundEvent FIRERED_GC = registerSoundEvent("firered_gc");
    public static final RegistryKey<JukeboxSong> FIRERED_GC_KEY = of("firered_gc");
    // Second Generation Remasters
    public static final SoundEvent HEARTGOLD_GC = registerSoundEvent("heartgold_gc");
    public static final RegistryKey<JukeboxSong> HEARTGOLD_GC_KEY = of("heartgold_gc");
    // Third Generation
    public static final SoundEvent EMERALD_GC = registerSoundEvent("emerald_gc");
    public static final RegistryKey<JukeboxSong> EMERALD_GC_KEY = of("emerald_gc");
    // First Generation
    public static final SoundEvent PLATINUM_GC = registerSoundEvent("platinum_gc");
    public static final RegistryKey<JukeboxSong> PLATINUM_GC_KEY = of("platinum_gc");

    private static RegistryKey<JukeboxSong> of(String name) {
        return RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(CasinoRocket.MOD_ID, name));
    }
    private static SoundEvent registerSoundEvent(String name) {
        return Registry.register(Registries.SOUND_EVENT, Identifier.of(CasinoRocket.MOD_ID, name),
                SoundEvent.of(Identifier.of(CasinoRocket.MOD_ID, name)));
    }

    public static void registerSounds() {
        CasinoRocket.LOGGER.info("Registering Mod Sounds for " + CasinoRocket.MOD_ID);
    }

}