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

public record LegacyBugsConfigUpdatePayload(
		int target,
		boolean enabled
) implements CustomPacketPayload {

	public static final int TARGET_GLOBAL = 0;
	public static final int TARGET_LB001 = 1;
	public static final int TARGET_LB002 = 2;

	public static final Type<LegacyBugsConfigUpdatePayload> TYPE =
			new Type<>(LegacyBugs.id("config_update"));

	public static final StreamCodec<RegistryFriendlyByteBuf, LegacyBugsConfigUpdatePayload> CODEC =
			StreamCodec.composite(
					ByteBufCodecs.VAR_INT,
					LegacyBugsConfigUpdatePayload::target,
					ByteBufCodecs.BOOL,
					LegacyBugsConfigUpdatePayload::enabled,
					LegacyBugsConfigUpdatePayload::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}