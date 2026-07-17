/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.bug.grindstone;

import net.minecraft.world.item.ItemStack;

public final class GrindstoneDurability {

    private GrindstoneDurability() {
    }

    public static void merge(ItemStack result, ItemStack input, ItemStack additional) {

        // TODO LB-001
        // Reproduce 24w11a durability calculation.

    }
}