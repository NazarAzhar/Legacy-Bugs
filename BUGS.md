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
- [ ] Test durability behavior
- [x] Clean debug logs before release