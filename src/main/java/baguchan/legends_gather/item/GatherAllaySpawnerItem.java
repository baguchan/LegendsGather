package baguchan.legends_gather.item;

import baguchan.legends_gather.entity.GatherAllay;
import baguchan.legends_gather.registry.ModEntities;
import baguchan.legends_gather.registry.ModMemorys;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;
import java.util.List;

public class GatherAllaySpawnerItem extends Item {
    public GatherAllaySpawnerItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        ItemStack itemstack = player.getItemInHand(p_41434_);

        Vec3 vec3 = player.getEyePosition();
        Vec3 vec31 = player.getViewVector(1.0F);
        double d0 = player.getEntityReach();
        Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
        float f = 1.0F;
        AABB aabb = player.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, vec3, vec32, aabb, (p_234237_) -> {
            return !p_234237_.isSpectator() && p_234237_.getType() == EntityType.ALLAY;
        }, d0 * d0);

        AABB aabbForAllay = player.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);

        List<GatherAllay> listWorkerAllay = level.getEntities(EntityTypeTest.forClass(GatherAllay.class), aabbForAllay, living -> {
            return living.isReturnOwner(player);
        });

        if (entityhitresult != null && getCapturedAllay(itemstack) < 4) {
            Entity entity = entityhitresult.getEntity();
            if(entity instanceof Allay allay){
                if (!level.isClientSide) {
                    allay.getInventory().removeAllItems().forEach(player::spawnAtLocation);
                    ItemStack allayInventory = allay.getItemBySlot(EquipmentSlot.MAINHAND);
                    if (!allayInventory.isEmpty() && !EnchantmentHelper.hasVanishingCurse(allayInventory)) {
                        player.spawnAtLocation(allayInventory);
                        allay.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    }
                }
                allay.discard();

                player.getCooldowns().addCooldown(this, 40);
                player.playSound(SoundEvents.ITEM_PICKUP);

                addAllay(itemstack, 1);
                return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
            }
        }else {
            HitResult hitResult = player.pick(20.0D, 0.0F, false);

            Vec3 pos = hitResult.getLocation();
            if (hitResult.getType() != HitResult.Type.MISS && !player.isShiftKeyDown()) {
                if (getCurrentAllay(itemstack) > 0) {

                    hitResult = BlockHitResult.miss(pos, Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(pos));


                    if (hitResult instanceof BlockHitResult blockHitResult) {
                        BlockPos blockpos = blockHitResult.getBlockPos();
                        if (!level.isClientSide) {
                            GatherAllay thrownpotion = ModEntities.GATHER_ALLAY.get().create(level);
                            thrownpotion.getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player.getUUID());
                            thrownpotion.setPos(player.position());
                            thrownpotion.getBrain().setMemory(ModMemorys.WORK_POS.get(), GlobalPos.of(level.dimension(), blockpos));
                            level.addFreshEntity(thrownpotion);
                        }
                        removeAllay(itemstack, 1);

                        player.playSound(SoundEvents.ALLAY_ITEM_GIVEN);
                        player.awardStat(Stats.ITEM_USED.get(this));
                        player.getCooldowns().addCooldown(this, 40);


                        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                    }
                }
            }else  if(!listWorkerAllay.isEmpty() && player.isShiftKeyDown()){
                for(GatherAllay gatherAllay: listWorkerAllay){
                    gatherAllay.giveResource();
                    addAllay(itemstack, 1);
                    gatherAllay.discard();
                }
                player.getCooldowns().addCooldown(this, 100);
                player.playSound(SoundEvents.ALLAY_ITEM_TAKEN);

                return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
            }
        }

        return super.use(level, player, p_41434_);
    }

    public void appendHoverText(ItemStack p_40880_, @Nullable Level p_40881_, List<Component> p_40882_, TooltipFlag p_40883_) {
        p_40882_.add(Component.literal(getCapturedAllay(p_40880_) + "").withStyle(ChatFormatting.DARK_AQUA).append(" ").append(Component.translatable("item.legends_gather.gather_allay_spawner.tooltip")));

        super.appendHoverText(p_40880_, p_40881_, p_40882_, p_40883_);
    }


    public static void addAllay(ItemStack p_40885_, int allay) {
        CompoundTag compoundtag = p_40885_.getOrCreateTag();
        int i = allay;
        if(compoundtag.contains("Allays")){
            i += compoundtag.getInt("Allays");
        }

        compoundtag.putInt("Allays", i);
        compoundtag.putInt("CapturedAllays", i);
    }

    public static void setAllay(ItemStack p_40885_, int allay) {
        CompoundTag compoundtag = p_40885_.getOrCreateTag();
        int i = allay;

        compoundtag.putInt("Allays", i);
    }

    public static boolean removeAllay(ItemStack p_40885_, int allay) {
        CompoundTag compoundtag = p_40885_.getOrCreateTag();
        if(compoundtag.contains("Allays")){
            if(compoundtag.getInt("Allays") > 0) {
                compoundtag.putInt("Allays", compoundtag.getInt("Allays") - allay);
                return true;
            }
        }

       return false;
    }

    public static int getCurrentAllay(ItemStack p_40885_) {
        CompoundTag compoundtag = p_40885_.getOrCreateTag();
        if(compoundtag.contains("Allays")){
         return compoundtag.getInt("Allays");
        }

        return 0;
    }

    public static int getCapturedAllay(ItemStack p_40885_) {
        CompoundTag compoundtag = p_40885_.getOrCreateTag();
        if (compoundtag.contains("CapturedAllays")) {
            return compoundtag.getInt("CapturedAllays");
        }

        return 0;
    }
}
