# Legacy Bugs

Legacy Bugs is a research-based Fabric mod for Minecraft 1.21.11 that restores historical Minecraft bugs as accurately as possible.

This branch is the Java 21 port of Legacy Bugs for Minecraft 1.21.11. The main branch is kept separately for the modern Java 25 / Minecraft 26.x version.

Each bug is studied from original snapshot behavior, documented, tested, and reimplemented carefully instead of being recreated by guesswork.

## Supported Version

This branch targets:

- Minecraft 1.21.11
- Java 21
- Fabric Loader 0.17.3 or later
- Fabric API 0.141.x for Minecraft 1.21.11

Do not use this JAR for Minecraft 26.1.2 or other Java 25 versions.

## Current Bugs

### LB-001 — Grindstone Enchantment Merge Bug

**Snapshot:** 24w11a  
**Status:** Implemented / Tested on Minecraft 1.21.11

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
**Status:** Implemented / Tested on Minecraft 1.21.11

This bug is related to crafting result handling in snapshot 24w18a.

In the original 24w18a behavior, remaining items from a crafting recipe can be returned to the wrong crafting grid position when the recipe is placed away from the top-left corner of the grid.

Legacy Bugs restores this behavior in Minecraft 1.21.11.

Confirmed behavior:

- Crafting remainders are applied starting from the top-left crafting grid slot
- The original recipe position inside the crafting grid is ignored
- Container items can appear in the wrong slot
- The original input item can remain in its original slot when the recipe is placed away from the top-left corner

Confirmed test examples:

- Honey Bottle placed in the top-left crafting slot
- Honey Bottle placed in the center crafting slot
- Honey Bottle placed in the bottom-right crafting slot
- Sugar crafting result leaves a Glass Bottle in the top-left slot when the recipe is offset

Relevant implementation area:

- `ResultSlot.onTake`
- `CraftingInput.Positioned`
- `getRemainingItems`

## Branches

This repository uses separate branches for different Minecraft and Java targets.

```text
main
= Minecraft 26.x
= Java 25

version/1.21.11
= Minecraft 1.21.11
= Java 21
```

Future backports, such as Minecraft 1.20.5, should use separate branches.

## Research Notes

Detailed bug notes and test progress are documented in:

```text
BUGS.md
```

## Disclaimer

Legacy Bugs is intended for singleplayer research, historical preservation, testing, and educational purposes.

Some restored behaviors may be considered unfair, unintended, or unwanted on multiplayer servers. Do not use this mod on servers without explicit permission from the server owner or administrators.

This project is not intended to be a cheat mod, hacked client, or exploit tool. Its purpose is to document and reproduce selected historical Minecraft bugs for learning, preservation, and controlled experimentation.

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.17.3 or later
- Fabric API 0.141.x for Minecraft 1.21.11
- Java 21