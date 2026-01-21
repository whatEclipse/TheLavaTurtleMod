package net.whateclipse.lavaturtlemod.client.model;

import net.minecraft.resources.ResourceLocation;
import net.whateclipse.lavaturtlemod.entity.LavaTurtleEntity;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class LavaTurtleModel extends DefaultedGeoModel<LavaTurtleEntity> {
    public LavaTurtleModel() {
        super(new ResourceLocation("lavaturtlemod", "lavaturtle"));
    }

    @Override
    public ResourceLocation getModelResource(LavaTurtleEntity animatable) {
        return new ResourceLocation("lavaturtlemod", "geo/lavaturtle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LavaTurtleEntity animatable) {
        return new ResourceLocation("lavaturtlemod", "textures/entity/lavaturtle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LavaTurtleEntity animatable) {
        return new ResourceLocation("lavaturtlemod", "animations/lavaturtle.animation.json");
    }
}
