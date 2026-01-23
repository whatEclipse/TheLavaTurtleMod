package net.whateclipse.lavaturtlemod.client.model;

import net.minecraft.resources.ResourceLocation;
import net.whateclipse.lavaturtlemod.entity.LavaTurtleEntity;
import software.bernie.geckolib.model.GeoModel;

public class LavaTurtleModel extends GeoModel<LavaTurtleEntity> {
    public LavaTurtleModel() {
    }

    @Override
    public ResourceLocation getModelResource(LavaTurtleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("lavaturtlemod", "geo/lavaturtle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LavaTurtleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("lavaturtlemod", "textures/entity/lavaturtle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LavaTurtleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("lavaturtlemod", "animations/lavaturtle.animation.json");
    }

    @Override
    public void setCustomAnimations(LavaTurtleEntity animatable, long instanceId,
            software.bernie.geckolib.animation.AnimationState<LavaTurtleEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        software.bernie.geckolib.cache.object.GeoBone leg0 = getAnimationProcessor().getBone("leg0");
        software.bernie.geckolib.cache.object.GeoBone leg1 = getAnimationProcessor().getBone("leg1");
        software.bernie.geckolib.cache.object.GeoBone leg2 = getAnimationProcessor().getBone("leg2");
        software.bernie.geckolib.cache.object.GeoBone leg3 = getAnimationProcessor().getBone("leg3");
        software.bernie.geckolib.cache.object.GeoBone eggbelly = getAnimationProcessor().getBone("eggbelly");
        software.bernie.geckolib.cache.object.GeoBone bodyR1 = getAnimationProcessor().getBone("body_r1");

        if (bodyR1 != null) {
            // body_r1.setRotX((float) Math.toRadians(270));
            // body_r1.setRotY((float) Math.toRadians(180));

            // Move the shell down and forward to align with the head and legs
            // Adjust these values if the gap is still visible
            // bodyR1.setPosY(-15.5f);
            // bodyR1.setPosZ(-10.0f);
        }

        if (leg0 != null && leg1 != null && leg2 != null && leg3 != null) {
            float f0 = 0;
            float g = 0;

            // Try to get limb swing from animation state or entity
            // GeckoLib's AnimationState doesn't always expose vanilla limb swing directly
            // in a uniform way across versions,
            // but we can try accessing the entity's walk animation state.
            f0 = animatable.walkAnimation.position();
            g = animatable.walkAnimation.speed();

            leg0.setRotX(net.minecraft.util.Mth.cos(f0 * 0.6662F * 0.6F) * 0.5F * g);
            leg1.setRotX(net.minecraft.util.Mth.cos(f0 * 0.6662F * 0.6F + (float) Math.PI) * 0.5F * g);
            leg2.setRotZ(net.minecraft.util.Mth.cos(f0 * 0.6662F * 0.6F + (float) Math.PI) * 0.5F * g);
            leg3.setRotZ(net.minecraft.util.Mth.cos(f0 * 0.6662F * 0.6F) * 0.5F * g);

            leg2.setRotX(0.0F);
            leg3.setRotX(0.0F);
            leg2.setRotY(0.0F);
            leg3.setRotY(0.0F);
            leg0.setRotY(0.0F);
            leg1.setRotY(0.0F);

            if (!animatable.isInWater() && !animatable.isInLava() && animatable.onGround()) {
                float f = animatable.isLayingEgg() ? 4.0F : 1.0F;
                float f1 = animatable.isLayingEgg() ? 2.0F : 1.0F;

                leg2.setRotY(net.minecraft.util.Mth.cos(f * f0 * 5.0F + (float) Math.PI) * 8.0F * g * f1);
                leg2.setRotZ(0.0F);
                leg3.setRotY(net.minecraft.util.Mth.cos(f * f0 * 5.0F) * 8.0F * g * f1);
                leg3.setRotZ(0.0F);
                leg0.setRotY(net.minecraft.util.Mth.cos(f0 * 5.0F + (float) Math.PI) * 3.0F * g);
                leg0.setRotX(0.0F);
                leg1.setRotY(net.minecraft.util.Mth.cos(f0 * 5.0F) * 3.0F * g);
                leg1.setRotX(0.0F);
            }
        }

        if (eggbelly != null) {
            eggbelly.setHidden(!(!animatable.isBaby() && animatable.hasEgg()));
        }
    }
}
