package com.nazarazhar.legacybugs.mixin.grindstone;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.inventory.GrindstoneMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMergeMixin {
    @Unique
    private static final Logger LB001_LOGGER = LoggerFactory.getLogger("Legacy-Bugs/LB-001");

    @Inject(
            method = "mergeItems",
            at = @At("HEAD")
    )
    private void legacybugs$restore24w11aTopSlotMutation(
            ItemStack input,
            ItemStack additional,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (!legacybugs$shouldApply(input, additional)) {
            return;
        }

        legacybugs$copyEnchantmentsIntoTopInput(input, additional);

        LB001_LOGGER.info("[LB-001] Applied 24w11a-style top input mutation.");
        LB001_LOGGER.info("[LB-001] top/input components after mutation={}", input.getComponents());
        LB001_LOGGER.info("[LB-001] bottom/additional components={}", additional.getComponents());
    }

    @Unique
    private static boolean legacybugs$shouldApply(ItemStack input, ItemStack additional) {
        if (input.isEmpty() || additional.isEmpty()) {
            return false;
        }

        if (input.getCount() > 1 || additional.getCount() > 1) {
            return false;
        }

        if (!input.is(additional.getItem())) {
            return false;
        }

        if (!input.isDamageableItem()) {
            return false;
        }

        return EnchantmentHelper.hasAnyEnchantments(input)
                || EnchantmentHelper.hasAnyEnchantments(additional);
    }

    @Unique
    private static void legacybugs$copyEnchantmentsIntoTopInput(ItemStack topInput, ItemStack bottomInput) {
        ItemEnchantments bottomEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(bottomInput);

        if (bottomEnchantments.isEmpty()) {
            return;
        }

        EnchantmentHelper.updateEnchantments(topInput, mutable -> {
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : bottomEnchantments.entrySet()) {
                Holder<Enchantment> enchantment = entry.getKey();

                if (!enchantment.is(EnchantmentTags.CURSE) || mutable.getLevel(enchantment) == 0) {
                    mutable.upgrade(enchantment, entry.getIntValue());
                }
            }
        });
    }
}