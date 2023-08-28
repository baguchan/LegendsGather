package baguchan.legends_gather;

import baguchan.legends_gather.registry.ModItems;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LegendsGather.MODID)
public class CommonEvents {
    @SubscribeEvent
    public static void cancelAllayInterect(PlayerInteractEvent.EntityInteract event) {
        if (event.getItemStack().is(ModItems.ALLAY_SPAWNER.get())) {
            event.setCancellationResult(InteractionResult.PASS);
            event.setCanceled(true);
        }
    }
}
