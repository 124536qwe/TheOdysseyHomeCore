package com.seeds.theodysseyhomecore.block;

import com.seeds.theodysseyhomecore.multiblock.MultiblockDetector;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnaceCoreEntity extends BlockEntity {
    private boolean formed = false;
    private int tickCounter = 0;

    public BlastFurnaceCoreEntity(BlockPos pos, BlockState state) {
        super(BlastFurnaceCore.BLAST_FURNACE_CORE_ENTITY.get(), pos, state);
    }

    public void setFormed(boolean formed) {
        this.formed = formed;
        if (this.level != null) {
            BlockState state = this.getBlockState();
            this.level.setBlock(this.worldPosition, state.setValue(BlastFurnaceCoreBlock.FORMED, formed), 3);
        }
        setChanged();
    }

    public boolean isFormed() {
        return formed;
    }

    public void tick() {
        if (formed && level instanceof ServerLevel serverLevel) {
            tickCounter++;
            if (tickCounter >= 10) {
                tickCounter = 0;
                if (!MultiblockDetector.checkFormed(serverLevel, worldPosition)) {
                    setFormed(false);
                }
            }
        }
    }
}
