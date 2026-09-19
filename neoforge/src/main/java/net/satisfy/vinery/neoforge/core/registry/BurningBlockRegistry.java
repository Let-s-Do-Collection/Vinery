package net.satisfy.vinery.neoforge.core.registry;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BurningBlockRegistry {
    private static final Map<Block, Pair<Integer, Integer>> INSTANCE = new HashMap();

    public BurningBlockRegistry() {
    }

    public static void add(int burnOdd, int igniteOdd, Block... blocks) {
        Block[] var3 = blocks;
        int var4 = blocks.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Block b = var3[var5];
            INSTANCE.put(b, new Pair(burnOdd, igniteOdd));
        }

    }

    public static Map<Block, Pair<Integer, Integer>> getInstance() {
        return INSTANCE;
    }

    public static int getIgniteOdd(Block block) {
        Pair<Integer, Integer> odds = INSTANCE.get(block);
        return odds == null ? 0 : odds.getSecond();
    }

    public static int getBurnOdd(Block block) {
        Pair<Integer, Integer> odds = INSTANCE.get(block);
        return odds == null ? 0 : odds.getFirst();
    }
}

