/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.network;

import com.nazarazhar.legacybugs.config.LegacyBugsConfig;
import com.nazarazhar.legacybugs.network.payload.LegacyBugsConfigRequestPayload;
import com.nazarazhar.legacybugs.network.payload.LegacyBugsConfigStatePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class LegacyBugsNetworking {
	private LegacyBugsNetworking() {
	}

	public static void register() {
		PayloadTypeRegistry.serverboundPlay().register(
				LegacyBugsConfigRequestPayload.TYPE,
				LegacyBugsConfigRequestPayload.CODEC
		);

		PayloadTypeRegistry.clientboundPlay().register(
				LegacyBugsConfigStatePayload.TYPE,
				LegacyBugsConfigStatePayload.CODEC
		);

		ServerPlayNetworking.registerGlobalReceiver(
				LegacyBugsConfigRequestPayload.TYPE,
				(payload, context) -> sendConfigState(context.player())
		);
	}

	public static void sendConfigState(ServerPlayer player) {
		ServerPlayNetworking.send(
				player,
				new LegacyBugsConfigStatePayload(
						LegacyBugsConfig.isEnabled(),
						LegacyBugsConfig.isLb001RawEnabled(),
						LegacyBugsConfig.isLb002RawEnabled()
				)
		);
	}
}