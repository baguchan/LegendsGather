package baguchan.legends_gather.registry;

import baguchan.legends_gather.LegendsGather;
import baguchan.legends_gather.item.GatherAllaySpawnerItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LegendsGather.MODID);

    public static final RegistryObject<Item> ALLAY_SPAWNER = ITEMS.register("gather_allay_spawner", () -> new GatherAllaySpawnerItem((new Item.Properties())));

}
