package baguchan.legends_gather.client;

import baguchan.legends_gather.LegendsGather;
import baguchan.legends_gather.entity.ai.GatherBlocks;
import baguchan.legends_gather.registry.ModItems;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = LegendsGather.MODID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerEntityRenders(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            LocalPlayer player = Minecraft.getInstance().player;
            Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;
            if (player != null && camera != null && player.isHolding(ModItems.ALLAY_SPAWNER.get())) {
                HitResult hitResult = player.pick(20.0D, 0.0F, false);

                Vec3 pos = hitResult.getLocation();
                if (hitResult.getType() != HitResult.Type.MISS) {


                    VertexConsumer vertexconsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
                    LevelRenderer.renderLineBox(event.getPoseStack(), vertexconsumer, (double) pos.x - GatherBlocks.RANGE - camera.getPosition().x, (double) pos.y - GatherBlocks.RANGE - camera.getPosition().y, (double) pos.z - GatherBlocks.RANGE - camera.getPosition().z, (double) (pos.x + GatherBlocks.RANGE) - camera.getPosition().x, (double) (pos.y + GatherBlocks.RANGE) - camera.getPosition().y, (double) (pos.z + GatherBlocks.RANGE) - camera.getPosition().z, 0.3F, 0.3F, 1.0F, 1.0F);


                }
            }
        }
    }

}
