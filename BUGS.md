## LB-001

**Name**  
Grindstone Enchantment Merge Bug

**Minecraft Version**  
24w11a

**Target Mod Version**  
Minecraft 1.21.6 / 26.1.2

**Mojang Issue**  
None (internal fix during Item Data Components refactoring)

**Status**  
🟢 Implemented / Testing

**Description**  
A grindstone bug introduced during the Item Data Components refactoring in snapshot 24w11a.

The bug causes enchantments from the second grindstone input item to be copied into the first input item under specific conditions. This makes the top input slot become an illegal merged enchanted item.

This behavior is different from a normal grindstone result. In the original 24w11a behavior, the important mutation happens to the input item itself, not only to the output slot.

**Observed 24w11a Behavior**

Example:

- Top slot: Diamond Sword with Fire Aspect II
- Bottom slot: Diamond Sword with Looting III

After the grindstone updates:

- Top slot becomes: Diamond Sword with Fire Aspect II and Looting III
- Bottom slot remains: Diamond Sword with Looting III
- Result slot shows a normal grindstone output without enchantments

**Legacy Bugs Implementation**

LB-001 v1 originally recreated the visible merged result in the output slot.

LB-001 v2 is closer to the original 24w11a behavior. It intentionally mutates the top input item by copying enchantments from the bottom input item into it.

**Confirmed Test A**

Input:

- Top slot: Diamond Sword with Fire Aspect II
- Bottom slot: Diamond Sword with Looting III

Result:

- Top slot after update: Diamond Sword with Fire Aspect II and Looting III
- Bottom slot after update: Diamond Sword with Looting III
- Output slot: Diamond Sword without enchantments

Runtime log confirmed:

- `Applied 24w11a-style top input mutation`
- Top input contained `looting=>3` and `fire_aspect=>2`
- Bottom input still contained only `looting=>3`

**Confirmed Test B**

Input:

- Top slot: Diamond Sword with Sharpness V
- Bottom slot: Diamond Sword with Fire Aspect II and Looting III

Result:

- Top slot after update: Diamond Sword with Sharpness V, Fire Aspect II, and Looting III
- Bottom slot after update: Diamond Sword with Fire Aspect II and Looting III
- Output slot: Diamond Sword without enchantments

**Confirmed Test C**

Input:

- Top slot: Diamond Sword with Fire Aspect II and Looting III
- Bottom slot: Diamond Sword with Sharpness V

Result:

- Top slot after update: Diamond Sword with Sharpness V, Fire Aspect II, and Looting III
- Bottom slot after update: Diamond Sword with Sharpness V
- Output slot: Diamond Sword without enchantments

Note:

- Enchantment display order may differ from input order.
- The important behavior is that the top input item contains enchantments from both input items.

**Confirmed Test D**

Input:

- Top slot: Diamond Chestplate with Protection IV
- Bottom slot: Diamond Chestplate with Blast Protection IV, Fire Protection IV, and Projectile Protection IV

Result:

- Top slot after update: Diamond Chestplate with Protection IV, Blast Protection IV, Fire Protection IV, and Projectile Protection IV
- Bottom slot after update: Diamond Chestplate with Blast Protection IV, Fire Protection IV, and Projectile Protection IV
- Output slot: Diamond Chestplate without enchantments

Note:

- This confirms that mutually exclusive armor protection enchantments can be merged into the top input item.

**Confirmed Test E**

Input:

- Top slot: Bow with Mending
- Bottom slot: Bow with Infinity

Result:

- Top slot after update: Bow with Infinity and Mending
- Bottom slot after update: Bow with Infinity
- Output slot: Bow without enchantments

Note:

- This confirms that mutually exclusive bow enchantments can be merged into the top input item.

**Confirmed Test F**

Input:

- Top slot: Diamond Sword with Sharpness V, durability around 150 / 1561
- Bottom slot: Diamond Sword with Looting III, durability around 800 / 1561

Result:

- Top slot after update: Diamond Sword with Sharpness V and Looting III
- Top slot durability stays the same as the original top input item
- Bottom slot after update: Diamond Sword with Looting III
- Bottom slot durability stays the same as the original bottom input item
- Output slot: Diamond Sword without enchantments
- Output slot durability is around 1028 / 1561

Note:

