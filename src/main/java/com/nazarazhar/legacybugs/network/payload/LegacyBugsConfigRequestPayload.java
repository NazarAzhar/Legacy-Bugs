/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.network.payload;

import com.nazarazhar.legacybugs.LegacyBugs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record LegacyBugsConfigRequestPayload() implements CustomPacketPayload {
	public static final Type<LegacyBugsConfigRequestPayload> TYPE =
			new Type<>(LegacyBugs.id("config_request"));

	public static final StreamCodec<RegistryFriendlyByteBuf, LegacyBugsConfigRequestPayload> CODEC =
			StreamCodec.unit(new LegacyBugsConfigRequestPayload());

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}