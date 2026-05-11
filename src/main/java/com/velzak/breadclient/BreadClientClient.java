package com.velzak.breadclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import com.velzak.breadclient.config.BreadConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class BreadClientClient implements ClientModInitializer {
    private static KeyBinding openHudKey;
    private static boolean hudOpen = false;
    private static BreadConfig config;

    @Override
    public void onInitializeClient() {
        // Register config
        AutoConfig.register(BreadConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(BreadConfig.class).getConfig();

        // Register key binding
        openHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.breadclient.open_hud",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.breadclient"
        ));

        // Tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openHudKey.wasPressed()) {
                hudOpen = !hudOpen;
            }
            // Handle fly
            if (client.player != null) {
                client.player.getAbilities().allowFlying = config.flyEnabled;
                if (!config.flyEnabled) {
                    client.player.getAbilities().flying = false;
                }
            }
        });

        // HUD render
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (hudOpen) {
                MinecraftClient client = MinecraftClient.getInstance();
                int y = 10;
                drawContext.drawText(client.textRenderer, Text.of("Bread Client HUD"), 10, y, config.hudColor, false);
                y += 10;
                if (client.player != null) {
                    drawContext.drawText(client.textRenderer, Text.of("X: " + (int)client.player.getX()), 10, y, config.hudColor, false);
                    y += 10;
                    drawContext.drawText(client.textRenderer, Text.of("Y: " + (int)client.player.getY()), 10, y, config.hudColor, false);
                    y += 10;
                    drawContext.drawText(client.textRenderer, Text.of("Z: " + (int)client.player.getZ()), 10, y, config.hudColor, false);
                    y += 10;
                    drawContext.drawText(client.textRenderer, Text.of("Health: " + (int)client.player.getHealth()), 10, y, config.hudColor, false);
                }
            }
        });
    }

    public static boolean isHudOpen() {
        return hudOpen;
    }
}