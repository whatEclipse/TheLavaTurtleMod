package net.whateclipse.lavaturtlemod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.whateclipse.lavaturtlemod.entity.LavaTurtleEntity;

public class Lavaturtlemod implements ModInitializer {

        public static final EntityType<LavaTurtleEntity> LAVA_TURTLE = Registry.register(
                        BuiltInRegistries.ENTITY_TYPE,
                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "lavaturtle"),
                        EntityType.Builder.of(LavaTurtleEntity::new, MobCategory.CREATURE).sized(1.0f, 1.0f)
                                        .build("lavaturtle"));

        @Override
        public void onInitialize() {

                // This method is invoked by the Fabric mod loader when it is ready
                // to load your mod. You can access Fabric and Common code in this
                // project.

                // Use Fabric to bootstrap the Common mod.
                Constants.LOG.info("Hello Fabric world!");
                CommonClass.init();

                net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry.register(LAVA_TURTLE,
                                LavaTurtleEntity.createAttributes());

                net.minecraft.world.entity.SpawnPlacements.register(LAVA_TURTLE,
                                net.minecraft.world.entity.SpawnPlacementTypes.IN_LAVA,
                                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                                LavaTurtleEntity::checkLavaTurtleSpawnRules);

                net.fabricmc.fabric.api.biome.v1.BiomeModifications.addSpawn(
                                net.fabricmc.fabric.api.biome.v1.BiomeSelectors.foundInTheNether(),
                                net.minecraft.world.entity.MobCategory.CREATURE,
                                LAVA_TURTLE,
                                15, 1, 3);
        }
}
