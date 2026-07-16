package com.nazarazhar.legacybugs.mixin.grindstone;

import com.nazarazhar.legacybugs.LegacyBugs;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMergeMixin {

    @Inject(
            method = "mergeItems",
            at = @At("HEAD")
    )
    private void legacybugs$onMergeItems(
            ItemStack input,
            ItemStack additional,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        LegacyBugs.LOGGER.info("[LB-001] mergeItems() called");
    }
}