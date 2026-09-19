[1.5.4]

**Added**
* Traditional Chinese translation (thanks to CherryPuff)
* Bulgarian translation (thanks to OmegaSleepy)
* Brazilian Portuguese translation (thanks to Patrik)
* Config option to disable the automatic leaf canopy that Red/White Grapevine stems grow over time

**Changed**

**Fixed**
* Fixed the teleport effect of Chorus Wine, resolving a bug where drinking it could teleport players tens of thousands of blocks away or even out of the world (thanks to 鸢银子)
* Fixed incorrect compostable ids for Vinery items in the datamap
* `GrapeBush` no longer fails to generate during worldgen due to a light check, resolving a ScalableLux compatibility issue (thanks to Gardel)
* `StorageBlockEntity` now implements `Clearable`, fixing compatibility with Sable (thanks to Clem76)
* Fixed incorrect item ids for Taiga Grape Seed trades, which could crash on NeoForge (thanks to James)
* Fixed and updated the Italian translation, including a Chorus Wine text fix (thanks to Serena)
* Fixed wrong `tags/items` directory names preventing several item tags from working correctly (thanks to Ninjdai)
* Fixed the Winemaker Trade crash on NeoForge for good: a second overlooked Taiga Grape Seed trade id was corrected, and the underlying cause was fixed - an unknown item id now resolves to `null` on NeoForge instead of `AIR`, matching Fabric's behavior
* Fixed a crash with Create's Schematicannon/`SchematicLevel` caused by an unsafe cast in `StorageBlockEntity`
* Fixed the Trader Mule's hitbox floating above its saddle by correcting its height to match the model
* Fixed wine tooltips showing "0:00" effect duration for freshly obtained bottles (crafted, looted, traded) before their aging data had synced, even though drinking them already applied the correct duration
* Fixed the Winemaker villager profession being nearly impossible to get on NeoForge, since its workstation detection range was far smaller there than on Fabric
* Fixed a potential world save crash for Lattice blocks with missing or old grape data
* Fixed a client-side exploit/desync where the XP bonus effect could apply extra experience without server authorization
* Fixed the Wandering Winemaker selling Jungle White Grapes instead of Jungle White Grape Seeds
* Fixed the Creeper effect at level I not dealing any block damage on explosion

***

[1.5.3]

**Fixed**
* Lattice blocks now render correctly with see-through parts by using proper occlusion settings
* Vinery Items not being compostable
* Removed deprecated and invalid item IDs from default trade configs
* Dark Cherry Saplings now correctly grow Dark Cherry Trees instead of Apple Trees 
* Wine aging is now stored per-bottle using server values, preventing pre-aged wines and client-dependent aging/effects on servers
* Added missing loottables for potted Vinery saplings (thanks to AraneaeDiscordia)
* Fixed a crash when inserting non-Vinery bottles into Wine Boxes, which could prevent worlds from loading
* Fixed multiple Vinery advancements not progressing or completing correctly due to invalid criterias

**Changed**
* /wine command now requires OP level 2 

***

[1.5.1]

**Fixed**
* Resolved multiple invalid or missing tag assignments across block and item registries
* Corrected Cherry Boat render layer and model reference causing missing visuals
* Fixed advancement conditions triggering on unintended player actions
* Adjusted `DarkCherryLeaves` loot tables 
* Fixed shader/material issue causing Winemaker Armor to render black under specific conditions
* Corrected UV mapping and sprite reference for Hanging Sign textures

*** 

[1.5.0]

**Welcome to 1.21.1!**

***

[1.4.41]

**Fixed**
* `JungleWineRack` open/close property being inverted
* Wine aging did not update in tooltip or when consumed. Root causes:
    * cached NBT never recomputed
    * inverted `hasWineYear` prevented proper initialization
    * getter side-effects caused stale UI values
* `getDays`/`getYear` math stabilized; no unintended zeroing
* `JellieWine` Texture alignment

**Added**
* Debug commands for testing:
    * `/wine info` — shows current Age (years/days), effect amplifier, and duration (ticks) for the bottle in main hand
    * `/wine age <years>` — sets the NBT year so the item is treated as aged by `<years>` years immediately

***

[1.4.40]

**Added**
- Tooltips for all wine bottles now indicate their size (`small` or `big`).
- Wine bottle storage blocks now display tooltips specifying compatible bottle sizes (`small` or `all sizes`).
- Japanese translation _(Thanks to PExPE3)_
- Bushy Leaves resourcepack is now bundled and supplied by default for forge as well
- Grapevine Leaves are now available again! They will grow near Grapevine Stems with Red Grape or White Grapes

**Changed**
- Added template models for most Vinery specific Blocks (e.g. Wine Racks, Wines) - this should reduce loading times 
- Increased the `Completionist Banner` effect duration from 40 to 200
- Updated the `Overgrown Lattices` advancement description for better clarity
- Replace `VineryIdentifier` with `Vinery.identifier` - this should resolve Issues with Xaeros Map Mob Icons
- Completely reworked Apple & Dark Cherry Trees - new Features, Textures and better functionality
- Wine aging now uses `getGameTime()` instead of `getDayTime()` to prevent aging reset when using `/time set`
- Wine age and upgrade progress calculation now use day-based precision instead of year rounding _(Thanks to AverageChaos)_
- Stackable logs now provide `1000` smelting value, previously `300`
- Jungle Grapevine Blocks now use biome foliage tint 

**Fixed**
- Resolved an issue where `Apple Leaves` were not properly registered as flammable
- `Completionist Wall Banner` not having a Loottable
- Fixed a crash when opening the Vinery creative tab caused by `ItemStack` entries with a count greater than `1`
- Fixed a startup crash with Easy NPC caused by parallel modification of the strippables map during setup

***

[1.4.39]

**Added**
- Added Cherry Leaves, Apple Leaves and Grapevine Leaves to the `hoe` mineable tag.

**Changed**
- Reworked Wine Aging Logic - /set time won't affect Wine Aging anymore. If you want to manually age your Wine use /time add. 
- Changed the `apple_mashing` `honey_comb` recipe output count from "9" to "4"

**Fixed**
- Config should now load & generate properly on Forge
- Wine Years values do are now properly update
- CreeperCrush not showing a DeathMessage and dropping the players lot
- ExperienceEffect not giving the correct amount of additional experience
