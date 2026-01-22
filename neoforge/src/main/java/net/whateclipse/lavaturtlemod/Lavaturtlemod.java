package net.whateclipse.lavaturtlemod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.whateclipse.lavaturtlemod.entity.LavaTurtleEntity;

@Mod(Constants.MOD_ID)
public class Lavaturtlemod {

        public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister
                        .create(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE, Constants.MOD_ID);

        public static final java.util.function.Supplier<EntityType<LavaTurtleEntity>> LAVA_TURTLE = ENTITY_TYPES
                        .register("lavaturtle",
                                        () -> EntityType.Builder.of(LavaTurtleEntity::new, MobCategory.CREATURE)
                                                        .sized(1.0f, 1.0f).build("lavaturtle"));

        public Lavaturtlemod(IEventBus eventBus) {
                Constants.LOG.info("Hello NeoForge world!");
                CommonClass.init();
                ENTITY_TYPES.register(eventBus);
        }

        @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
        public static class ModEvents {
                @SubscribeEvent
                public static void commonSetup(EntityAttributeCreationEvent event) {
                        event.put(LAVA_TURTLE.get(), LavaTurtleEntity.createAttributes().build());
                }

                @SubscribeEvent
                public static void registerSpawnPlacements(
                                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent event) {
                        event.register(LAVA_TURTLE.get(),
                                        net.minecraft.world.entity.SpawnPlacementTypes.IN_LAVA,
                                        net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                                        LavaTurtleEntity::checkLavaTurtleSpawnRules,
                                        net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE);
                }
        }
}
