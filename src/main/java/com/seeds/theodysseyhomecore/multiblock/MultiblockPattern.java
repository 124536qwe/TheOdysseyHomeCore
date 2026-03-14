package com.seeds.theodysseyhomecore.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public abstract class MultiblockPattern {
    protected final String name;
    protected final int minX, maxX, minY, maxY, minZ, maxZ;
    protected final Set<RelativeBlockPos> requiredBlocks = new HashSet<>();
    protected final Set<RelativeBlockPos> corePositions = new HashSet<>();

    protected MultiblockPattern(String name, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        this.name = name;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    protected abstract void buildPattern();

    protected void addRequiredBlock(int x, int y, int z, Block block) {
        requiredBlocks.add(new RelativeBlockPos(x, y, z, block));
    }

    protected void addCorePosition(int x, int y, int z) {
        corePositions.add(new RelativeBlockPos(x, y, z, null));
    }

    public boolean check(LevelReader world, BlockPos clickedPos) {
        for (RelativeBlockPos relPos : requiredBlocks) {
            BlockPos checkPos = clickedPos.offset(relPos.x, relPos.y, relPos.z);
            BlockState state = world.getBlockState(checkPos);
            if (!state.is(relPos.block)) {
                return false;
            }
        }
        return true;
    }

    public boolean isCorePosition(int x, int y, int z) {
        return corePositions.contains(new RelativeBlockPos(x, y, z, null));
    }

    public Set<RelativeBlockPos> getCorePositions() {
        return corePositions;
    }

    public Set<RelativeBlockPos> getRequiredBlocks() {
        return requiredBlocks;
    }

    public String getName() {
        return name;
    }

    public int getMinX() { return minX; }
    public int getMaxX() { return maxX; }
    public int getMinY() { return minY; }
    public int getMaxY() { return maxY; }
    public int getMinZ() { return minZ; }
    public int getMaxZ() { return maxZ; }

    public record RelativeBlockPos(int x, int y, int z, Block block) {}
}
