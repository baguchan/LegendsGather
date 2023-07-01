package baguchan.legends_gather;

import baguchan.legends_gather.registry.ModEntities;
import baguchan.legends_gather.registry.ModItems;
import baguchan.legends_gather.registry.ModMemorys;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LegendsGather.MODID)
public class LegendsGather
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "legends_gather";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public LegendsGather()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModMemorys.MEMORY_REGISTRY.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES_REGISTRY.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

 }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }
}
