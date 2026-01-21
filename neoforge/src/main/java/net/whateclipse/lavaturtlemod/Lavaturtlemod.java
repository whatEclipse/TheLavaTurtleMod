package net.whateclipse.lavaturtlemod;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Lavaturtlemod {

    public Lavaturtlemod(IEventBus eventBus) {
       Constants.LOG.info("Hello NeoForge world!");
       CommonClass.init();
    }
}
