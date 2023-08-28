package baguchan.legends_gather.registry;

import baguchan.legends_gather.LegendsGather;
import baguchan.legends_gather.item.GatherAllaySpawnerItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = LegendsGather.MODID)
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LegendsGather.MODID);

    public static final RegistryObject<Item> ALLAY_SPAWNER = ITEMS.register("gather_allay_spawner", () -> new GatherAllaySpawnerItem((new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> VEX_SCRIPT = ITEMS.register("vex_script", () -> new Item((new Item.Properties())));


    @SubscribeEvent
    public static void registerCreativeTabsItem(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.ALLAY_SPAWNER.get());
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.VEX_SCRIPT.get());
        }
    }
}