- This confirms that the top input item receives merged enchantments.
- The grindstone output still follows normal durability repair behavior.

**Progress**

- [x] Project setup
- [x] Initial research
- [x] Reproduce original behavior
- [x] Analyze source code
- [x] Implement v1 output-slot recreation
- [x] Implement v2 top-input mutation
- [x] Test Fire Aspect II + Looting III case
- [x] Test Sharpness V + Looting III + Fire Aspect II case
- [x] Test reversed input order
- [x] Test armor protection conflict case
- [x] Test bow Mending + Infinity conflict case
- [x] Test durability behavior
- [x] Clean debug logs before release

---

## LB-002

**Name**  
MC-271398 Crafting Result Bug

**Minecraft Version**  
24w18a

**Target Mod Version**  
Minecraft 1.21.6 / 26.1.2

**Mojang Issue**  
MC-271398

**Status**  
🟢 Implemented / Testing

**Description**  
A crafting result handling bug observed in snapshot 24w18a.

The bug is related to how the crafting result slot handles remaining items after a crafted item is taken.

In 24w18a, remaining items from a crafting recipe can be returned to the wrong crafting grid position when the recipe is placed away from the top-left corner of the grid.

The research target is to reproduce the original 24w18a behavior as accurately as possible in Minecraft 1.21.6 / 26.1.2.

Unlike LB-001, this bug involves crafting result slot logic instead of grindstone logic.

**Observed 24w18a Behavior**

The bug was confirmed using the Honey Bottle to Sugar recipe.

Honey Bottle normally leaves behind a Glass Bottle after crafting Sugar.

In 24w18a, when the Honey Bottle is placed away from the top-left crafting slot, the Glass Bottle appears in the top-left slot instead of replacing the original Honey Bottle position.

The original Honey Bottle can remain in its original slot.

**Observed Vanilla 26.1.2 Behavior**

In Minecraft 1.21.6 / 26.1.2, the bug is fixed.

When the Honey Bottle is placed in the center or bottom-right of the crafting grid, the Glass Bottle correctly appears in the same position as the original Honey Bottle.

**Legacy Bugs Implementation**

LB-002 restores the 24w18a-style crafting remainder bug.

The implementation intentionally applies remaining crafting items starting from the top-left crafting grid slot instead of using the recipe's original `left` and `top` position.

Modern 26.1.2 vanilla uses `CraftingInput.Positioned` to map remaining items back to the correct grid position.

LB-002 intentionally ignores that positioned mapping and restores the older broken behavior.

**Technical Notes**

24w18a behavior used direct input indexes when processing remaining items:

- Recipe input was created from the active recipe shape
- Remaining items were processed from index `0`
- The original recipe position inside the crafting grid was not preserved correctly

Modern 26.1.2 behavior uses:

- `CraftingInput.Positioned`
- `left`
- `top`
- `width`
- `height`

The important modern method is:

- `ResultSlot.onTake`

The LB-002 mixin targets:

- `net.minecraft.world.inventory.ResultSlot`

**Confirmed Test A**

Input:

- Honey Bottle placed in the top-left of the 3x3 crafting grid
- Crafting result: Sugar

24w18a-style result:

- Glass Bottle appears in the top-left crafting slot
- This is normal behavior because the recipe is already positioned at the top-left

Result:

- Passed

**Confirmed Test B**

Input:

- Honey Bottle placed in the center of the 3x3 crafting grid
- Crafting result: Sugar

Expected 24w18a-style result:

- Glass Bottle appears in the top-left crafting slot
- Original Honey Bottle remains in the center slot

Result:

- Passed

**Confirmed Test C**

Input:

- Honey Bottle placed in the bottom-right of the 3x3 crafting grid
- Crafting result: Sugar

Expected 24w18a-style result:

- Glass Bottle appears in the top-left crafting slot
- Original Honey Bottle remains in the bottom-right slot

Result:

- Passed

**Progress**

- [x] Create LB-002 documentation section
- [x] Create crafting mixin package
- [x] Locate current 26.1.2 crafting result code
- [x] Locate 24w18a crafting result code
- [x] Compare 24w18a behavior with 26.1.2 behavior
- [x] Identify exact bug trigger condition
- [x] Implement mixin
- [x] Test in game
- [x] Document confirmed behavior