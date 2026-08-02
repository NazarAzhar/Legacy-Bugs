/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nazarazhar.legacybugs.LegacyBugs;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class LegacyBugsConfig {
	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	private static final Path CONFIG_PATH = FabricLoader.getInstance()
			.getConfigDir()
			.resolve("legacy-bugs.json");

	private static LegacyBugsConfig INSTANCE = new LegacyBugsConfig();

	public boolean enabled = true;
	public boolean lb001GrindstoneMerge = true;
	public boolean lb002CraftingResultBug = true;

	private LegacyBugsConfig() {
	}

	public static void load() {
		if (!Files.exists(CONFIG_PATH)) {
			save();
			LegacyBugs.LOGGER.info("Created default Legacy Bugs config at {}", CONFIG_PATH);
			return;
		}

		try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
			LegacyBugsConfig loaded = GSON.fromJson(reader, LegacyBugsConfig.class);

			if (loaded == null) {
				LegacyBugs.LOGGER.warn("Legacy Bugs config was empty. Recreating default config.");
				INSTANCE = new LegacyBugsConfig();
				save();
				return;
			}

			INSTANCE = loaded;
			LegacyBugs.LOGGER.info("Loaded Legacy Bugs config from {}", CONFIG_PATH);
		} catch (Exception exception) {
			LegacyBugs.LOGGER.error("Failed to load Legacy Bugs config. Using default settings.", exception);
			INSTANCE = new LegacyBugsConfig();
			save();
		}
	}

	public static void save() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());

			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
				GSON.toJson(INSTANCE, writer);
			}
		} catch (IOException exception) {
			LegacyBugs.LOGGER.error("Failed to save Legacy Bugs config.", exception);
		}
	}

	public static boolean isEnabled() {
		return INSTANCE.enabled;
	}

	public static boolean isLb001RawEnabled() {
		return INSTANCE.lb001GrindstoneMerge;
	}

	public static boolean isLb002RawEnabled() {
		return INSTANCE.lb002CraftingResultBug;
	}

	public static boolean isLb001GrindstoneMergeEnabled() {
		return INSTANCE.enabled && INSTANCE.lb001GrindstoneMerge;
	}

	public static boolean isLb002CraftingResultBugEnabled() {
		return INSTANCE.enabled && INSTANCE.lb002CraftingResultBug;
	}

	public static void setEnabled(boolean enabled) {
		INSTANCE.enabled = enabled;
		save();
	}

	public static void setLb001GrindstoneMerge(boolean enabled) {
		INSTANCE.lb001GrindstoneMerge = enabled;
		save();
	}

	public static void setLb002CraftingResultBug(boolean enabled) {
		INSTANCE.lb002CraftingResultBug = enabled;
		save();
	}
}