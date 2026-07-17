# Legacy Bugs

Legacy Bugs is a research-based Fabric mod for Minecraft 1.21.6 / 26.1.2 that restores historical Minecraft bugs as accurately as possible.

Each bug is studied from original snapshot behavior, documented, tested, and reimplemented carefully instead of being recreated by guesswork.

## Current Bugs

### LB-001 — Grindstone Enchantment Merge Bug

**Snapshot:** 24w11a  
**Status:** Implemented / Testing

This bug was introduced during the Item Data Components refactoring in snapshot 24w11a.

In the original behavior, enchantments from the second grindstone input item can be copied into the first input item under specific conditions. This causes the top input slot to become an illegal merged enchanted item.

Confirmed behavior:

- The top input item receives enchantments from the bottom input item
- The bottom input item remains unchanged
- The output slot shows a normal grindstone output without enchantments
- Mutually exclusive enchantments can be merged into the top input item
- Grindstone durability repair behavior still works normally on the output item

Confirmed test examples:

- Diamond Sword with Fire Aspect II + Diamond Sword with Looting III
- Diamond Sword with Sharpness V + Diamond Sword with Fire Aspect II and Looting III
- Diamond Chestplate with multiple mutually exclusive protection enchantments
- Bow with Mending + Bow with Infinity
- Damaged Diamond Swords with normal grindstone repair output

### LB-002 — MC-271398 Crafting Result Bug

**Snapshot:** 24w18a  
**Status:** Research

This bug is related to crafting result handling in snapshot 24w18a.

The research target is to reproduce the original 24w18a behavior as accurately as possible in Minecraft 1.21.6 / 26.1.2.

Relevant areas being investigated:

- Crafting result slot behavior
- Remaining items after crafting
- Container items returned to the crafting grid
- `ResultSlot.onTake`
- `CraftingInput.Positioned`
- `getRemainingItems`

## Research Notes

Detailed bug notes and test progress are documented in:

```text
BUGS.md