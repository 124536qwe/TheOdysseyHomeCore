package com.seeds.theodysseyhomecore.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.ChoppingBoardBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.StockpotRecipeBuilder;
import com.seeds.theodysseyhomecore.EldritchFeast;
import com.seeds.theodysseyhomecore.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class EldritchFeastRecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    
    private static final TagKey<Item> RAW_BEEF = TagKey.create(net.minecraft.core.registries.Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/raw_beef"));
    private static final TagKey<Item> CROPS_TOMATO = TagKey.create(net.minecraft.core.registries.Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "crops/tomato"));
    private static final TagKey<Item> RAW_PORK = TagKey.create(net.minecraft.core.registries.Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/raw_pork"));
    private static final TagKey<Item> EGGS = TagKey.create(net.minecraft.core.registries.Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "eggs"));
    private static final TagKey<Item> MUSHROOMS = TagKey.create(net.minecraft.core.registries.Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "mushrooms"));

    public EldritchFeastRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        // 番茄牛肉汤
        StockpotRecipeBuilder.builder()
                .addInput(RAW_BEEF, RAW_BEEF,
                        CROPS_TOMATO, CROPS_TOMATO,
                        CROPS_TOMATO)
                .setResult(Items.SUSPICIOUS_STEW, 2)
                .save(consumer, "beef_tomato_soup");

        // 蘑菇猪肉汤
        StockpotRecipeBuilder.builder()
                .addInput(RAW_PORK, RAW_PORK,
                        MUSHROOMS, MUSHROOMS,
                        MUSHROOMS)
                .setResult(Items.SUSPICIOUS_STEW, 2)
                .save(consumer, "mushroom_pork_soup");

        // 简易鸡蛋汤
        StockpotRecipeBuilder.builder()
                .addInput(EGGS, EGGS, EGGS)
                .setResult(Items.SUSPICIOUS_STEW)
                .save(consumer, "egg_soup");

        // 胡萝卜切4刀 -> 4个胡萝卜片 (使用森罗物语的猪肉模型)
        ChoppingBoardBuilder.builder()
                .setIngredient(Items.CARROT)
                .setResult(ModItems.CARROT_SLICE.get(), 4)
                .setCutCount(4)
                .setModelId(ResourceLocation.fromNamespaceAndPath("kaleidoscope_cookery", "raw_pork_belly"))
                .save(consumer);

        // 捕获的生物切4刀 -> 默认产出2个猪肉 (使用森罗物语的猪肉模型)
        ChoppingBoardBuilder.builder()
                .setIngredient(ModItems.ENTITY_DISPLAY.get())
                .setResult(Items.PORKCHOP, 2)
                .setCutCount(4)
                .setModelId(ResourceLocation.fromNamespaceAndPath("kaleidoscope_cookery", "raw_pork_belly"))
                .save(consumer, "entity_display_default");

        // 村民切4刀 -> 产出村民心脏(数量为5个，用于在Mixin中替换为5个内脏)
        ChoppingBoardBuilder.builder()
                .setIngredient(ModItems.ENTITY_DISPLAY.get())
                .setResult(ModItems.HEART_OF_VILLAGER.get(), 5)
                .setCutCount(4)
                .setModelId(ResourceLocation.fromNamespaceAndPath("kaleidoscope_cookery", "raw_pork_belly"))
                .save(consumer, "entity_display_villager");
    }
}
