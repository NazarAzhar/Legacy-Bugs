/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.client.network;

import com.nazarazhar.legacybugs.network.payload.LegacyBugsConfigRequestPayload;
import com.nazarazhar.legacybugs.network.payload.LegacyBugsConfigStatePayload;
import com.nazarazhar.legacybugs.network.payload.LegacyBugsConfigUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class LegacyBugsClientNetworking {
	private LegacyBugsClientNetworking() {
	}

	public static void register() {
		ClientPlayNetworking.registerGlobalReceiver(
				LegacyBugsConfigStatePayload.TYPE,
				(payload, context) -> LegacyBugsClientState.update(
						payload.enabled(),
						payload.lb001Enabled(),
						payload.lb002Enabled(),
						payload.canEdit()
				)
		);
	}

	public static void requestConfigState() {
		if (!ClientPlayNetworking.canSend(LegacyBugsConfigRequestPayload.TYPE)) {
			return;
		}

		ClientPlayNetworking.send(
				new LegacyBugsConfigRequestPayload()
		);
	}

	public static void setGlobalEnabled(boolean enabled) {
		sendUpdate(
				LegacyBugsConfigUpdatePayload.TARGET_GLOBAL,
				enabled
		);
	}

	public static void setLb001Enabled(boolean enabled) {
		sendUpdate(
				LegacyBugsConfigUpdatePayload.TARGET_LB001,
				enabled
		);
	}

	public static void setLb002Enabled(boolean enabled) {
		sendUpdate(
				LegacyBugsConfigUpdatePayload.TARGET_LB002,
				enabled
		);
	}

	private static void sendUpdate(int target, boolean enabled) {
		if (!ClientPlayNetworking.canSend(LegacyBugsConfigUpdatePayload.TYPE)) {
			return;
		}

		ClientPlayNetworking.send(
				new LegacyBugsConfigUpdatePayload(
						target,
						enabled
				)
		);
	}
}