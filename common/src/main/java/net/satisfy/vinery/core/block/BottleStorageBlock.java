package net.satisfy.vinery.core.block;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.foundation.storage.StorageBlock;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.satisfy.vinery.core.registry.StorageTypeRegistry;
import net.satisfy.vinery.core.registry.TagRegistry;

public class BottleStorageBlock extends StorageBlock {
    private final Layout layout;

    public BottleStorageBlock(Properties settings, Layout layout) {
        super(settings);
        this.layout = layout;
    }

    @Override
    public boolean canInsertStack(ItemStack stack) {
        return stack.is(TagRegistry.SMALL_BOTTLE);
    }

    @Override
    public BlockEntityType<?> blockEntityType() {
        return EntityTypeRegistry.STORAGE_ENTITY.get();
    }

    @Override
    public int size() {
        return layout.offsets.length;
    }

    @Override
    public ResourceLocation type() {
        return layout.type;
    }

    @Override
    public Direction[] unAllowedDirections() {
        return new Direction[]{Direction.DOWN, Direction.UP};
    }

    @Override
    public int getSection(Float x, Float y) {
        return layout.getSection(x, y);
    }

    public enum Layout {
        FOUR(StorageTypeRegistry.FOUR_BOTTLE, new float[][]{{-0.35f, 0f}, {0f, -0.33f}, {-0.7f, -0.33f}, {-0.35f, -0.66f}}) {
            @Override
            int getSection(float x, float y) {
                if (x > 0.375 && x < 0.625) {
                    if (y >= 0.55) return 0;
                    if (y <= 0.45) return 3;
                } else if (y > 0.35 && y < 0.65) {
                    if (x < 0.4) return 1;
                    if (x > 0.65) return 2;
                }
                return Integer.MIN_VALUE;
            }
        },

        NINE(StorageTypeRegistry.NINE_BOTTLE, grid(3, 3)) {
            @Override
            int getSection(float x, float y) {
                float third = 1f / 3;
                int col = x < 0.375F ? 0 : x < 0.6875F ? 1 : 2;
                int row = y >= third * 2 ? 0 : y >= third ? 1 : 2;
                return col + row * 3;
            }
        };

        public final ResourceLocation type;

        public final float[][] offsets;

        Layout(ResourceLocation type, float[][] offsets) {
            this.type = type;
            this.offsets = offsets;
        }

        abstract int getSection(float x, float y);

        private static float[][] grid(int rows, int cols) {
            float[][] offsets = new float[rows * cols][];
            for (int i = 0; i < offsets.length; i++) {
                offsets[i] = new float[]{-0.35f * (i % cols), -0.33f * (i / cols)};
            }
            return offsets;
        }
    }
}
