/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.mixin.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public abstract class CraftingResultBugMixin {
    @Shadow
    @Final
    private CraftingContainer craftSlots;

    @Shadow
    @Final
    private Player player;

    @Shadow
    protected abstract void checkTakeAchievements(ItemStack carried);

    @Shadow
    private NonNullList<ItemStack> getRemainingItems(CraftingInput input, Level level) {
        throw new AssertionError();
    }

    @Inject(
            method = "onTake",
            at = @At("HEAD"),
            cancellable = true
    )
    private void legacybugs$restore24w18aCraftingRemainderBug(
            Player takingPlayer,
            ItemStack carried,
            CallbackInfo ci
    ) {
        this.checkTakeAchievements(carried);

        CraftingInput.Positioned positionedRecipe = this.craftSlots.asPositionedCraftInput();
        CraftingInput input = positionedRecipe.input();
        NonNullList<ItemStack> remaining = this.getRemainingItems(input, takingPlayer.level());

        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x < input.width(); x++) {
                /*
                 * 24w18a-style bug:
                 *
                 * The recipe input is already trimmed to its active width and height,
                 * but the remaining items are applied starting from the top-left
                 * crafting grid slot instead of using the recipe's original left/top
                 * position.
                 *
                 * Modern 26.1.2 vanilla fixes this by adding recipeLeft and recipeTop.
                 */
                int slot = x + y * this.craftSlots.getWidth();

                ItemStack itemStack = this.craftSlots.getItem(slot);
                ItemStack replacement = remaining.get(x + y * input.width());

                if (!itemStack.isEmpty()) {
                    this.craftSlots.removeItem(slot, 1);
                    itemStack = this.craftSlots.getItem(slot);
                }

                if (!replacement.isEmpty()) {
                    if (itemStack.isEmpty()) {
                        this.craftSlots.setItem(slot, replacement);
                    } else if (ItemStack.isSameItemSameComponents(itemStack, replacement)) {
                        replacement.grow(itemStack.getCount());
                        this.craftSlots.setItem(slot, replacement);
                    } else if (!this.player.getInventory().add(replacement)) {
                        this.player.drop(replacement, false);
                    }
                }
            }
        }

        ci.cancel();
    }
}