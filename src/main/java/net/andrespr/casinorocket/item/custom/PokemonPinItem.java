package net.andrespr.casinorocket.item.custom;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import net.andrespr.casinorocket.util.CobblemonUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import java.util.List;

public class PokemonPinItem extends Item {

    private final int level;
    private final int ivs;
    private final boolean shiny;

    public PokemonPinItem(Settings settings, int level, int ivs, boolean shiny) {
        super(settings.maxCount(1));
        this.level = level;
        this.ivs = ivs;
        this.shiny = shiny;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof ServerPlayerEntity player) {

            NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
            NbtCompound tag = nbtComponent != null ? nbtComponent.copyNbt() : new NbtCompound();

            if (!tag.getBoolean("Used")) {
                MinecraftServer server = player.getServer();
                Identifier id = Registries.ITEM.getId(this);
                String speciesName = id.getPath().replace("_pin", "");

                PokemonProperties properties = CobblemonUtils.safeParse(speciesName, player, server);
                if (properties == null) { return; }

                properties.setLevel(level);
                properties.setIvs(CobblemonUtils.createFixedIVs(ivs));
                properties.setShiny(shiny);
                properties.setForm(null);

                CobblemonUtils.addPokemon(properties, player);

                tag.putBoolean("Used", true);
                stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));

                player.getInventory().removeOne(stack);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.casinorocket.pokemon_pin"));
        super.appendTooltip(stack, context, tooltip, type);
    }

}