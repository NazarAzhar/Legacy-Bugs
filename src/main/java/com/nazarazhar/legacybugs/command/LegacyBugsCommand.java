/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.nazarazhar.legacybugs.config.LegacyBugsConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

public final class LegacyBugsCommand {
	private LegacyBugsCommand() {
	}

	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				register(dispatcher, environment));
	}

	private static void register(
			CommandDispatcher<CommandSourceStack> dispatcher,
			Commands.CommandSelection environment
	) {
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("legacybugs");

		/*
		 * Singleplayer / integrated server:
		 * The world owner can always use Legacy Bugs commands.
		 *
		 * Dedicated server:
		 * Only operators/moderators can use Legacy Bugs commands.
		 */
		if (environment.includeDedicated) {
			root.requires(source ->
					source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR));
		}

		root.then(Commands.literal("status")
						.executes(context -> status(context.getSource())))
				.then(Commands.literal("enable")
						.executes(context -> setGlobal(context.getSource(), true)))
				.then(Commands.literal("disable")
						.executes(context -> setGlobal(context.getSource(), false)))
				.then(Commands.literal("lb001")
						.then(Commands.literal("enable")
								.executes(context -> setLb001(context.getSource(), true)))
						.then(Commands.literal("disable")
								.executes(context -> setLb001(context.getSource(), false))))
				.then(Commands.literal("lb002")
						.then(Commands.literal("enable")
								.executes(context -> setLb002(context.getSource(), true)))
						.then(Commands.literal("disable")
								.executes(context -> setLb002(context.getSource(), false))));

		dispatcher.register(root);
	}

	private static int status(CommandSourceStack source) {
		source.sendSuccess(
				() -> Component.literal(
						"Legacy Bugs: " +
								enabledText(LegacyBugsConfig.isEnabled())),
				false
		);

		source.sendSuccess(
				() -> Component.literal(
						"LB-001 Grindstone Merge: " +
								enabledText(LegacyBugsConfig.isLb001RawEnabled())),
				false
		);

		source.sendSuccess(
				() -> Component.literal(
						"LB-002 Crafting Result Bug: " +
								enabledText(LegacyBugsConfig.isLb002RawEnabled())),
				false
		);

		return 1;
	}

	private static int setGlobal(CommandSourceStack source, boolean enabled) {
		LegacyBugsConfig.setEnabled(enabled);

		source.sendSuccess(
				() -> Component.literal(
						"Legacy Bugs is now " + enabledText(enabled) + "."),
				true
		);

		return 1;
	}

	private static int setLb001(CommandSourceStack source, boolean enabled) {
		LegacyBugsConfig.setLb001GrindstoneMerge(enabled);

		source.sendSuccess(
				() -> Component.literal(
						"LB-001 Grindstone Merge is now " +
								enabledText(enabled) + "."),
				true
		);

		return 1;
	}

	private static int setLb002(CommandSourceStack source, boolean enabled) {
		LegacyBugsConfig.setLb002CraftingResultBug(enabled);

		source.sendSuccess(
				() -> Component.literal(
						"LB-002 Crafting Result Bug is now " +
								enabledText(enabled) + "."),
				true
		);

		return 1;
	}

	private static String enabledText(boolean enabled) {
		return enabled ? "enabled" : "disabled";
	}
}