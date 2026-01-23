package net.whateclipse.lavaturtlemod.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.whateclipse.lavaturtlemod.block.LavaTurtleEggBlock;

public class ModBlocks {
    public static final Block LAVA_TURTLE_EGG = new LavaTurtleEggBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(0.5F)
                    .sound(net.minecraft.world.level.block.SoundType.METAL).randomTicks().noOcclusion());
}
