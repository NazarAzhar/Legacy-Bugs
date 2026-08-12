/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.network;

import com.nazarazhar.legacybugs.LegacyBugs;
import com.nazarazhar.legacybugs.config.LegacyBugsConfig;
import com.nazarazhar.legacybugs.network.payload.LegacyBugsConfigRequestPayload;
import com.nazarazhar.legacybugs.network.payload.LegacyBugsConfigStatePayload;
import com.nazarazhar.legacybugs.network.payload.LegacyBugsConfigUpdatePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionLevel;

public final class LegacyBugsNetworking {
	private LegacyBugsNetworking() {
	}

	public static void register() {
		PayloadTypeRegistry.serverboundPlay().register(
				LegacyBugsConfigRequestPayload.TYPE,
				LegacyBugsConfigRequestPayload.CODEC
		);

		PayloadTypeRegistry.serverboundPlay().register(
				LegacyBugsConfigUpdatePayload.TYPE,
				LegacyBugsConfigUpdatePayload.CODEC
		);

		PayloadTypeRegistry.clientboundPlay().register(
				LegacyBugsConfigStatePayload.TYPE,
				LegacyBugsConfigStatePayload.CODEC
		);

		ServerPlayNetworking.registerGlobalReceiver(
				LegacyBugsConfigRequestPayload.TYPE,
				(payload, context) ->
						sendConfigState(context.player(), context.server())
		);

		ServerPlayNetworking.registerGlobalReceiver(
				LegacyBugsConfigUpdatePayload.TYPE,
				(payload, context) -> {
					ServerPlayer player = context.player();
					MinecraftServer server = context.server();

					if (!canEdit(player, server)) {
						sendConfigState(player, server);
						return;
					}

					switch (payload.target()) {
						case LegacyBugsConfigUpdatePayload.TARGET_GLOBAL ->
								LegacyBugsConfig.setEnabled(payload.enabled());

						case LegacyBugsConfigUpdatePayload.TARGET_LB001 ->
								LegacyBugsConfig.setLb001GrindstoneMerge(payload.enabled());

						case LegacyBugsConfigUpdatePayload.TARGET_LB002 ->
								LegacyBugsConfig.setLb002CraftingResultBug(payload.enabled());

						default -> {
							sendConfigState(player, server);
							return;
						}
					}

					sendConfigState(player, server);
				}
		);
	}

	public static void sendConfigState(
			ServerPlayer player,
			MinecraftServer server
	) {
		ServerPlayNetworking.send(
				player,
				new LegacyBugsConfigStatePayload(
						LegacyBugsConfig.isEnabled(),
						LegacyBugsConfig.isLb001RawEnabled(),
						LegacyBugsConfig.isLb002RawEnabled(),
						canEdit(player, server)
				)
		);
	}

	private static boolean canEdit(
			ServerPlayer player,
			MinecraftServer server
	) {
		/*
		 * Singleplayer / integrated server:
		 * Always allow the world owner to change Legacy Bugs settings.
		 */
		if (!(server instanceof DedicatedServer)) {
			return true;
		}

		/*
		 * Dedicated server:
		 * Only operators / gamemasters can change Legacy Bugs settings.
		 */
		return player.checkPermission(
				LegacyBugs.id("configure"),
				PermissionLevel.GAMEMASTERS
		);
	}
}