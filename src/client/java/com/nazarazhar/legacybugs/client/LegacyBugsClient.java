/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.client;

import com.nazarazhar.legacybugs.client.network.LegacyBugsClientNetworking;
import com.nazarazhar.legacybugs.client.network.LegacyBugsClientState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class LegacyBugsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LegacyBugsClientNetworking.register();

		ClientPlayConnectionEvents.JOIN.register(
				(handler, sender, client) ->
						LegacyBugsClientNetworking.requestConfigState()
		);

		ClientPlayConnectionEvents.DISCONNECT.register(
				(handler, client) ->
						LegacyBugsClientState.clear()
		);
	}
}