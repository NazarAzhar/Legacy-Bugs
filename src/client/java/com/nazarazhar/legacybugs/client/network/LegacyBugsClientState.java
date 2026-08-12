/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.client.network;

public final class LegacyBugsClientState {
	private static boolean enabled;
	private static boolean lb001Enabled;
	private static boolean lb002Enabled;
	private static boolean canEdit;
	private static boolean receivedState;

	private LegacyBugsClientState() {
	}

	public static void update(
			boolean enabled,
			boolean lb001Enabled,
			boolean lb002Enabled,
			boolean canEdit
	) {
		LegacyBugsClientState.enabled = enabled;
		LegacyBugsClientState.lb001Enabled = lb001Enabled;
		LegacyBugsClientState.lb002Enabled = lb002Enabled;
		LegacyBugsClientState.canEdit = canEdit;
		LegacyBugsClientState.receivedState = true;
	}

	public static void clear() {
		enabled = false;
		lb001Enabled = false;
		lb002Enabled = false;
		canEdit = false;
		receivedState = false;
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static boolean isLb001Enabled() {
		return lb001Enabled;
	}

	public static boolean isLb002Enabled() {
		return lb002Enabled;
	}

	public static boolean canEdit() {
		return canEdit;
	}

	public static boolean hasReceivedState() {
		return receivedState;
	}
}