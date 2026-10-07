package net.satisfy.vinery.core.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.ArmorSetRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
@Mixin(BoneMealItem.class)
public abstract class BoneMealItemMixin {
    @Inject(method = "useOn", at = @At("RETURN"), cancellable = true)
    public void useOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue() != InteractionResult.CONSUME) {
            return;
        }

        if (!(context.getLevel() instanceof ServerLevel)) {
            return;
        }

        Player player = context.getPlayer();
        if (player == null) return;

        if (ArmorSetRegistry.hasWinemakerSet(player)) {
            cir.setReturnValue(InteractionResult.PASS);
            ItemStack heldItem = context.getItemInHand();
            if (!heldItem.isEmpty()) {
                heldItem.grow(1);
            }

            for (int i = 0; i < 4; i++) {
                ItemStack armorPiece = player.getInventory().getArmor(i);
                if (!armorPiece.isEmpty()) {
                    EquipmentSlot slot = switch(i) {
                        case 0 -> EquipmentSlot.FEET;
                        case 1 -> EquipmentSlot.LEGS;
                        case 2 -> EquipmentSlot.CHEST;
                        case 3 -> EquipmentSlot.HEAD;
                        default -> EquipmentSlot.MAINHAND;
                    };
                    armorPiece.hurtAndBreak(2, player, slot);
                }
            }
        }
    }
}