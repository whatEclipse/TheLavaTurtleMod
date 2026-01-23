package net.whateclipse.lavaturtlemod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.whateclipse.lavaturtlemod.registry.ModEntities;
import net.whateclipse.lavaturtlemod.entity.LavaTurtleEntity;

@Mod(Constants.MOD_ID)
public class Lavaturtlemod {

        public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister
                        .create(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE, Constants.MOD_ID);
        public static final DeferredRegister<net.minecraft.world.level.block.Block> BLOCKS = DeferredRegister
                        .create(net.minecraft.core.registries.BuiltInRegistries.BLOCK, Constants.MOD_ID);
        public static final DeferredRegister<net.minecraft.world.item.Item> ITEMS = DeferredRegister
                        .create(net.minecraft.core.registries.BuiltInRegistries.ITEM, Constants.MOD_ID);

        public static final java.util.function.Supplier<net.minecraft.world.level.block.Block> LAVA_TURTLE_EGG = BLOCKS
                        .register("lavaturtle_eggs",
                                        () -> net.whateclipse.lavaturtlemod.registry.ModBlocks.LAVA_TURTLE_EGG);
        public static final java.util.function.Supplier<net.minecraft.world.item.Item> LAVA_TURTLE_EGG_ITEM = ITEMS
                        .register("lavaturtle_eggs", () -> new net.minecraft.world.item.BlockItem(LAVA_TURTLE_EGG.get(),
                                        new net.minecraft.world.item.Item.Properties()));

        public static final java.util.function.Supplier<EntityType<LavaTurtleEntity>> LAVA_TURTLE = ENTITY_TYPES
                        .register("lavaturtle",
                                        () -> EntityType.Builder.of(LavaTurtleEntity::new, MobCategory.MONSTER)
                                                        .sized(1.0f, 1.0f).fireImmune().build("lavaturtle"));

        public static final java.util.function.Supplier<net.minecraft.world.item.Item> LAVA_TURTLE_SPAWN_EGG = ITEMS
                        .register("lavaturtle_spawn_egg", () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(
                                        LAVA_TURTLE, 0x4D2121, 0xFF9900,
                                        new net.minecraft.world.item.Item.Properties()));

        public Lavaturtlemod(IEventBus eventBus) {
                Constants.LOG.info("Hello NeoForge world! Loading LavaTurtleMod...");
                ModEntities.LAVA_TURTLE = LAVA_TURTLE;
                CommonClass.init();
                ENTITY_TYPES.register(eventBus);
                BLOCKS.register(eventBus);
                ITEMS.register(eventBus);
                ModEntities.LAVA_TURTLE = LAVA_TURTLE;
        }

        @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
        public static class ModEvents {
                @SubscribeEvent
                public static void commonSetup(EntityAttributeCreationEvent event) {
                        event.put(ModEntities.LAVA_TURTLE.get(), LavaTurtleEntity.createAttributes().build());
                }

                @SubscribeEvent
                public static void registerSpawnPlacements(
                                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent event) {
                        event.register(ModEntities.LAVA_TURTLE.get(),
                                        net.minecraft.world.entity.SpawnPlacementTypes.NO_RESTRICTIONS,
                                        net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                                        LavaTurtleEntity::checkLavaTurtleSpawnRules,
                                        net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE);
                }
        }
}
