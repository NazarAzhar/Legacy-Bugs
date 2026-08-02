/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs;

import com.nazarazhar.legacybugs.command.LegacyBugsCommand;
import com.nazarazhar.legacybugs.config.LegacyBugsConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LegacyBugs implements ModInitializer {
	public static final String MOD_ID = "legacy-bugs";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LegacyBugsConfig.load();
		LegacyBugsCommand.register();

		LOGGER.info("Legacy Bugs initialized.");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}