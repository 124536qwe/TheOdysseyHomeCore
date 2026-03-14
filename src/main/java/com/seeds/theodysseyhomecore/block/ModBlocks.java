package com.seeds.theodysseyhomecore.block;

import com.seeds.theodysseyhomecore.EldritchFeast;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(EldritchFeast.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(EldritchFeast.MOD_ID);

    public static final DeferredHolder<Block, Block> ICE_STOVE = BLOCKS.register("ice_stove", 
            () -> new IceStoveBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)));
    
    public static final DeferredHolder<Item, Item> ICE_STOVE_ITEM = ITEMS.register("ice_stove", 
            () -> new BlockItem(ICE_STOVE.get(), new Item.Properties()));

    public static final DeferredHolder<Block, Block> BLAST_FURNACE_BRICK = BLOCKS.register("blast_furnace_brick", 
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)));
    
    public static final DeferredHolder<Item, Item> BLAST_FURNACE_BRICK_ITEM = ITEMS.register("blast_furnace_brick", 
            () -> new BlockItem(BLAST_FURNACE_BRICK.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
