# Legacy Bugs

Legacy Bugs is a research-based Fabric mod that restores selected historical Minecraft bugs as accurately as possible.

The mod was originally developed for **Minecraft Java Edition 26.1.2** and has also been successfully tested on **Minecraft Java Edition 26.2**.

Both tested versions use and require **Java 25**. The mod may also work on other Minecraft versions that use Java 25, but compatibility with untested versions is not guaranteed.

Each bug is studied from its original snapshot behavior, documented, tested, and carefully reimplemented instead of being recreated through guesswork.

## Current Bugs

### LB-001 — Grindstone Enchantment Merge Bug

**Original Snapshot:** 24w11a  
**Status:** Implemented / Tested

This bug was introduced during the Item Data Components refactoring in snapshot 24w11a.

In the original behavior, enchantments from the second grindstone input item can be copied into the first input item under specific conditions. This causes the top input slot to become an illegally merged enchanted item.

Confirmed behavior:

- The top input item receives enchantments from the bottom input item
- The bottom input item remains unchanged
- The output slot shows a normal grindstone output without enchantments
- Mutually exclusive enchantments can be merged into the top input item
- Normal grindstone durability repair behavior still applies to the output item

Confirmed test examples:

- Diamond Sword with Fire Aspect II + Diamond Sword with Looting III
- Diamond Sword with Sharpness V + Diamond Sword with Fire Aspect II and Looting III
- Diamond Chestplate with multiple mutually exclusive protection enchantments
- Bow with Mending + Bow with Infinity
- Damaged Diamond Swords with normal grindstone repair output

### LB-002 — MC-271398 Crafting Result Bug

**Original Snapshot:** 24w18a  
**Status:** Implemented / Tested

This bug is related to crafting-result handling in snapshot 24w18a.

In the original 24w18a behavior, remaining items from a crafting recipe can be returned to the wrong crafting-grid position when the recipe is placed away from the top-left corner of the grid.

Legacy Bugs restores this historical behavior on supported Minecraft versions.

Confirmed behavior:

- Crafting remainders are applied starting from the top-left crafting-grid slot
- The original recipe position inside the crafting grid is ignored
- Container items can appear in the wrong slot
- The original input item can remain in its original slot when the recipe is placed away from the top-left corner

Confirmed test examples:

- Honey Bottle placed in the top-left crafting slot
- Honey Bottle placed in the center crafting slot
- Honey Bottle placed in the bottom-right crafting slot
- Sugar crafting result leaves a Glass Bottle in the top-left slot when the recipe is offset

Relevant implementation areas:

- `ResultSlot.onTake`
- `CraftingInput.Positioned`
- `getRemainingItems`

## Bug Toggle System

Legacy Bugs includes a control system that allows the restored bugs to be enabled or disabled globally or individually.

The config file is created automatically after the game is launched once with the mod installed.

Config location:

```text
.minecraft/config/legacy-bugs.json
```

Default config:

```json
{
  "enabled": true,
  "lb001GrindstoneMerge": true,
  "lb002CraftingResultBug": true
}
```

Config options:

- `enabled` controls the whole mod globally
- `lb001GrindstoneMerge` controls LB-001
- `lb002CraftingResultBug` controls LB-002

If `enabled` is set to `false`, all restored bug behavior is disabled even if the individual bug options are still set to `true`.

## Commands

Legacy Bugs also provides in-game commands for controlling the restored bugs without manually editing the config file.

Available commands:

```text
/legacybugs status
/legacybugs enable
/legacybugs disable
/legacybugs lb001 enable
/legacybugs lb001 disable
/legacybugs lb002 enable
/legacybugs lb002 disable
```

Command behavior:

- `/legacybugs status` shows the current Legacy Bugs settings
- `/legacybugs enable` enables Legacy Bugs globally
- `/legacybugs disable` disables Legacy Bugs globally
- `/legacybugs lb001 enable` enables the grindstone bug
- `/legacybugs lb001 disable` disables the grindstone bug
- `/legacybugs lb002 enable` enables the crafting result bug
- `/legacybugs lb002 disable` disables the crafting result bug

Changes made through commands are saved to:

```text
.minecraft/config/legacy-bugs.json
```

## Version Compatibility

Legacy Bugs was originally developed for **Minecraft Java Edition 26.1.2**.

The same mod build has also been confirmed to run successfully on **Minecraft Java Edition 26.2**.

Compatibility with other Minecraft versions that use Java 25 may be possible. However, Minecraft updates can change internal classes, methods, mappings, Fabric APIs, or Mixin targets, so untested versions are not officially guaranteed to work.

## Branches

This repository uses separate branches for different Minecraft and Java targets.

```text
main
= Minecraft 26.1.2 / 26.2
= Java 25

version/1.21.11
= Minecraft 1.21.11
= Java 21
```

Use the correct JAR for your Minecraft version.

## Research Notes

Detailed bug documentation and testing progress are available in:

```text
BUGS.md
```

## Disclaimer

Legacy Bugs is intended for singleplayer research, historical preservation, testing, and educational purposes.

Some restored behaviors may be considered unfair, unintended, or unwanted on multiplayer servers. Do not use this mod on a server without explicit permission from the server owner or administrators.

This project is not intended to be a cheat mod, hacked client, or exploit tool. Its purpose is to document and reproduce selected historical Minecraft bugs for learning, preservation, and controlled experimentation.

## Requirements

- **Minecraft Edition:** Java Edition
- **Originally Developed For:** Minecraft 26.1.2
- **Confirmed Working On:** Minecraft 26.1.2 and 26.2
- **Java Runtime:** Java 25
- **Mod Loader:** Fabric Loader
- **Required Library:** Fabric API