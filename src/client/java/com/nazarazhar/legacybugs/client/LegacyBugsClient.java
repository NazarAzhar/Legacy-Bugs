/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.nazarazhar.legacybugs.LegacyBugs;
import com.nazarazhar.legacybugs.client.gui.LegacyBugsConfigScreen;
import com.nazarazhar.legacybugs.client.network.LegacyBugsClientNetworking;
import com.nazarazhar.legacybugs.client.network.LegacyBugsClientState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.KeyMapping;

public class LegacyBugsClient implements ClientModInitializer {

	private static final KeyMapping.Category LEGACY_BUGS_CATEGORY =
			KeyMapping.Category.register(
					LegacyBugs.id("controls")
			);

	private static final KeyMapping OPEN_CONFIG_KEY =
			KeyMappingHelper.registerKeyMapping(
					new KeyMapping(
							"key.legacy-bugs.open_config",
							InputConstants.Type.KEYSYM,
							InputConstants.KEY_MINUS,
							LEGACY_BUGS_CATEGORY
					)
			);

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

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (OPEN_CONFIG_KEY.consumeClick()) {
				if (client.player == null || client.level == null) {
					continue;
				}

				client.gui.setScreen(
						new LegacyBugsConfigScreen()
				);
			}
		});
	}
}