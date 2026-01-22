package net.whateclipse.lavaturtlemod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.whateclipse.lavaturtlemod.Constants;
import net.whateclipse.lavaturtlemod.Lavaturtlemod;
import net.whateclipse.lavaturtlemod.client.renderer.LavaTurtleRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class LavaturtlemodClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Lavaturtlemod.LAVA_TURTLE, LavaTurtleRenderer::new);
    }
}
