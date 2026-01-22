package net.whateclipse.lavaturtlemod.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.whateclipse.lavaturtlemod.Constants;
import net.whateclipse.lavaturtlemod.Lavaturtlemod;
import net.whateclipse.lavaturtlemod.client.renderer.LavaTurtleRenderer;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LavaturtlemodClient {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Lavaturtlemod.LAVA_TURTLE.get(), LavaTurtleRenderer::new);
    }
}
