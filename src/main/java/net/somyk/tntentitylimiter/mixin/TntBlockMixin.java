package net.somyk.tntentitylimiter.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static net.somyk.tntentitylimiter.ModConfig.*;
import static net.somyk.tntentitylimiter.TntEntityLimiter.activeSet;

@Mixin(net.minecraft.block.TntBlock.class)
public class TntBlockMixin {

	@ModifyArg(method = "primeTnt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private static Entity primeTnt(Entity tntEntity) {
        if (activeSet.size() < getIntegerValue(maxPrimedTntAmount)) {
            activeSet.add(tntEntity.getUuid());
        }
        return tntEntity;
    }
}