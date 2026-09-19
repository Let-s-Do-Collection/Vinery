package net.satisfy.vinery.core.registry;

import net.satisfy.vinery.core.util.GrapeType;

import java.util.HashSet;
import java.util.Set;

public class GrapeTypeRegistry {
    public static final Set<GrapeType> GRAPE_TYPE_TYPES = new HashSet<>();

    public static final GrapeType NONE = registerGrapeType("none", false, false);
    public static final GrapeType RED = registerGrapeType("red", false, true);
    public static final GrapeType WHITE = registerGrapeType("white", false, false);
    public static final GrapeType SAVANNA_RED = registerGrapeType("savanna_red", false, true);
    public static final GrapeType SAVANNA_WHITE = registerGrapeType("savanna_white", false, false);
    public static final GrapeType TAIGA_RED = registerGrapeType("taiga_red", false, true);
    public static final GrapeType TAIGA_WHITE = registerGrapeType("taiga_white", false, false);
    public static final GrapeType JUNGLE_RED = registerGrapeType("jungle_red", true, true);
    public static final GrapeType JUNGLE_WHITE = registerGrapeType("jungle_white", true, false);

    public static void addGrapeAttributes() {
        RED.setItems(ObjectRegistry.RED_GRAPE, ObjectRegistry.RED_GRAPE_SEEDS, ObjectRegistry.RED_GRAPEJUICE);
        WHITE.setItems(ObjectRegistry.WHITE_GRAPE, ObjectRegistry.WHITE_GRAPE_SEEDS, ObjectRegistry.WHITE_GRAPEJUICE);
        SAVANNA_RED.setItems(ObjectRegistry.SAVANNA_RED_GRAPE, ObjectRegistry.SAVANNA_RED_GRAPE_SEEDS, ObjectRegistry.RED_SAVANNA_GRAPEJUICE);
        SAVANNA_WHITE.setItems(ObjectRegistry.SAVANNA_WHITE_GRAPE, ObjectRegistry.SAVANNA_WHITE_GRAPE_SEEDS, ObjectRegistry.WHITE_SAVANNA_GRAPEJUICE);
        TAIGA_RED.setItems(ObjectRegistry.TAIGA_RED_GRAPE, ObjectRegistry.TAIGA_RED_GRAPE_SEEDS, ObjectRegistry.RED_TAIGA_GRAPEJUICE);
        TAIGA_WHITE.setItems(ObjectRegistry.TAIGA_WHITE_GRAPE, ObjectRegistry.TAIGA_WHITE_GRAPE_SEEDS, ObjectRegistry.WHITE_TAIGA_GRAPEJUICE);
        JUNGLE_RED.setItems(ObjectRegistry.JUNGLE_RED_GRAPE, ObjectRegistry.JUNGLE_RED_GRAPE_SEEDS, ObjectRegistry.RED_JUNGLE_GRAPEJUICE);
        JUNGLE_WHITE.setItems(ObjectRegistry.JUNGLE_WHITE_GRAPE, ObjectRegistry.JUNGLE_WHITE_GRAPE_SEEDS, ObjectRegistry.WHITE_JUNGLE_GRAPEJUICE);
    }

    public static GrapeType registerGrapeType(String id, boolean lattice, boolean red) {
        GrapeType grapeType = new GrapeType(id, lattice, red);
        GRAPE_TYPE_TYPES.add(grapeType);
        return grapeType;
    }
}
