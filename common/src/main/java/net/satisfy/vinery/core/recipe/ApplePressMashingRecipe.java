package net.satisfy.vinery.core.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.recipe.input.*;
import net.satisfy.vinery.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class ApplePressMashingRecipe implements Recipe<ApplePressMashingRecipeInput> {
    public final Ingredient input;
    private final ItemStack output;
    public static RecipeType<ApplePressMashingRecipe> Type = RecipeTypeRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get();

    public ApplePressMashingRecipe(Ingredient input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(ApplePressMashingRecipeInput inventory, Level level) {
        return input.test(inventory.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(ApplePressMashingRecipeInput container, HolderLookup.Provider registryAccess) {
        return this.output.copy();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(input);
        return list;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return this.output.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.APPLE_PRESS_MASHING_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public static class Serializer implements RecipeSerializer<ApplePressMashingRecipe> {
        @Override
        public MapCodec<ApplePressMashingRecipe> codec() {
            return RecordCodecBuilder.mapCodec(inst->inst.group(
                    Ingredient.CODEC.fieldOf("input").forGetter(ApplePressMashingRecipe::getInput),
                    ItemStack.CODEC.fieldOf("output").forGetter(ApplePressMashingRecipe::getOutput)
            ).apply(inst,ApplePressMashingRecipe::new));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ApplePressMashingRecipe> streamCodec() {
            return new StreamCodec<>(){
                @Override
                public void encode(RegistryFriendlyByteBuf buf, ApplePressMashingRecipe recipe) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf,recipe.getInput());
                    ItemStack.STREAM_CODEC.encode(buf,recipe.getOutput());
                }

                @Override
                public ApplePressMashingRecipe decode(RegistryFriendlyByteBuf buf) {
                    return new ApplePressMashingRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(buf),ItemStack.STREAM_CODEC.decode(buf));                }
            };
        }
    }

}
