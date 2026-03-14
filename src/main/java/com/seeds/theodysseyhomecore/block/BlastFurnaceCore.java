package com.seeds.theodysseyhomecore.block;

import com.seeds.theodysseyhomecore.EldritchFeast;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BlastFurnaceCore {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = 
            DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, EldritchFeast.MOD_ID);

    public static final DeferredHolder<Block, Block> BLAST_FURNACE_CORE = 
            com.seeds.theodysseyhomecore.block.ModBlocks.BLOCKS.register("blast_furnace_core",
                    () -> new BlastFurnaceCoreBlock(Block.Properties.of()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlastFurnaceCoreEntity>> BLAST_FURNACE_CORE_ENTITY = 
            BLOCK_ENTITY_TYPES.register("blast_furnace_core", 
                    () -> BlockEntityType.Builder.of(BlastFurnaceCoreEntity::new, BLAST_FURNACE_CORE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
