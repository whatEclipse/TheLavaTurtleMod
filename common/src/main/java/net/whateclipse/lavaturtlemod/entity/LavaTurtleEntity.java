package net.whateclipse.lavaturtlemod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.whateclipse.lavaturtlemod.block.LavaTurtleEggBlock;
import net.whateclipse.lavaturtlemod.registry.ModBlocks;
import net.whateclipse.lavaturtlemod.registry.ModEntities;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

public class LavaTurtleEntity extends Animal implements GeoEntity {
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(LavaTurtleEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(LavaTurtleEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(LavaTurtleEntity.class,
            EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> HAS_EGG_BELLY = SynchedEntityData.defineId(LavaTurtleEntity.class,
            EntityDataSerializers.BOOLEAN); // Visual only

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public LavaTurtleEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(net.minecraft.world.level.pathfinder.PathType.LAVA, 0.0F);
        this.setPathfindingMalus(net.minecraft.world.level.pathfinder.PathType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(net.minecraft.world.level.pathfinder.PathType.DAMAGE_FIRE, 0.0F);
        this.moveControl = new LavaTurtleMoveControl(this);
    }

    @Override
    protected net.minecraft.world.entity.ai.navigation.PathNavigation createNavigation(Level level) {
        return new LavaTurtlePathNavigation(this, level);
    }

    static class LavaTurtlePathNavigation extends net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation {
        public LavaTurtlePathNavigation(LavaTurtleEntity mob, Level level) {
            super(mob, level);
        }

        @Override
        protected net.minecraft.world.level.pathfinder.PathFinder createPathFinder(int maxVisitedNodes) {
            this.nodeEvaluator = new net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator(true);
            this.nodeEvaluator.setCanPassDoors(true);
            return new net.minecraft.world.level.pathfinder.PathFinder(this.nodeEvaluator, maxVisitedNodes);
        }

        @Override
        protected boolean canUpdatePath() {
            return true;
        }

        @Override
        protected net.minecraft.world.phys.Vec3 getTempMobPos() {
            return new net.minecraft.world.phys.Vec3(this.mob.getX(), this.mob.getY() + this.mob.getBbHeight() * 0.5D,
                    this.mob.getZ());
        }

        @Override
        public boolean isStableDestination(BlockPos pos) {
            return this.level.getBlockState(pos).is(net.minecraft.tags.BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)
                    || this.level.getFluidState(pos).is(net.minecraft.tags.FluidTags.LAVA);
        }
    }

    static class LavaTurtleMoveControl extends net.minecraft.world.entity.ai.control.MoveControl {
        private final LavaTurtleEntity turtle;

        public LavaTurtleMoveControl(LavaTurtleEntity turtle) {
            super(turtle);
            this.turtle = turtle;
        }

        @Override
        public void tick() {
            if (this.turtle.isInLava()) {
                this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0D, 0.005D, 0.0D)); // Buoyancy
                if (this.operation == Operation.MOVE_TO && !this.turtle.getNavigation().isDone()) {
                    double d0 = this.wantedX - this.turtle.getX();
                    double d1 = this.wantedY - this.turtle.getY();
                    double d2 = this.wantedZ - this.turtle.getZ();
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                    if (d3 < 2.500000277905201E-7D) {
                        this.mob.setZza(0.0F);
                    } else {
                        float f = (float) (net.minecraft.util.Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI))
                                - 90.0F;
                        this.turtle.setYRot(this.rotlerp(this.turtle.getYRot(), f, 10.0F));
                        this.turtle.yBodyRot = this.turtle.getYRot();
                        this.turtle.yHeadRot = this.turtle.getYRot();
                        float f1 = (float) (this.speedModifier
                                * this.turtle.getAttributeValue(Attributes.MOVEMENT_SPEED));
                        if (this.turtle.isInLava()) {
                            this.turtle.setSpeed(f1 * 0.15F);
                            double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                            if (Math.abs(d1) > 1.0E-5D || Math.abs(d4) > 1.0E-5D) {
                                float f2 = -((float) (net.minecraft.util.Mth.atan2(d1, d4)
                                        * (double) (180F / (float) Math.PI)));
                                f2 = net.minecraft.util.Mth.clamp(net.minecraft.util.Mth.wrapDegrees(f2), -85.0F,
                                        85.0F);
                                this.turtle.setXRot(this.rotlerp(this.turtle.getXRot(), f2, 5.0F));
                                float f3 = net.minecraft.util.Mth.cos(this.turtle.getXRot() * ((float) Math.PI / 180F));
                                float f4 = net.minecraft.util.Mth.sin(this.turtle.getXRot() * ((float) Math.PI / 180F));
                                this.turtle.zza = f3 * f1;
                                this.turtle.yya = -f4 * f1;
                            }
                        } else {
                            this.turtle.setSpeed(f1 * 0.25F);
                            super.tick();
                        }
                    }
                } else {
                    this.turtle.setSpeed(0.0F);
                    this.turtle.setXxa(0.0F);
                    this.turtle.setYya(0.0F);
                    this.turtle.setZza(0.0F);
                }
            } else {
                super.tick();
            }
        }
    }

    public void setHomePos(BlockPos pos) {
        this.entityData.set(HOME_POS, pos);
    }

