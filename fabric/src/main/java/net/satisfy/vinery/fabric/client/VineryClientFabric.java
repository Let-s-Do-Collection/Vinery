package net.satisfy.vinery.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.satisfy.foundation.fabric.client.FoundationArmorRenderer;
import net.satisfy.vinery.client.VineryClient;
import net.satisfy.vinery.core.registry.MobEffectRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import org.lwjgl.glfw.GLFW;

public class VineryClientFabric implements ClientModInitializer {
    private static boolean hasDoubleJumped = false;
    private static boolean wasOnGround = true;
    private static boolean spaceWasPressed = false;
    @Override
    public void onInitializeClient() {
        VineryClient.preInitClient();
        VineryClient.onInitializeClient();
        ArmorRenderer.register(FoundationArmorRenderer.INSTANCE, ObjectRegistry.STRAW_HAT.get(), ObjectRegistry.WINEMAKER_APRON.get(), ObjectRegistry.WINEMAKER_LEGGINGS.get(), ObjectRegistry.WINEMAKER_BOOTS.get());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            LocalPlayer player = client.player;

            if (player == null || client.level == null) {
                return;
            }

            if (!player.hasEffect(MobEffectRegistry.getHolder(MobEffectRegistry.IMPROVED_JUMP_BOOST))) return;
            if (!canJump(player)) return;


            boolean spacePressed = client.options.keyJump.isDown();


            if (player.onGround()) {
                hasDoubleJumped = false;
                wasOnGround = true;
                spaceWasPressed = false;
            } else if (wasOnGround) {
                wasOnGround = false;
            }

            if (spacePressed && !spaceWasPressed && !player.onGround() && !hasDoubleJumped && !wasOnGround) {
                performDoubleJump(player);
                hasDoubleJumped = true;
            }

            spaceWasPressed = spacePressed;
        });
    }

    private static void performDoubleJump(Player player) {
        Vec3 motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.x, 0.42, motion.z);
        player.hasImpulse = true;
    }

    private static boolean canJump(LocalPlayer player) {
        return !wearingUsableElytra(player)
                && !player.isFallFlying()
                && !player.isPassenger()
                && !player.isInWater()
                && !player.hasEffect(MobEffects.LEVITATION);
    }

    private static boolean wearingUsableElytra(LocalPlayer player) {
        ItemStack chestItemStack = player.getItemBySlot(EquipmentSlot.CHEST);
        return chestItemStack.getItem() == Items.ELYTRA && ElytraItem.isFlyEnabled(chestItemStack);
    }
}
