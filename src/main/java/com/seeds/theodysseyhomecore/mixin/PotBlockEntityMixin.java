package com.seeds.theodysseyhomecore.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.seeds.theodysseyhomecore.tag.EldritchTags;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(PotBlockEntity.class)
public abstract class PotBlockEntityMixin extends BlockEntity {
    public PotBlockEntityMixin(BlockEntityType<?> type, net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        super(type, pos, state);
    }

    @Shadow
    private Ingredient carrier;
    @Shadow
    private ItemStack result;
    @Shadow
    private int status;
    @Shadow
    private int currentTick;
    @Shadow
    private int stirFryCount;
    @Shadow
    private NonNullList<ItemStack> inputs;

    @Unique
    private boolean eldritchfeast$isIceOrSnowBlock() {
        BlockState belowState = this.level.getBlockState(this.worldPosition.below());
        return belowState.is(Blocks.ICE) || belowState.is(Blocks.PACKED_ICE) ||
               belowState.is(Blocks.BLUE_ICE) || belowState.is(Blocks.FROSTED_ICE) ||
               belowState.is(Blocks.SNOW) || belowState.is(Blocks.SNOW_BLOCK) ||
               belowState.is(Blocks.POWDER_SNOW);
    }

    @Unique
    private boolean eldritchfeast$isIceStove() {
        BlockState belowState = this.level.getBlockState(this.worldPosition.below());
        return belowState.is(com.seeds.theodysseyhomecore.block.ModBlocks.ICE_STOVE.get());
    }

    @Unique
    private boolean eldritchfeast$hasColdSource() {
        return eldritchfeast$isIceOrSnowBlock() || eldritchfeast$isIceStove();
    }

    @Unique
    private boolean eldritchfeast$isColdRecipe(ResourceLocation id) {
        String path = id.getPath();
        return path.contains("/cold_") || path.startsWith("cold_");
    }

    @Inject(method = "hasHeatSource", at = @At("RETURN"), cancellable = true)
    private void eldritchfeast$checkColdSource(Level level, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }
        
        if (eldritchfeast$hasColdSource()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "startCooking", at = @At("HEAD"), cancellable = true)
    private void eldritchfeast$filterRecipeBySource(Level level, CallbackInfo ci) {
        boolean hasCold = eldritchfeast$hasColdSource();
        
        SimpleInput simpleInput = new SimpleInput(this.inputs);
        List<RecipeHolder<PotRecipe>> allRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.POT_RECIPE);
        
        Optional<RecipeHolder<PotRecipe>> matchedRecipe;
        
        if (hasCold) {
            matchedRecipe = allRecipes.stream()
                .filter(holder -> eldritchfeast$isColdRecipe(holder.id()))
                .filter(holder -> holder.value().matches(simpleInput, level))
                .findFirst();
        } else {
            matchedRecipe = allRecipes.stream()
                .filter(holder -> !eldritchfeast$isColdRecipe(holder.id()))
                .filter(holder -> holder.value().matches(simpleInput, level))
                .findFirst();
        }

        if (matchedRecipe.isPresent()) {
            ci.cancel();
            PotRecipe value = matchedRecipe.get().value();
            this.carrier = value.carrier();
            this.result = value.assemble(simpleInput, level.registryAccess());
            this.currentTick = value.time();
            this.stirFryCount = value.stirFryCount();
            this.status = 1;
            ((PotBlockEntity)(Object)this).refresh();
        } else {
            ci.cancel();
            this.carrier = Ingredient.of(net.minecraft.world.item.Items.BOWL);
            this.result = com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry.getItem(
                com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry.SUSPICIOUS_STIR_FRY).getDefaultInstance();
            this.currentTick = 10 * 20;
            this.stirFryCount = 0;
            this.status = 1;
            ((PotBlockEntity)(Object)this).refresh();
        }
    }
}
