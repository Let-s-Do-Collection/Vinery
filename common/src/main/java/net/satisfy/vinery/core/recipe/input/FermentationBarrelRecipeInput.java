package net.satisfy.vinery.core.recipe.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record FermentationBarrelRecipeInput(
        List<ItemStack> ingredientStacks,
        ItemStack wineBottle,
        JuiceData data
) implements RecipeInput {
    public static final int WINE_BOTTLE_SLOT = 0;

    @Override
    public ItemStack getItem(int i) {
        if (i == WINE_BOTTLE_SLOT) return wineBottle;
        return ingredientStacks.get(i - 1);
    }

    @Override
    public int size() {
        return ingredientStacks.size() + 1;
    }

    public List<ItemStack> getIngredientSlots() {
        return ingredientStacks;
    }

    @Override
    public JuiceData data() {
        return data;
    }

    public record JuiceData(String type, int amount) {
        public static final MapCodec<JuiceData> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.STRING.fieldOf("type").forGetter(JuiceData::type),
                        Codec.INT.fieldOf("amount").forGetter(JuiceData::amount)
                ).apply(instance, JuiceData::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf,JuiceData> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, JuiceData>() {
            @Override
            public JuiceData decode(RegistryFriendlyByteBuf buf) {
                return new JuiceData(buf.readUtf(),buf.readInt());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, JuiceData data) {
                buf.writeUtf(data.type());
                buf.writeInt(data.amount());
            }
        };
    }

}
