package net.satisfy.vinery.core.wine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

import static net.satisfy.vinery.core.registry.GrapeTypeRegistry.GRAPE_TYPE_TYPES;

public class GrapeType implements Comparable<GrapeType>, StringRepresentable {
    public static final Codec<GrapeType> CODEC = RecordCodecBuilder.create(inst->inst.group(
            Codec.STRING.fieldOf("id").forGetter(GrapeType::getId),
            Codec.BOOL.fieldOf("lattice").forGetter(GrapeType::isLattice),
            Codec.BOOL.fieldOf("red").forGetter(GrapeType::isRed)
    ).apply(inst,GrapeType::new));
    private final String id;
    private final boolean lattice;
    private final boolean red;
    private Supplier<Item> fruit;
    private Supplier<Item> seeds;
    private Supplier<Item> bottle;

    public GrapeType(String id, boolean lattice, boolean red) {
        this(id, () -> Items.AIR, () -> Items.AIR, () -> Items.AIR, lattice, red);
    }

    private GrapeType(String id, Supplier<Item> fruit, Supplier<Item> seeds, Supplier<Item> bottle, boolean lattice, boolean red) {
        this.id = id;
        this.fruit = fruit;
        this.seeds = seeds;
        this.bottle = bottle;
        this.lattice = lattice;
        this.red = red;
    }

    public static GrapeType fromString(String id) {
        for (GrapeType type : GRAPE_TYPE_TYPES) {
            if (type.getSerializedName().equals(id)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public @NotNull String getSerializedName() {
        return id;
    }

    public Item getFruit() {
        return this.fruit.get();
    }

    public String getId() {
        return id;
    }

    public Item getSeeds() {
        return this.seeds.get();
    }

    public Item getBottle() {
        return bottle.get();
    }

    public boolean isLattice() {
        return lattice;
    }

    public boolean isRed() {
        return red;
    }

    public boolean isWhite() {
        return !red;
    }

    public void setItems(Supplier<Item> fruit, Supplier<Item> seeds, Supplier<Item> bottle) {
        this.fruit = fruit;
        this.seeds = seeds;
        this.bottle = bottle;
    }

    @Override
    public int compareTo(@NotNull GrapeType grapeType) {
        return this.id.compareTo(grapeType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrapeType grapeType)) return false;
        return Objects.equals(id, grapeType.id);
    }
}
