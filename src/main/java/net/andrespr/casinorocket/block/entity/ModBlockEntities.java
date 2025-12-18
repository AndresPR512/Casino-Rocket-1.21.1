package net.andrespr.casinorocket.block.entity;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.block.entity.custom.BlackjackTableEntity;
import net.andrespr.casinorocket.block.entity.custom.SlotMachineEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<SlotMachineEntity> SLOT_MACHINE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(CasinoRocket.MOD_ID, "slot_machine_be"),
                    BlockEntityType.Builder.create(SlotMachineEntity::new, ModBlocks.SLOT_MACHINE).build(null));

    public static final BlockEntityType<BlackjackTableEntity> BLACKJACK_TABLE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(CasinoRocket.MOD_ID, "blackjack_table_be"),
                    BlockEntityType.Builder.create(BlackjackTableEntity::new, ModBlocks.BLACKJACK_TABLE).build(null));

    public static void registerBlockEntities() {
        CasinoRocket.LOGGER.info("Registering Block Entities for " + CasinoRocket.MOD_ID);
    }

}