package com.seeds.theodysseyhomecore.block;

import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IceStoveBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<IceStoveBlock> CODEC = simpleCodec(IceStoveBlock::new);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public IceStoveBlock(BlockBehaviour.Properties properties) {
        super(properties
                .lightLevel(state -> state.getValue(LIT) ? 13 : 0)
                .randomTicks());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.SOUTH)
                .setValue(LIT, false));
    }

    private static boolean isIce(ItemStack stack) {
        return stack.is(Items.ICE) || stack.is(Items.PACKED_ICE) || stack.is(Items.BLUE_ICE);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;

            if (random.nextInt(10) == 0) {
                level.playLocalSound(x, y, z,
                        SoundEvents.SNOW_PLACE,
                        SoundSource.BLOCKS,
                        0.5F + random.nextFloat(),
                        random.nextFloat() * 0.7F + 0.6F, false);
            }

            level.addParticle(ParticleTypes.SNOWFLAKE,
                    x + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    y + 0.5 + random.nextDouble() / 3,
                    z + random.nextDouble() / 3 * (random.nextBoolean() ? 1 : -1),
                    0, 0.02, 0);

            Direction direction = state.getValue(FACING);
            Direction.Axis axis = direction.getAxis();
            double offsetRandom = random.nextDouble() * 0.6 - 0.3;
            double xOffset = axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52 : offsetRandom;
            double yOffset = 0.25 + random.nextDouble() * 6.0 / 16.0;
            double zOffset = axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52 : offsetRandom;
            level.addParticle(ParticleTypes.CLOUD,
                    x + xOffset,
                    pos.getY() + yOffset,
                    z + zOffset,
                    0, 0, 0);
        }
    }

    @Override
    public void randomTick(BlockState blockState, net.minecraft.server.level.ServerLevel level, BlockPos pos, RandomSource random) {
        if (blockState.getValue(LIT) && level.isRainingAt(pos.above())) {
            level.setBlockAndUpdate(pos, blockState.setValue(LIT, false));
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(LIT) && levelAccessor.isWaterAt(pos.above()) && levelAccessor instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.setBlockAndUpdate(pos, state.setValue(LIT, false));
            serverLevel.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return super.updateShape(state, direction, neighborState, levelAccessor, pos, neighborPos);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemInHand = player.getItemInHand(hand);
        boolean isShovel = itemInHand.is(net.minecraft.world.item.Items.IRON_SHOVEL) || 
                          itemInHand.is(net.minecraft.world.item.Items.WOODEN_SHOVEL) ||
                          itemInHand.is(net.minecraft.world.item.Items.STONE_SHOVEL) ||
                          itemInHand.is(net.minecraft.world.item.Items.DIAMOND_SHOVEL) ||
                          itemInHand.is(net.minecraft.world.item.Items.GOLDEN_SHOVEL) ||
                          itemInHand.is(net.minecraft.world.item.Items.NETHERITE_SHOVEL) ||
                          itemInHand.is(TagMod.KITCHEN_SHOVEL);
        
        if (!state.getValue(LIT) && isIce(itemInHand)) {
            level.setBlockAndUpdate(pos, state.setValue(LIT, true));
            level.playSound(player, pos,
                    SoundEvents.SNOW_PLACE,
                    SoundSource.BLOCKS, 1.0F,
                    level.getRandom().nextFloat() * 0.4F + 0.8F);
            itemInHand.shrink(1);
            return ItemInteractionResult.SUCCESS;
        }
        
        if (state.getValue(LIT) && isShovel) {
            level.playSound(player, pos,
                    SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.BLOCKS, 0.5F,
                    2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
            level.setBlockAndUpdate(pos, state.setValue(LIT, false));
            return ItemInteractionResult.SUCCESS;
        }
        
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.the_odyssey_home_core.ice_stove").withStyle(ChatFormatting.GRAY));
    }
}
