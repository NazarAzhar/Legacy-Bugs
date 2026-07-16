package com.nazarazhar.legacybugs.bug.grindstone;

import net.minecraft.world.item.ItemStack;

public final class LegacyGrindstoneLogic {

    private LegacyGrindstoneLogic() {
    }

    /**
     * Recreates the Grindstone enchantment merge bug from snapshot 24w11a.
     */
    public static ItemStack merge(ItemStack input, ItemStack additional) {

        // TODO:
        // This method will reproduce LB-001.

        return ItemStack.EMPTY;
    }
}