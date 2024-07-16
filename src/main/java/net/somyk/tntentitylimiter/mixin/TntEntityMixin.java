package net.somyk.tntentitylimiter.mixin;

import net.minecraft.entity.TntEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.somyk.tntentitylimiter.ModConfig.*;
import static net.somyk.tntentitylimiter.TntEntityLimiter.*;

@Mixin(TntEntity.class)
public class TntEntityMixin {


    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void freezeTntEntity(CallbackInfo ci){
        TntEntity tntEntity = (TntEntity) (Object) this;
        if(activeSet.size() < getIntegerValue(maxPrimedTntAmount)) {
            activeSet.add(tntEntity.getUuid());
        }
        if (!activeSet.contains(tntEntity.getUuid())){
            if(tntEntity.getWorld() instanceof ServerWorld world) {
                world.getChunkManager().sendToNearbyPlayers(tntEntity,
                        new EntitySpawnS2CPacket(tntEntity, 1, tntEntity.getBlockPos()));
            }
            ci.cancel();
        }
    }

    @Inject(method = "explode", at = @At("TAIL"))
    private void decreaseCount(CallbackInfo ci){
        TntEntity tntEntity = (TntEntity) (Object) this;
        activeSet.remove(tntEntity.getUuid());
    }
}
