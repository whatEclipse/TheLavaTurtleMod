package net.whateclipse.lavaturtlemod.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.whateclipse.lavaturtlemod.entity.LavaTurtleEntity;
import net.whateclipse.lavaturtlemod.client.model.LavaTurtleModel;

public class LavaTurtleRenderer extends GeoEntityRenderer<LavaTurtleEntity> {
    public LavaTurtleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LavaTurtleModel());
    }
}
