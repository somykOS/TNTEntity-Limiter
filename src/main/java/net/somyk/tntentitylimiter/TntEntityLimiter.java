package net.somyk.tntentitylimiter;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.somyk.tntentitylimiter.command.ModifyMaxTntEntityAmount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TntEntityLimiter implements ModInitializer {
	public static final String MOD_ID = "tntentitylimiter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Set<UUID> activeSet = new HashSet<>();

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(ModifyMaxTntEntityAmount::register);
	}
}