package baguchan.legends_gather.entity.ai;

import baguchan.legends_gather.entity.AbstractWorkerAllay;
import baguchan.legends_gather.entity.GatherAllayAi;
import baguchan.legends_gather.registry.ModMemorys;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;

public class WalkToPlayer extends Behavior<AbstractWorkerAllay> {
    private final float speedMultiplier;

    public WalkToPlayer(float p_275357_) {
        super(ImmutableMap.of(MemoryModuleType.LIKED_PLAYER, MemoryStatus.VALUE_PRESENT));
        this.speedMultiplier = p_275357_;
    }

    protected void tick(ServerLevel level, AbstractWorkerAllay mob, long p_147405_) {
        Brain<?> brain = mob.getBrain();
        Optional<PositionTracker> positionTracker = GatherAllayAi.getLikedPlayerPositionTracker(mob);

        if (positionTracker.isPresent()) {
            positionTracker.ifPresent((p_217206_) -> {
                BehaviorUtils.setWalkAndLookTargetMemories(mob, p_217206_, this.speedMultiplier, 3);
                if(mob.distanceToSqr(p_217206_.currentPosition()) < 8.0F){
                    mob.giveResource();
                }
            });
        }
    }
}