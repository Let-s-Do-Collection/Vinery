# Vinery Addon API

Addons can add their own grape types and juices. Everything goes through `net.satisfy.vinery.api`.

## 1. Plugin

Grape types end up in the block states of every grape block (stems, lattices, grapevine pot), so they have to exist before Vinery creates its blocks. Vinery runs all plugins at the very start of its init, registering later throws an `IllegalStateException`.

```java
@VineryPlugin.Entry // NeoForge
public class MyVineryPlugin implements VineryPlugin {
    public static GrapeType NETHER_RED;

    @Override
    public void register() {
        NETHER_RED = VineryApi.registerGrapeType("mymod_nether_red", false, true);
        VineryApi.registerJuice("red_nether", MyTags.NETHER_RED_GRAPEJUICE);
    }
}
```

**Fabric**: add the class as entrypoint in `fabric.mod.json`:

```json
"entrypoints": {
  "vinery": ["com.example.MyVineryPlugin"]
}
```

**NeoForge**: annotate the class with `@VineryPlugin.Entry`, it needs a public no-arg constructor. Add Vinery as a dependency in `neoforge.mods.toml`.

## 2. Grape type

`registerGrapeType(id, lattice, red)`

- `id`: ends up in block states, only `a-z`, `0-9` and `_`. Prefix it with your mod id.
- `lattice`: grows on lattices (like jungle grapes) instead of grapevine stems.
- `red`: red or white, picks the lattice and grapevine pot visuals and the juice color.

Once your items are registered, connect them (for example in common setup):

```java
NETHER_RED.setItems(MyItems.NETHER_RED_GRAPE, MyItems.NETHER_RED_GRAPE_SEEDS, MyItems.NETHER_RED_GRAPEJUICE);
```

Use Vinery's classes for the items and blocks, for example `GrapeItem`, `GrapeBushSeedItem` and `GrapeBush` with your type.

## 3. Juice

`registerJuice(type, tag)` or `registerJuice(type, itemSupplier)` makes the fermentation barrel accept the juice. The type is the string recipes use:

```json
"juice": { "type": "red_nether", "amount": 25 }
```

Barrel tooltip translation key: `tooltip.vinery.fermentation_barrel.<type>_juice_with_percentage`.

## 4. Models

Minecraft merges block state files from all mods per variant, so ship an `assets/vinery/blockstates/grapevine_stem.json` that only contains your variants:

```json
{
  "variants": {
    "age=1,grape=mymod_nether_red": { "model": "mymod:block/nether_grapevine" },
    "age=2,grape=mymod_nether_red": { "model": "mymod:block/nether_grapevine" },
    "age=3,grape=mymod_nether_red": { "model": "mymod:block/nether_grapevine" },
    "age=4,grape=mymod_nether_red": { "model": "mymod:block/nether_grapevine_grown" }
  }
}
```

Lattices and the grapevine pot need nothing, they use the red/white visuals.

## Note

The juice id synced to the client is the registration order, so client and server need the same addons. That is the case anyway since the blocks have to match too.
