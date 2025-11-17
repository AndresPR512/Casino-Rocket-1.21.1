package net.andrespr.casinorocket.screen.custom;

import net.andrespr.casinorocket.network.c2s.OpenBetScreenC2SPayload;
import net.andrespr.casinorocket.network.c2s.OpenMenuScreenC2SPayload;
import net.andrespr.casinorocket.network.c2s.OpenWithdrawScreenC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

public abstract class CasinoMachineScreen<T extends ScreenHandler> extends HandledScreen<T> {

    public CasinoMachineScreen(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    protected void onBetPressed() {
        if (client != null && client.player != null) {
            ClientPlayNetworking.send(new OpenBetScreenC2SPayload());
        }
    }

    protected void onWithdrawPressed() {
        if (client != null && client.player != null) {
            ClientPlayNetworking.send(new OpenWithdrawScreenC2SPayload());
        }
    }

    protected void onMenuPressed() {
        if (client != null && client.player != null) {
            ClientPlayNetworking.send(new OpenMenuScreenC2SPayload());
        }
    }

}