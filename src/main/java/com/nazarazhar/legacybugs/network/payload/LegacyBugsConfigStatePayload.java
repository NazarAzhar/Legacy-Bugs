/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.network.payload;

import com.nazarazhar.legacybugs.LegacyBugs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record LegacyBugsConfigStatePayload(
		boolean enabled,
		boolean lb001Enabled,
		boolean lb002Enabled
) implements CustomPacketPayload {

	public static final Type<LegacyBugsConfigStatePayload> TYPE =
			new Type<>(LegacyBugs.id("config_state"));

	public static final StreamCodec<RegistryFriendlyByteBuf, LegacyBugsConfigStatePayload> CODEC =
			StreamCodec.composite(
					ByteBufCodecs.BOOL,
					LegacyBugsConfigStatePayload::enabled,
					ByteBufCodecs.BOOL,
					LegacyBugsConfigStatePayload::lb001Enabled,
					ByteBufCodecs.BOOL,
					LegacyBugsConfigStatePayload::lb002Enabled,
					LegacyBugsConfigStatePayload::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}