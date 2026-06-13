# Fluid Recipes - TODO

All 6 fluids are implemented and registered. This file tracks the melter/solidifier
recipes to add, plus new items to create. Recipe JSON format: see
`data/cyclic/recipe/melter/melter_honey.json` and `data/cyclic/recipe/solidifier/honeycomb.json`.

---

## New Items

- [x] **Chocolate Milk Bottle** (`cyclic:chocolate_milk`) - registered in ItemRegistry, model + lang added. Speed I + Saturation briefly. Needs texture at `assets/cyclic/textures/item/food/chocolate_milk.png`.
- [ ] **Glowstone Vial** - deferred (no output torch/vial item yet, waiting on light source design decision)
- [ ] **Warden's Flask** - deferred

---

## Melter Recipes (item -> fluid)

All done.

### Chocolate (`cyclic:chocolate`) [DONE]
| Input | Output | File |
|---|---|---|
| cocoa beans | 100 mB | melter_chocolate_cocoa.json |
| sugar | 50 mB | melter_chocolate_sugar.json |

### Liquid Redstone (`cyclic:redstone`) [DONE]
| Input | Output | File |
|---|---|---|
| redstone dust | 100 mB | melter_redstone_dust.json |
| redstone block | 900 mB | melter_redstone_block.json |

### Liquid Ender (`cyclic:ender`) [DONE]
| Input | Output | File |
|---|---|---|
| ender pearl | 250 mB | melter_ender_pearl.json |
| eye of ender | 500 mB | melter_ender_eye.json |
| chorus fruit | 100 mB | melter_ender_chorus.json |

### Liquid Glowstone (`cyclic:glowstone`) [DONE]
| Input | Output | File |
|---|---|---|
| glowstone dust | 250 mB | melter_glowstone_dust.json |
| glowstone block | 1000 mB | melter_glowstone_block.json |

### Liquid Amethyst (`cyclic:amethyst`) [DONE]
| Input | Output | File |
|---|---|---|
| amethyst shard | 100 mB | melter_amethyst_shard.json |
| amethyst cluster | 400 mB | melter_amethyst_cluster.json |
| amethyst block | 400 mB | melter_amethyst_block.json |
| budding amethyst | 1200 mB | melter_amethyst_budding.json |

### Liquid Sculk (`cyclic:sculk`) [DONE]
| Input | Output | File |
|---|---|---|
| sculk | 200 mB | melter_sculk_sculk.json |
| sculk vein | 50 mB | melter_sculk_vein.json |
| sculk catalyst | 1000 mB | melter_sculk_catalyst.json |
| echo shard | 500 mB | melter_sculk_echo_shard.json |

---

## Solidifier Recipes (item + fluid -> item)

### Chocolate [DONE]
| Fluid | Input item | Output | File |
|---|---|---|---|
| 200 mB | sugar | cookie x2 | solidifier_chocolate_cookie.json |
| 500 mB | apple | chocolate apple | solidifier_chocolate_apple.json |
| 1000 mB | glass bottle | Chocolate Milk | solidifier_chocolate_milk.json |
| 500 mB | wheat x3 | chocolate cake | **DEFERRED** - see ideas.md |

### Liquid Redstone [DONE*]
| Fluid | Input item | Output | File |
|---|---|---|---|
| 250 mB | stick | redstone torch x2 | solidifier_redstone_torch.json |
| 500 mB | quartz + stone | comparator | solidifier_redstone_comparator.json |
| ~~100 mB~~ | ~~clay ball~~ | ~~redstone dust~~ | skipped (clay/redstone mix thematically wrong) |
| ~~900 mB~~ | ~~clay balls~~ | ~~redstone block~~ | skipped |

### Liquid Ender [DONE]
| Fluid | Input item | Output | File |
|---|---|---|---|
| 1000 mB | sandstone | end stone | solidifier_ender_endstone.json |
| 500 mB | popped chorus fruit | purpur block | solidifier_ender_purpur.json |
| 1000 mB | popped chorus x3 | end stone bricks x2 | solidifier_ender_endstonebricks.json |
| 500 mB | obsidian | ender pearl | solidifier_ender_pearl.json |

### Liquid Glowstone [DONE*]
| Fluid | Input item | Output | File |
|---|---|---|---|
| 250 mB | sand | glowstone dust | solidifier_glowstone_dust.json |
| 1000 mB | sand x3 | glowstone block | solidifier_glowstone_block.json |
| 500 mB | iron nugget | glow ink sac | solidifier_glowstone_glowinksac.json |
| 100 mB | stick | glowstone torch | **DEFERRED** - no torch item yet |
| 500 mB | glass bottle | Glowstone Vial | **DEFERRED** - item not yet created |

### Liquid Amethyst [DONE]
| Fluid | Input item | Output | File |
|---|---|---|---|
| 400 mB | cobblestone | amethyst block | solidifier_amethyst_block.json |
| 100 mB | glass pane | tinted glass | solidifier_amethyst_tinted_glass.json |
| Note: existing solidifier_budding_amethyst.json kept as-is (uses XP juice, different recipe path) | | | |

### Liquid Sculk [DONE]
| Fluid | Input item | Output | File |
|---|---|---|---|
| 200 mB | rotten flesh | sculk | solidifier_sculk_rotten.json |
| 500 mB | amethyst shard | echo shard | solidifier_sculk_echo_shard.json |
| 1000 mB | glass bottle + sculk + sculk vein | experience bottle | solidifier_sculk_xp_bottle.json |
| Note: existing solidifier_sculk_catalyst.json kept as-is (uses XP juice) | | | |

---

## Still Pending

- Chocolate Milk texture (`assets/cyclic/textures/item/food/chocolate_milk.png`)
- Glowstone light-emitting block solidifier recipe (deferred - waiting on design: froglights? torch variant?)
- Chocolate Cake (solidifier + item) - see ideas.md for design notes
- Warden's Flask item + recipe
