package net.whateclipse.lavaturtlemod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LavaTurtleEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public LavaTurtleEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.25D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new net.minecraft.world.entity.ai.goal.RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.LookAtPlayerGoal(this,
                net.minecraft.world.entity.player.Player.class, 8.0F));
        this.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.RandomLookAroundGoal(this));
    }

    public static boolean checkLavaTurtleSpawnRules(EntityType<LavaTurtleEntity> type,
            net.minecraft.world.level.LevelAccessor level, net.minecraft.world.entity.MobSpawnType spawnType,
            net.minecraft.core.BlockPos pos, net.minecraft.util.RandomSource random) {
        return level.getFluidState(pos).is(net.minecraft.tags.FluidTags.LAVA);
    }

    private boolean hasEgg;
    private boolean layingEgg;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Add animation controllers here
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public boolean hasEgg() {
        return this.hasEgg;
    }

    public void setHasEgg(boolean hasEgg) {
        this.hasEgg = hasEgg;
    }

    public boolean isLayingEgg() {
        return this.layingEgg;
    }

    public void setLayingEgg(boolean layingEgg) {
        this.layingEgg = layingEgg;
    }
}
