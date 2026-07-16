package com.nazarazhar.legacybugs.bug.grindstone;

import net.minecraft.world.item.ItemStack;

public final class LegacyGrindstoneLogic {

    private LegacyGrindstoneLogic() {
    }

    /**
     * Recreates the Grindstone enchantment merge bug introduced in snapshot
     * 24w11a.
     */
    public static ItemStack merge(ItemStack input, ItemStack additional) {

        if (!input.is(additional.getItem())) {
            return ItemStack.EMPTY;
        }

        ItemStack result = input.copyWithCount(1);

        GrindstoneDurability.merge(result, input, additional);

        GrindstoneEnchantments.merge(result, input, additional);

        GrindstoneComponents.finish(result);

        return result;
    }
}