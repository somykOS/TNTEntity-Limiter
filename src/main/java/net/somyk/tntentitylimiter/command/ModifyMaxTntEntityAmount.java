package net.somyk.tntentitylimiter.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.somyk.tntentitylimiter.ModConfig.*;
import static net.minecraft.server.command.CommandManager.*;
import static net.somyk.tntentitylimiter.TntEntityLimiter.*;

public class ModifyMaxTntEntityAmount {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(literal(MOD_ID)
                .requires(source -> Permissions.check(source, MOD_ID+".modify") || source.hasPermissionLevel(2))
                .then(argument("value", IntegerArgumentType.integer(0))
                        .executes(context -> {
                            try {
                                return run(context, IntegerArgumentType.getInteger(context,"value"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                )
        );
    }

    private static int run(CommandContext<ServerCommandSource> context, int value) {
        setValue(maxPrimedTntAmount, value);
        context.getSource().sendFeedback(() -> Text.literal(String.format("Successfully changed the maximum amount of primed tnt to %d", value)),true);
        return 1;
    }
}
