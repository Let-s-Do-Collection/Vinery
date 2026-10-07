package net.satisfy.vinery.client.util.fabric;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public class ExtraModelsImpl {
    public static BakedModel get(ResourceLocation id) {
        return Minecraft.getInstance().getModelManager().getModel(id);
    }
}
