package com.seeds.theodysseyhomecore.block;

import com.seeds.theodysseyhomecore.multiblock.MultiblockDetector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.Blocks;

public class BlastFurnaceCoreBlock extends Block implements EntityBlock {
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");

    public BlastFurnaceCoreBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FORMED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return ( lvl, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof BlastFurnaceCoreEntity entity) {
                entity.tick();
            }
        };
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlastFurnaceCoreEntity(pos, state);
    }
}
