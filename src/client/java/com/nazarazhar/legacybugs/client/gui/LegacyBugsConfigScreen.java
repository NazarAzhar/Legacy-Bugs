/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.client.gui;

import com.nazarazhar.legacybugs.client.network.LegacyBugsClientNetworking;
import com.nazarazhar.legacybugs.client.network.LegacyBugsClientState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class LegacyBugsConfigScreen extends Screen {
	private final Screen parent;

	private static final int PANEL_WIDTH = 420;
	private static final int PANEL_HEIGHT = 214;

	private static final int TOGGLE_BUTTON_WIDTH = 92;
	private static final int TOGGLE_BUTTON_HEIGHT = 20;

	private static final int ACTION_BUTTON_WIDTH = 110;
	private static final int ACTION_BUTTON_HEIGHT = 20;

	private static final int ROW_SPACING = 34;

	private Button globalButton;
	private Button lb001Button;
	private Button lb002Button;
	private Button refreshButton;

	public LegacyBugsConfigScreen() {
		this(null);
	}

	public LegacyBugsConfigScreen(Screen parent) {
		super(Component.literal("Legacy Bugs"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int panelX = (this.width - PANEL_WIDTH) / 2;
		int panelY = (this.height - PANEL_HEIGHT) / 2;

		int row1Y = panelY + 56;
		int row2Y = row1Y + ROW_SPACING;
		int row3Y = row2Y + ROW_SPACING;

		int toggleX = panelX + PANEL_WIDTH - 110;

		this.globalButton = this.addRenderableWidget(
				Button.builder(
						globalButtonText(),
						button -> {
							if (!LegacyBugsClientState.canEdit()) {
								return;
							}

							LegacyBugsClientNetworking.setGlobalEnabled(
									!LegacyBugsClientState.isEnabled()
							);
						}
				).bounds(
						toggleX,
						row1Y - 4,
						TOGGLE_BUTTON_WIDTH,
						TOGGLE_BUTTON_HEIGHT
				).build()
		);

		this.lb001Button = this.addRenderableWidget(
				Button.builder(
						lb001ButtonText(),
						button -> {
							if (!LegacyBugsClientState.canEdit()) {
								return;
							}

							LegacyBugsClientNetworking.setLb001Enabled(
									!LegacyBugsClientState.isLb001Enabled()
							);
						}
				).bounds(
						toggleX,
						row2Y - 4,
						TOGGLE_BUTTON_WIDTH,
						TOGGLE_BUTTON_HEIGHT
				).build()
		);

		this.lb002Button = this.addRenderableWidget(
				Button.builder(
						lb002ButtonText(),
						button -> {
							if (!LegacyBugsClientState.canEdit()) {
								return;
							}

							LegacyBugsClientNetworking.setLb002Enabled(
									!LegacyBugsClientState.isLb002Enabled()
							);
						}
				).bounds(
						toggleX,
						row3Y - 4,
						TOGGLE_BUTTON_WIDTH,
						TOGGLE_BUTTON_HEIGHT
				).build()
		);

		this.refreshButton = this.addRenderableWidget(
				Button.builder(
						Component.literal("Refresh"),
						button -> LegacyBugsClientNetworking.requestConfigState()
				).bounds(
						panelX + 96,
						panelY + PANEL_HEIGHT - 30,
						ACTION_BUTTON_WIDTH,
						ACTION_BUTTON_HEIGHT
				).build()
		);

		this.addRenderableWidget(
				Button.builder(
						Component.literal("Done"),
						button -> this.onClose()
				).bounds(
						panelX + PANEL_WIDTH - 96 - ACTION_BUTTON_WIDTH,
						panelY + PANEL_HEIGHT - 30,
						ACTION_BUTTON_WIDTH,
						ACTION_BUTTON_HEIGHT
				).build()
		);

		refreshButtonStates();
		LegacyBugsClientNetworking.requestConfigState();
	}

	@Override
	public void tick() {
		super.tick();
		refreshButtonStates();
	}

	private void refreshButtonStates() {
		if (this.globalButton == null
				|| this.lb001Button == null
				|| this.lb002Button == null
				|| this.refreshButton == null) {
			return;
		}

		this.globalButton.setMessage(globalButtonText());
		this.lb001Button.setMessage(lb001ButtonText());
		this.lb002Button.setMessage(lb002ButtonText());

		boolean hasState = LegacyBugsClientState.hasReceivedState();
		boolean canEdit = hasState && LegacyBugsClientState.canEdit();

		this.globalButton.active = canEdit;
		this.lb001Button.active = canEdit;
		this.lb002Button.active = canEdit;
		this.refreshButton.active = true;
	}

	private Component globalButtonText() {
		return toggleText(LegacyBugsClientState.isEnabled());
	}

	private Component lb001ButtonText() {
		return toggleText(LegacyBugsClientState.isLb001Enabled());
	}

	private Component lb002ButtonText() {
		return toggleText(LegacyBugsClientState.isLb002Enabled());
	}

	private Component toggleText(boolean enabled) {
		return Component.literal(enabled ? "TRUE" : "FALSE")
				.withStyle(
						enabled ? ChatFormatting.GREEN : ChatFormatting.RED
				);
	}

	@Override
	public void onClose() {
		if (this.parent != null) {
			this.minecraft.gui.setScreen(this.parent);
		} else {
			super.onClose();
		}
	}

	@Override
	public void extractBackground(
			GuiGraphicsExtractor graphics,
			int mouseX,
			int mouseY,
			float delta
	) {
		super.extractBackground(graphics, mouseX, mouseY, delta);

		int panelX = (this.width - PANEL_WIDTH) / 2;
		int panelY = (this.height - PANEL_HEIGHT) / 2;

		int panelRight = panelX + PANEL_WIDTH;
		int panelBottom = panelY + PANEL_HEIGHT;

		// Main panel
		graphics.fill(
				panelX,
				panelY,
				panelRight,
				panelBottom,
				0xC0101010
		);

		// Header strip
		graphics.fill(
				panelX,
				panelY,
				panelRight,
				panelY + 24,
				0xD0202020
		);

		// Top separator
		graphics.fill(
				panelX + 8,
				panelY + 28,
				panelRight - 8,
				panelY + 29,
				0x80FFFFFF
		);

		// Bottom separator
		graphics.fill(
				panelX + 8,
				panelY + 154,
				panelRight - 8,
				panelY + 155,
				0x50FFFFFF
		);
	}

	@Override
	public void extractRenderState(
			GuiGraphicsExtractor graphics,
			int mouseX,
			int mouseY,
			float delta
	) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);

		int panelX = (this.width - PANEL_WIDTH) / 2;
		int panelY = (this.height - PANEL_HEIGHT) / 2;

		int titleCenterX = this.width / 2;

		int row1Y = panelY + 56;
		int row2Y = row1Y + ROW_SPACING;
		int row3Y = row2Y + ROW_SPACING;

		int labelX = panelX + 18;

		// Title
		Component titleComponent =
				this.title.copy().withStyle(
						ChatFormatting.GOLD,
						ChatFormatting.BOLD
				);

		graphics.text(
				this.font,
				titleComponent,
				titleCenterX - this.font.width(titleComponent) / 2,
				panelY + 8,
				0xFFFFFFFF,
				true
		);

		// Subtitle
		String subtitle = "Historical Bug Control Panel";

		graphics.text(
				this.font,
				subtitle,
				titleCenterX - this.font.width(subtitle) / 2,
				panelY + 34,
				0xFFCCCCCC,
				true
		);

		// Global Toggle
		graphics.text(
				this.font,
				"Global Toggle",
				labelX,
				row1Y + 2,
				0xFFFFFFFF,
				true
		);

		graphics.text(
				this.font,
				"Master switch for all Legacy Bugs behavior",
				labelX,
				row1Y + 13,
				0xFFAAAAAA,
				true
		);

		// LB-001
		graphics.text(
				this.font,
				"LB-001  Grindstone Enchantment Merge",
				labelX,
				row2Y + 2,
				0xFFFFFFFF,
				true
		);

		graphics.text(
				this.font,
				"Restores the 24w11a grindstone merge behavior",
				labelX,
				row2Y + 13,
				0xFFAAAAAA,
				true
		);

		// LB-002
		graphics.text(
				this.font,
				"LB-002  Crafting Result Bug",
				labelX,
				row3Y + 2,
				0xFFFFFFFF,
				true
		);

		graphics.text(
				this.font,
				"Restores the 24w18a crafting remainder bug",
				labelX,
				row3Y + 13,
				0xFFAAAAAA,
				true
		);

		// Status
		String statusText;
		int statusColor;

		if (!LegacyBugsClientState.hasReceivedState()) {
			statusText = "Status: Waiting for server...";
			statusColor = 0xFFAAAAAA;
		} else if (LegacyBugsClientState.canEdit()) {
			statusText = "Status: Editable";
			statusColor = 0xFF55FF55;
		} else {
			statusText = "Status: View only (operator permission required)";
			statusColor = 0xFFFF7777;
		}

		graphics.text(
				this.font,
				statusText,
				panelX + 16,
				panelY + PANEL_HEIGHT - 48,
				statusColor,
				true
		);
	}
}