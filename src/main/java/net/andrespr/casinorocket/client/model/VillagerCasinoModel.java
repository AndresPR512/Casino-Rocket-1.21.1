package net.andrespr.casinorocket.client.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.entity.passive.VillagerEntity;

public class VillagerCasinoModel<T extends VillagerEntity> extends VillagerResemblingModel<T> {
    public VillagerCasinoModel(ModelPart root) {
        super(root);
    }
}