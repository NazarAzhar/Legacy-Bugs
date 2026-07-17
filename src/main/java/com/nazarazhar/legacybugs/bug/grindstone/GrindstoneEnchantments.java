/*
 * Legacy Bugs
 * Copyright (C) 2026 Nazar Azhar
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.nazarazhar.legacybugs.bug.grindstone;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class GrindstoneEnchantments {

    private GrindstoneEnchantments() {
    }


    /**
     * Legacy 24w11a enchantment merge behavior.
     *
     * The bug kept enchantment insertion order
     * instead of sorting / normal merging.
     */
    public static void merge(
            ItemStack target,
            ItemStack source,
            ItemStack additional
    ) {

        EnchantmentHelper.updateEnchantments(target, enchantments -> {

            // copy enchant from first item
            ItemEnchantments first =
                    EnchantmentHelper.getEnchantmentsForCrafting(source);

            for (Entry<Holder<Enchantment>> entry : first.entrySet()) {

                Holder<Enchantment> enchant =
                        entry.getKey();

                enchantments.set(
                        enchant,
                        entry.getIntValue()
                );
            }


            // append second item
            ItemEnchantments second =
                    EnchantmentHelper.getEnchantmentsForCrafting(additional);


            for (Entry<Holder<Enchantment>> entry : second.entrySet()) {

                Holder<Enchantment> enchant =
                        entry.getKey();

                enchantments.set(
                        enchant,
                        entry.getIntValue()
                );
            }

        });
    }
}