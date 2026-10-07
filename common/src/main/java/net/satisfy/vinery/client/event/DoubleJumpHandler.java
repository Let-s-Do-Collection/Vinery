package net.satisfy.vinery.client.event;

import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.satisfy.vinery.core.registry.MobEffectRegistry;

public final class DoubleJumpHandler {
    private static final double JUMP_VELOCITY = 0.42;
    private static boolean hasDoubleJumped;
    private static boolean wasOnGround = true;
    private static boolean jumpWasPressed;

    private DoubleJumpHandler() {
    }

    public static void init() {
        ClientTickEvent.CLIENT_POST.register(DoubleJumpHandler::tick);
    }

    private static void tick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null || client.level == null || !player.hasEffect(MobEffectRegistry.getHolder(MobEffectRegistry.IMPROVED_JUMP_BOOST)) || !canJump(player)) {
            return;
        }
        boolean jumpPressed = client.options.keyJump.isDown();
        if (player.onGround()) {
            hasDoubleJumped = false;
            wasOnGround = true;
            jumpWasPressed = false;
        } else if (wasOnGround) {
            wasOnGround = false;
        } else if (jumpPressed && !jumpWasPressed && !hasDoubleJumped) {
            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(motion.x, JUMP_VELOCITY, motion.z);
            player.hasImpulse = true;
            hasDoubleJumped = true;
        }
        jumpWasPressed = jumpPressed;
    }

    private static boolean canJump(LocalPlayer player) {
        return !wearingUsableElytra(player) && !player.isFallFlying() && !player.isPassenger()
                && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION);
    }

    private static boolean wearingUsableElytra(LocalPlayer player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        return chest.is(Items.ELYTRA) && ElytraItem.isFlyEnabled(chest);
    }
}
