package com.seeds.theodysseyhomecore.multiblock;

import com.seeds.theodysseyhomecore.block.ModBlocks;
import net.minecraft.world.level.block.Blocks;

public class BlastFurnacePattern extends MultiblockPattern {
    public static final BlastFurnacePattern INSTANCE = new BlastFurnacePattern();

    private BlastFurnacePattern() {
        super("blast_furnace", 0, 2, 0, 3, 0, 2);
        buildPattern();
    }

    @Override
    protected void buildPattern() {
        var brick = ModBlocks.BLAST_FURNACE_BRICK.get();

        for (int x = 0; x <= 2; x++) {
            for (int z = 0; z <= 2; z++) {
                addRequiredBlock(x, 0, z, brick);
            }
        }

        addCorePosition(1, 1, 0);
        addCorePosition(1, 1, 2);
        addCorePosition(0, 1, 1);
        addCorePosition(2, 1, 1);

        addRequiredBlock(0, 2, 0, brick);
        addRequiredBlock(2, 2, 0, brick);
        addRequiredBlock(0, 2, 2, brick);
        addRequiredBlock(2, 2, 2, brick);
        addRequiredBlock(0, 2, 1, brick);
        addRequiredBlock(2, 2, 1, brick);
        addRequiredBlock(1, 2, 0, brick);
        addRequiredBlock(1, 2, 2, brick);

        addRequiredBlock(0, 3, 0, brick);
        addRequiredBlock(2, 3, 0, brick);
        addRequiredBlock(0, 3, 2, brick);
        addRequiredBlock(2, 3, 2, brick);
        addRequiredBlock(0, 3, 1, brick);
        addRequiredBlock(2, 3, 1, brick);
        addRequiredBlock(1, 3, 0, brick);
        addRequiredBlock(1, 3, 2, brick);
    }
}
