package com.seeds.theodysseyhomecore.multiblock;

import com.seeds.theodysseyhomecore.block.BlastFurnaceCore;
import com.seeds.theodysseyhomecore.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MultiblockDetector {
    public static DetectResult detect(Level level, BlockPos clickedPos, String patternName) {
        MultiblockPattern pattern = MultiblockRegistry.getPattern(patternName);
        if (pattern == null) {
            return new DetectResult(false, "Unknown pattern: " + patternName);
        }

        Block coreBlock = BlastFurnaceCore.BLAST_FURNACE_CORE.get();
        Block brickBlock = ModBlocks.BLAST_FURNACE_BRICK.get();

        if (level.getBlockState(clickedPos).is(coreBlock)) {
            for (MultiblockPattern.RelativeBlockPos coreRelPos : pattern.getCorePositions()) {
                BlockPos basePos = clickedPos.offset(-coreRelPos.x(), -coreRelPos.y(), -coreRelPos.z());
                
                boolean allValid = true;
                
                for (int x = 0; x <= 2; x++) {
                    for (int z = 0; z <= 2; z++) {
                        BlockPos checkPos = basePos.offset(x, 0, z);
                        if (!level.getBlockState(checkPos).is(brickBlock)) {
                            allValid = false;
                            break;
                        }
                    }
                    if (!allValid) break;
                }
                
                if (!allValid) continue;
                
                for (int x = 0; x <= 2; x++) {
                    for (int z = 0; z <= 2; z++) {
                        if (x == coreRelPos.x() && z == coreRelPos.z()) {
                            BlockPos checkPos = basePos.offset(x, 1, z);
                            if (!level.getBlockState(checkPos).is(coreBlock)) {
                                allValid = false;
                                break;
                            }
                        } else if (x == 1 && z == 1) {
                            BlockPos checkPos = basePos.offset(x, 1, z);
                            if (!level.getBlockState(checkPos).is(Blocks.AIR)) {
                                allValid = false;
                                break;
                            }
                        } else {
                            BlockPos checkPos = basePos.offset(x, 1, z);
                            if (!level.getBlockState(checkPos).is(brickBlock)) {
                                allValid = false;
                                break;
                            }
                        }
                    }
                    if (!allValid) break;
                }
                
                if (!allValid) continue;
                
                for (int x = 0; x <= 2; x++) {
                    for (int z = 0; z <= 2; z++) {
                        if (x == 1 && z == 1) continue;
                        BlockPos checkPos = basePos.offset(x, 2, z);
                        if (!level.getBlockState(checkPos).is(brickBlock)) {
                            allValid = false;
                            break;
                        }
                    }
                    if (!allValid) break;
                }
                
                if (!allValid) continue;
                
                for (int x = 0; x <= 2; x++) {
                    for (int z = 0; z <= 2; z++) {
                        if (x == 1 && z == 1) continue;
                        BlockPos checkPos = basePos.offset(x, 3, z);
                        if (!level.getBlockState(checkPos).is(brickBlock)) {
                            allValid = false;
                            break;
                        }
                    }
                    if (!allValid) break;
                }
                
                if (allValid) {
                    return new DetectResult(true, "Structure formed!");
                }
            }
        }

        return new DetectResult(false, "Structure incomplete");
    }

    public static boolean isValidCorePosition(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(BlastFurnaceCore.BLAST_FURNACE_CORE.get());
    }

    public static boolean checkFormed(Level level, BlockPos corePos) {
        MultiblockPattern pattern = MultiblockRegistry.getPattern("blast_furnace");
        if (pattern == null) return false;

        Block coreBlock = BlastFurnaceCore.BLAST_FURNACE_CORE.get();
        Block brickBlock = ModBlocks.BLAST_FURNACE_BRICK.get();

        BlockState coreState = level.getBlockState(corePos);
        if (!coreState.is(coreBlock)) return false;

        for (MultiblockPattern.RelativeBlockPos coreRelPos : pattern.getCorePositions()) {
            BlockPos basePos = corePos.offset(-coreRelPos.x(), -coreRelPos.y(), -coreRelPos.z());
            
            boolean allValid = true;
            
            for (int x = 0; x <= 2; x++) {
                for (int z = 0; z <= 2; z++) {
                    BlockPos checkPos = basePos.offset(x, 0, z);
                    if (!level.getBlockState(checkPos).is(brickBlock)) {
                        allValid = false;
                        break;
                    }
                }
                if (!allValid) break;
            }
            
            if (!allValid) continue;
            
            for (int x = 0; x <= 2; x++) {
                for (int z = 0; z <= 2; z++) {
                    if (x == coreRelPos.x() && z == coreRelPos.z()) {
                        BlockPos checkPos = basePos.offset(x, 1, z);
                        if (!level.getBlockState(checkPos).is(coreBlock)) {
                            allValid = false;
                            break;
                        }
                    } else if (x == 1 && z == 1) {
                        BlockPos checkPos = basePos.offset(x, 1, z);
                        if (!level.getBlockState(checkPos).is(Blocks.AIR)) {
                            allValid = false;
                            break;
                        }
                    } else {
                        BlockPos checkPos = basePos.offset(x, 1, z);
                        if (!level.getBlockState(checkPos).is(brickBlock)) {
                            allValid = false;
                            break;
                        }
                    }
                }
                if (!allValid) break;
            }
            
            if (!allValid) continue;
            
            for (int x = 0; x <= 2; x++) {
                for (int z = 0; z <= 2; z++) {
                    if (x == 1 && z == 1) continue;
                    BlockPos checkPos = basePos.offset(x, 2, z);
                    if (!level.getBlockState(checkPos).is(brickBlock)) {
                        allValid = false;
                        break;
                    }
                }
                if (!allValid) break;
            }
            
            if (!allValid) continue;
            
            for (int x = 0; x <= 2; x++) {
                for (int z = 0; z <= 2; z++) {
                    if (x == 1 && z == 1) continue;
                    BlockPos checkPos = basePos.offset(x, 3, z);
                    if (!level.getBlockState(checkPos).is(brickBlock)) {
                        allValid = false;
                        break;
                    }
                }
                if (!allValid) break;
            }
            
            if (allValid) {
                return true;
            }
        }
        
        return false;
    }

    public record DetectResult(boolean success, String message) {}
}
