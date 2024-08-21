package net.somyk.tntentitylimiter.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.somyk.tntentitylimiter.ModConfig.*;
import static net.somyk.tntentitylimiter.TntEntityLimiter.*;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin {

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V", at = @At("TAIL"))
    private void increaseCount(World world, double x, double y, double z, LivingEntity igniter, CallbackInfo ci){
        TntEntity tntEntity = (TntEntity) (Object) this;
        activeSet.add(tntEntity.getUuid());
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void stopTnt(CallbackInfo ci){
        TntEntity tntEntity = (TntEntity) (Object) this;
        if(activeSet.size() > getIntegerValue(maxPrimedTntAmount)){
//            PlayerEntity playerEntity = tntEntity.getWorld().getClosestPlayer(tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), -1, false);
//            if (playerEntity != null) playerEntity.giveItemStack(Items.TNT.getDefaultStack());
            tntEntity.kill();
            tntEntity.getWorld().setBlockState(tntEntity.getBlockPos(), Blocks.TNT.getDefaultState());
            activeSet.remove(tntEntity.getUuid());
        }
    }

    @Inject(method = "explode", at = @At("TAIL"))
    private void decreaseCount(CallbackInfo ci){
        TntEntity tntEntity = (TntEntity) (Object) this;
        activeSet.remove(tntEntity.getUuid());
    }
}
