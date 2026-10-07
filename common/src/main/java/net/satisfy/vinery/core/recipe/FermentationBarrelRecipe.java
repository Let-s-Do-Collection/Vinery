package net.satisfy.vinery.core.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.block.entity.FermentationBarrelBlockEntity;
import net.satisfy.vinery.core.recipe.input.FermentationBarrelRecipeInput;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class FermentationBarrelRecipe implements Recipe<FermentationBarrelRecipeInput> {
    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;
    private final FermentationBarrelRecipeInput.JuiceData juiceData;
    private final boolean wineBottleRequired;
    public static RecipeType<FermentationBarrelRecipe> Type = RecipeTypeRegistry.FERMENTATION_BARREL_RECIPE_TYPE.get();

    public FermentationBarrelRecipe(NonNullList<Ingredient> inputs, FermentationBarrelRecipeInput.JuiceData data, ItemStack output, boolean wineBottleRequired) {
        this.inputs = inputs;
        this.juiceData = data;
        this.output = output;

        this.wineBottleRequired = wineBottleRequired;
    }

    public FermentationBarrelRecipeInput.JuiceData getJuiceData()
    {
        return this.juiceData;
    }

    public boolean isWineBottleRequired() {
        return wineBottleRequired;
    }

    @Override
    public boolean matches(FermentationBarrelRecipeInput input, Level level) {
        if (this.juiceData.amount() > 0) {
            if (input.data().amount() < this.juiceData.amount()) return false;
            if (!this.juiceData.type().equals(input.data().type())) return false;
        }

        if (this.wineBottleRequired) {
            ItemStack wineBottle = input.getItem(FermentationBarrelRecipeInput.WINE_BOTTLE_SLOT);
            if (wineBottle.isEmpty() || !wineBottle.is(ObjectRegistry.WINE_BOTTLE.get())) return false;
        }

        StackedContents recipeMatcher = new StackedContents();
        int matchingStacks = 0;

        for (int i = 0; i < input.getIngredientSlots().size(); i++) {
            ItemStack itemStack = input.getIngredientSlots().get(i);
            if (!itemStack.isEmpty()) {
                ++matchingStacks;
                recipeMatcher.accountStack(itemStack, 1);
            }
        }

        return matchingStacks == this.inputs.size() && recipeMatcher.canCraft(this, null);
    }

    @Override
    public @NotNull ItemStack assemble(FermentationBarrelRecipeInput input, HolderLookup.Provider registryAccess) {
        return this.output.copy();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return this.output.copy();
    }

    public NonNullList<Ingredient> getInputs() {
        return inputs;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.FERMENTATION_BARREL_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.FERMENTATION_BARREL_RECIPE_TYPE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<FermentationBarrelRecipe> {
        private static final MapCodec<Boolean> WINE_BOTTLE_CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        Codec.BOOL.fieldOf("required").forGetter(b -> b)
                ).apply(inst, b -> b)
        );
        @Override
        public MapCodec<FermentationBarrelRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.listOf().fieldOf("ingredients")
                            .xmap(list -> NonNullList.of(Ingredient.EMPTY, list.toArray(new Ingredient[0])),
                                    ingredients -> ingredients)
                            .forGetter(FermentationBarrelRecipe::getInputs),
                    FermentationBarrelRecipeInput.JuiceData.CODEC.fieldOf("juice").forGetter(FermentationBarrelRecipe::getJuiceData),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.output),
                    WINE_BOTTLE_CODEC.fieldOf("wine_bottle").forGetter(FermentationBarrelRecipe::isWineBottleRequired)
            ).apply(instance, FermentationBarrelRecipe::new));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FermentationBarrelRecipe> streamCodec() {
            return StreamCodec.of(
                    (buf, recipe) -> {
                        buf.writeVarInt(recipe.inputs.size());
                        for (Ingredient ingredient : recipe.inputs) {
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
                        }
                        FermentationBarrelRecipeInput.JuiceData.STREAM_CODEC.encode(buf,recipe.getJuiceData());
                        ItemStack.STREAM_CODEC.encode(buf, recipe.output);
                        buf.writeBoolean(recipe.wineBottleRequired);
                    },
                    buf -> {
                        int size = buf.readVarInt();
                        NonNullList<Ingredient> inputs = NonNullList.withSize(size, Ingredient.EMPTY);
                        for (int i = 0; i < size; i++) {
                            inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                        }

                        FermentationBarrelRecipeInput.JuiceData juiceData =
                                FermentationBarrelRecipeInput.JuiceData.STREAM_CODEC.decode(buf);
                        ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
                        boolean wineBottleRequired = buf.readBoolean();

                        return new FermentationBarrelRecipe(inputs, juiceData, output, wineBottleRequired);
                    }
            );
        }
    }

}