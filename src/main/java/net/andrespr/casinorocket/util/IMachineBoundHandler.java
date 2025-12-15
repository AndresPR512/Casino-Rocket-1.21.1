package net.andrespr.casinorocket.util;

import net.minecraft.util.math.BlockPos;

public interface IMachineBoundHandler {
    BlockPos getMachinePos();
    default String getMachineKey() {
        return "unknown";
    }
}