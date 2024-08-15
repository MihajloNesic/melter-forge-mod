package com.oierbravo.melter.registrate;

import com.oierbravo.melter.Melter;
import com.oierbravo.melter.content.melter.MeltingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Melter.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Melter.MODID);

    public static final Supplier<RecipeType<MeltingRecipe>> MELTING_TYPE =
            RECIPE_TYPES.register("melting",() -> new RecipeType<>() {
                @Override
                public String toString() {
                    return MeltingRecipe.ID.toString();
                }
            });

    public static final Supplier<MeltingRecipe.Serializer> MELTING_SERIALIZER =
            SERIALIZERS.register("melting", () -> MeltingRecipe.Serializer.INSTANCE);

    public static Optional<RecipeHolder<MeltingRecipe>> find(SingleRecipeInput pInput, Level pLevel) {
        if(pLevel.isClientSide())
            return Optional.empty();
        return pLevel.getRecipeManager().getRecipeFor(ModRecipes.MELTING_TYPE.get() ,pInput,pLevel);
    }

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);

        RECIPE_TYPES.register(eventBus);
    }
}
