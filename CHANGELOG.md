[1.5.5]

**Added**
* Grapevine Pot: looking at it now shows a HUD panel with which grapes are inside, how full it is, how many times you've stomped them and how many bottles are left. Juice splashes out and the grapes get squished every time you land in the pot, and the juice now sloshes a little when you fill a bottle
* Stackable Logs: lit logs now work like a campfire. Put up to 4 food items on top and let them cook. They burn out after 10 minutes, adding a log while they burn resets the timer. They can also be lit with Fire Charges, burning arrows and fireballs, while Water Bottles, Splash Water Bottles and rain put them out
* HUD panels for wine racks, the Wine Box and shelves: aim at a slot to see what's inside. Wines also show their age and whether they're fully aged. Empty slots show which wines can go there, replacing the old "Storage for…" and bottle size tooltips
* HUD panels for grape bushes, grapevine stems and lattices: growth progress, what to plant and why something isn't growing (light, sunlight, soil)
* Wine tooltips now say "Fully Aged" once the highest effect level is reached
* Cellar aging: wine stored without sky light (underground or fully roofed) now ages faster, 1.5x by default
* New config options: every wine's effect, amplifier and duration, turning wine aging off completely, letting wine only age while it sits in storage (wine racks, Wine Box, shelves, placed bottles), the Grapevine Pot's jumps needed, heavy armor bonus and splash particles, and showing the HUD panels always, never or only while wearing the Straw Hat
* EMI support: Fermentation Barrel and Apple Press (mashing and fermenting) recipes now show up in EMI
* Lattices with grapes planted now drop the occasional leaf
* Bottles now slide into wine racks when you put them in
* The Fermentation Barrel now drips juice from its tap while it holds juice, in the color of the juice, and splashes when a recipe finishes
* The Apple Press now splashes and makes sounds while it works and when mashing or fermenting finishes
* The Vinery Standard now has the Legendary rarity, its name shows in a golden gradient with a rarity line in the tooltip
* The Straw Hat and the Winemaker Apron, Leggings and Boots now all have the Rare rarity, shown in blue with a rarity line in the tooltip

**Changed**
* Vinery now uses Foundation, the shared Let's Do library, which is bundled inside the mod jar - no separate download needed. Chairs, cabinets, drawers, shelves, wine racks, the big table, the Vinery Standard, Dark Cherry signs, hanging signs, boats and chest boats, the Dark Cherry Shelf, the window and the Fermentation Barrel slots now use Foundation's shared implementations
* Winemaker armor now uses Foundation's armor set system: the tooltip shows how many pieces you're wearing, and the custom models render the same way on Fabric and NeoForge
* The creative tab is now split into four side tabs: Vineyard Essentials, Wines & Juices, Cherry, and Decoration & Storage
* Grapevine Pot: stomping now takes 12 jumps, heavy armor (iron or better) stomps 10% faster, grape types can no longer be mixed in one pot, and breaking a pot that isn't stomped yet gives the grapes back
* Flint and Steel and Shovels now lose durability when used on Stackable Logs
* The effect level in wine tooltips now matches the vanilla numbering (amplifier 1 shows as II)
* The Experience effect now gives +50% experience per level when picking up experience orbs, the same on Fabric and NeoForge
* The Fermentation Barrel only looks up its recipe when its contents change instead of every tick, and empty barrels no longer save every tick
* The Apple Press uses a cached recipe lookup instead of searching all recipes twice per tick
* Taiga grape bushes check their soil much cheaper
* The Grapevine Pot has half as many block states, its content is drawn from dedicated models
* White grapes and white juice (block texture, Fermentation Barrel bar) got a new golden color, the old green looked more like grass
* The recipe book now puts the Wine Bottle into its slot for recipes that need one (Apple Press and Fermentation Barrel)

**Fixed**
* The Experience effect could hand out bonus experience every tick while standing on an experience orb, and stacked twice on Fabric
* Burning Stackable Logs never hurt anyone, they now deal campfire damage (sneak to walk over them safely)
* Improved Jump Boost did nothing since 1.21, the jump height bonus works again
* Double jump on NeoForge now uses the jump key binding and works the same as on Fabric
* The Grape Growth Multiplier now actually works and affects bushes, grapevine stems and lattices
* The config comments now correctly say durations are in ticks, not seconds
* The Grapevine Pot squeeze sound no longer plays once the juice is done
* The Apple Press no longer stops fermenting when the mashing slot holds something it can't mash
* Apple Press and Fermentation Barrel outputs no longer stack past the stack limit, and wines brewed on different days no longer merge in the barrel output (losing their age)
* The Fermentation Barrel no longer grants free juice when the max fluid level isn't a multiple of the juice amount
* Taking wine out of the Fermentation Barrel now always counts the taken amount correctly for advancements
* The Party effect no longer spawns a client-side ghost firework
* Server hang/crash when Create: Aeronautics/Sable contraptions hit Apple or Dark Cherry Leaves: fruit leaves no longer update the whole tree at once when a leaf breaks and use the vanilla leaf update behavior again (thanks to RhodeWithBrim)
* Dark Cherry hanging signs now open the hanging sign editor instead of the regular sign editor
* Boats no longer lose their leash when the world is reloaded
* Added the missing German name for the Apple Press REI category and missing German translations
* Lattice leaves no longer flicker where two lattices meet, and floor lattices no longer draw their leaves twice

***

[1.5.4]

**Added**
* Traditional Chinese translation (thanks to CherryPuff)
* Bulgarian translation (thanks to OmegaSleepy)
* Brazilian Portuguese translation (thanks to Patrik)
* Config option to disable the automatic leaf canopy that Red/White Grapevine stems grow over time

**Changed**
* Shearing a Red/White Grapevine stem all the way back to bare now resets its growth cycle, so a pending or already-grown leaf canopy from before is no longer tied to it (replanting starts a fresh cycle)

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
