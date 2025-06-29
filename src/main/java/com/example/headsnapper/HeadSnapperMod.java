// src/main/java/com/example/headsnapper/HeadSnapperMod.java
package com.example.headsnapper;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class HeadSnapperMod implements ClientModInitializer {
    private static KeyBinding snapKey;
    private static double snapAngle = 90.0;

    @Override
    public void onInitializeClient() {
        snapKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.headsnapper.snap",
                GLFW.GLFW_KEY_UNKNOWN,
                "category.headsnapper"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (snapKey.wasPressed()) {
                double yaw = client.player.getYaw();
                double target = snapAngle;
                double snapped = client.player.headYaw + ((float)target) - ((client.player.headYaw + target) % target);
                if ((client.player.headYaw + target) % target != 0) {
                    client.player.setYaw((float) snapped);
                }
                client.inGameHud.setOverlayMessage(Text.literal("Snapped to " + snapped + "°"), false);
            }
        });
    }

    public static ConfigBuilder createConfigScreen(ConfigBuilder builder) {
        builder.getOrCreateCategory(Text.literal("Settings"))
                .addDoubleField(
                        Text.literal("Snap angle (°)"),
                        snapAngle,
                        val -> snapAngle = val,
                        () -> snapAngle
                );
        builder.getOrCreateCategory(Text.literal("Settings"))
                .addKeyBindingField(
                        Text.literal("Snap key"),
                        snapKey,
                        val -> snapKey = val
                );
        return builder;
    }
}
