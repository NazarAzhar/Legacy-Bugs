# Legacy Bugs

Legacy Bugs is a research-based Fabric mod for Minecraft 1.21.6 / 26.1.2 that restores historical Minecraft bugs as accurately as possible.

Each bug is studied from original snapshot behavior, documented, tested, and reimplemented carefully instead of being recreated by guesswork.

## Current Bugs

### LB-001 — Grindstone Enchantment Merge Bug

**Snapshot:** 24w11a  
**Status:** Implemented / Testing

This bug was introduced during the Item Data Components refactoring in snapshot 24w11a.

In the original behavior, enchantments from the second grindstone input item can be copied into the first input item under specific conditions. This causes the top input slot to become an illegal merged enchanted item.

Confirmed test:

- Top slot: Diamond Sword with Fire Aspect II
- Bottom slot: Diamond Sword with Looting III
- Top slot after update: Diamond Sword with Fire Aspect II and Looting III
- Bottom slot remains: Diamond Sword with Looting III
- Output slot shows a normal grindstone output without enchantments

## Research Notes

Detailed bug notes and test progress are documented in:

```text
BUGS.md