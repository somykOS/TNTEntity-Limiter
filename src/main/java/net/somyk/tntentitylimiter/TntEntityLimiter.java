package net.somyk.tntentitylimiter;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.server.world.ServerWorld;
import net.somyk.tntentitylimiter.command.ModifyMaxTntEntityAmount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static net.somyk.tntentitylimiter.ModConfig.*;

public class TntEntityLimiter implements ModInitializer {
	public static final String MOD_ID = "tnt-entity-limiter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Queue<UUID> activeSet = new ConcurrentLinkedQueue<>();
	public static Queue<Entity> queue = new ConcurrentLinkedQueue<>();

	@Override
	public void onInitialize() {
		ModConfig.load();
		CommandRegistrationCallback.EVENT.register(ModifyMaxTntEntityAmount::register);

		ServerEntityEvents.ENTITY_LOAD.register(this::onEntityLoad);
		ServerEntityEvents.ENTITY_UNLOAD.register(this::onEntityUnload);
	}

	private void onEntityLoad(Entity entity, ServerWorld serverWorld) {
		if(entity instanceof TntEntity tntEntity) {
			if (activeSet.size() < getIntegerValue(maxPrimedTntAmount)) {
				activeSet.add(tntEntity.getUuid());
			} else {
				queue.add(new TntEntity(tntEntity.getWorld(), tntEntity.prevX, tntEntity.prevY, tntEntity.prevZ, tntEntity.getOwner()));
				tntEntity.discard();
			}
		}
	}

	private void onEntityUnload(Entity entity, ServerWorld serverWorld) {
		if (entity instanceof TntEntity tntEntity) {
			activeSet.remove(tntEntity.getUuid());
			if (activeSet.size() < getIntegerValue(maxPrimedTntAmount)) {
				Entity nextTNT = queue.poll();
				if (nextTNT != null && !nextTNT.isRemoved()) {
					serverWorld.spawnEntity(nextTNT);
				}
			}
		}
	}
}