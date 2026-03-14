package com.seeds.theodysseyhomecore.item;

import com.seeds.theodysseyhomecore.block.BlastFurnaceCore;
import com.seeds.theodysseyhomecore.block.BlastFurnaceCoreEntity;
import com.seeds.theodysseyhomecore.multiblock.MultiblockDetector;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EngineeringHammerItem extends Item {

    public EngineeringHammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = ctx.getPlayer();

        if (player == null) {
            return InteractionResult.PASS;
        }

        if (state.is(com.seeds.theodysseyhomecore.block.BlastFurnaceCore.BLAST_FURNACE_CORE.get())) {
            if (level.getBlockEntity(pos) instanceof BlastFurnaceCoreEntity entity) {
                if (entity.isFormed()) {
                    player.displayClientMessage(Component.literal("Already formed!"), true);
                    return InteractionResult.SUCCESS;
                }
                
                MultiblockDetector.DetectResult result = MultiblockDetector.detect(level, pos, "blast_furnace");
                
                if (result.success()) {
                    entity.setFormed(true);
                }
                
                player.displayClientMessage(Component.literal(result.message()), true);
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(ctx);
    }
}