    public BlockPos getHomePos() {
        return this.entityData.get(HOME_POS);
    }

    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
        this.entityData.set(HAS_EGG_BELLY, hasEgg); // Sync visual
    }

    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    public void setLayingEgg(boolean layingEgg) {
        this.entityData.set(LAYING_EGG, layingEgg);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_EGG, false);
        builder.define(LAYING_EGG, false);
        builder.define(HOME_POS, BlockPos.ZERO);
        builder.define(HAS_EGG_BELLY, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D) // Slower on land, fast in lava
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LavaTurtleLayEggGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.TemptGoal(this, 1.1D,
                (stack) -> stack.is(Items.NETHER_SPROUTS), false));
        this.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.RandomStrollGoal(this, 0.8D, 120)); // Slow
                                                                                                                // on
                                                                                                                // land
        this.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.RandomStrollGoal(this, 1.5D, 40) { // Fast
                                                                                                               // in
                                                                                                               // lava
            @Override
            public boolean canUse() {
                return super.canUse() && (LavaTurtleEntity.this.isInLava() || LavaTurtleEntity.this.isInWater());
            }
        });
        this.goalSelector.addGoal(4, new net.minecraft.world.entity.ai.goal.LookAtPlayerGoal(this,
                net.minecraft.world.entity.player.Player.class, 8.0F));
        this.goalSelector.addGoal(5, new net.minecraft.world.entity.ai.goal.RandomLookAroundGoal(this));
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.5F : 1.0F;
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel level, Animal mate) {
        ServerPlayer serverPlayer = this.getLoveCause();
        if (serverPlayer == null && mate.getLoveCause() != null) {
            serverPlayer = mate.getLoveCause();
        }

        if (serverPlayer != null) {
            serverPlayer.awardStat(net.minecraft.stats.Stats.ANIMALS_BRED);
            net.minecraft.advancements.CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayer, this, mate, null);
        }

        this.setHasEgg(true);
        this.resetLove();
        mate.resetLove();
        level.broadcastEntityEvent(this, (byte) 18); // Heart particles
        if (level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOMOBLOOT)) {
            level.addFreshEntity(new net.minecraft.world.entity.ExperienceOrb(level, this.getX(), this.getY(),
                    this.getZ(), this.getRandom().nextInt(7) + 1));
        }
        this.setHomePos(this.blockPosition()); // Update home to current location for egg laying

    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        if (level.getBlockState(pos).getFluidState().is(net.minecraft.tags.FluidTags.LAVA)) {
            return 10.0F; // Prefer lava
        }
        return super.getWalkTargetValue(pos, level);
    }

    // Strider-like movement in lava
    @Override
    public void travel(net.minecraft.world.phys.Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInLava()) {
            this.moveRelative(0.1F, travelVector);
            this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }
    }

    public static boolean checkLavaTurtleSpawnRules(EntityType<LavaTurtleEntity> type,
            LevelAccessor level, MobSpawnType spawnType,
            BlockPos pos, net.minecraft.util.RandomSource random) {
        // We use NO_RESTRICTIONS to bypass engine-level fluid blocking.
        // We filter manually to ensure we stay in lava and below the roof.
        if (pos.getY() > 120)
            return false;

        for (int i = -1; i <= 1; ++i) {
            if (level.getFluidState(pos.offset(0, i, 0)).is(net.minecraft.tags.FluidTags.LAVA)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public net.minecraft.world.entity.SpawnGroupData finalizeSpawn(net.minecraft.world.level.ServerLevelAccessor level,
            net.minecraft.world.DifficultyInstance difficulty,
            net.minecraft.world.entity.MobSpawnType reason,
            @org.jetbrains.annotations.Nullable net.minecraft.world.entity.SpawnGroupData spawnData) {
        this.setHomePos(this.blockPosition());
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.NETHER_SPROUTS);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.LAVA_TURTLE.get().create(level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Add animation controllers here
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    static class LavaTurtleLayEggGoal extends MoveToBlockGoal {
        private final LavaTurtleEntity turtle;
        private int layEggCounter;

        LavaTurtleLayEggGoal(LavaTurtleEntity turtle, double speedModifier) {
            super(turtle, speedModifier, 16);
            this.turtle = turtle;
        }

        @Override
        public boolean canUse() {
            return this.turtle.hasEgg() && this.turtle.getHomePos().distSqr(this.turtle.blockPosition()) < 2048.0D
                    && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.turtle.hasEgg();
        }

        @Override
        public void tick() {
            super.tick();
            if (!this.turtle.isInWater() && !this.turtle.isInLava() && this.isReachedTarget()) {
                if (this.layEggCounter < 1) {
                    this.turtle.setLayingEgg(true);
                }
                this.layEggCounter++;
                if (this.turtle.tickCount % 5 == 0) {
                    this.turtle.level().broadcastEntityEvent(this.turtle, (byte) 18); // Visuals
                }
                if (this.layEggCounter > 60) {
                    this.layEgg();
                }
            } else {
                this.turtle.setLayingEgg(false);
                this.layEggCounter = 0;
            }
        }

        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            return level.getBlockState(pos.above()).isAir() && level.getBlockState(pos).isSolid();
        }

        @Override
        public void start() {
            super.start();
            this.layEggCounter = 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.turtle.setLayingEgg(false);
        }

        public void layEgg() {
            Level level = this.turtle.level();
            BlockPos pos = this.blockPos.above();
            BlockState state = ModBlocks.LAVA_TURTLE_EGG.defaultBlockState().setValue(LavaTurtleEggBlock.EGGS,
                    this.turtle.getRandom().nextInt(4) + 1);
            level.setBlock(pos, state, 3);
            level.playSound(null, pos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F,
                    0.9F + level.random.nextFloat() * 0.2F);
            this.turtle.setHasEgg(false);
            this.turtle.setInLoveTime(600);
        }
    }
}
