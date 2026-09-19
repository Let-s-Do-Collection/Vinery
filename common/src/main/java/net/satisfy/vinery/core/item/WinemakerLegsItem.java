package net.satisfy.vinery.core.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.registry.ArmorRegistryClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WinemakerLegsItem extends ArmorItem {
    private final ResourceLocation leggingsTexture;

    public WinemakerLegsItem(Holder<ArmorMaterial> armorMaterial, Type type, Properties properties, ResourceLocation leggingsTexture) {
        super(armorMaterial, type, properties);
        this.leggingsTexture = leggingsTexture;
    }

    public ResourceLocation getLeggingsTexture() {
        return leggingsTexture;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, TooltipContext ctx, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.isClientSide()) {
            ArmorRegistryClient.appendToolTip(tooltip);
        }
    }
}
