package com.seeds.theodysseyhomecore.item;

import com.seeds.theodysseyhomecore.util.ItemStackUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class EntityDisplayItem extends net.minecraft.world.item.Item {
    public static final String SCALE_TAG = "Scale";

    public EntityDisplayItem() {
        super(new net.minecraft.world.item.Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 下蹲 + 右键：释放生物
        if (player.isCrouching()) {
            if (level.isClientSide()) {
                return InteractionResultHolder.success(stack);
            }

            CompoundTag tag = ItemStackUtils.getItemStackNbtIfPresent(stack);
            if (tag == null) {
                return InteractionResultHolder.pass(stack);
            }

            // 从NBT恢复实体
            Entity entity = EntityType.loadEntityRecursive(tag, level, Function.identity());
            if (entity != null) {
                // 放置在玩家前方位置
                Vec3 eyePos = player.getEyePosition();
                Vec3 lookDir = player.getViewVector(1.0F);
                Vec3 spawnPos = eyePos.add(lookDir.x * 2, lookDir.y * 2 - 1, lookDir.z * 2);
                entity.setPos(spawnPos);

                // 恢复自定义名称
                if (stack.has(DataComponents.CUSTOM_NAME)) {
                    entity.setCustomName(stack.get(DataComponents.CUSTOM_NAME));
                }

                level.addFreshEntity(entity);
            }

            // 消耗物品
            stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, ItemStack.EMPTY);
            }

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag tag = ItemStackUtils.getItemStackNbtIfPresent(stack);
        if (tag == null) return super.getName(stack);
        return EntityType.by(tag).orElse(EntityType.PIG).getDescription();
    }
}
