package net.andrespr.casinorocket.sound;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent INSERTING_COIN = registerSoundEvent("inserting_coin");
    public static final SoundEvent COMMON_PRIZE = registerSoundEvent("common_prize");
    public static final SoundEvent UNCOMMON_PRIZE = registerSoundEvent("uncommon_prize");
    public static final SoundEvent RARE_PRIZE = registerSoundEvent("rare_prize");
    public static final SoundEvent ULTRARARE_PRIZE = registerSoundEvent("ultrarare_prize");
    public static final SoundEvent LEGENDARY_PRIZE = registerSoundEvent("legendary_prize");
    public static final SoundEvent BONUS_PRIZE = registerSoundEvent("bonus_prize");
    public static final SoundEvent OPEN_PRIZE = registerSoundEvent("open_prize");

    private static SoundEvent registerSoundEvent(String name) {
        return Registry.register(Registries.SOUND_EVENT, Identifier.of(CasinoRocket.MOD_ID, name),
                SoundEvent.of(Identifier.of(CasinoRocket.MOD_ID, name)));
    }

    public static void registerSounds() {
        CasinoRocket.LOGGER.info("Registering Mod Sounds for " + CasinoRocket.MOD_ID);
    }

}