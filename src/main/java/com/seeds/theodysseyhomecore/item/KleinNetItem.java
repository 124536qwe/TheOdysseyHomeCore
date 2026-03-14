package com.seeds.theodysseyhomecore.item;

import com.seeds.theodysseyhomecore.EldritchFeast;
import com.seeds.theodysseyhomecore.util.ItemStackUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;

import java.util.function.Function;

public class KleinNetItem extends net.minecraft.world.item.Item {
    private static final double REACH = 15.0;
    private static final double HEALTH_THRESHOLD = 0.1;
    private static final double BASE_SCALE = 0.5;

    public KleinNetItem() {
        super(new net.minecraft.world.item.Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (usedHand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(stack);

        if (player.isCrouching()) {
            return tryReleaseEntity(level, player, usedHand, stack);
        }

        double reach = REACH;
        double squared = Mth.square(reach);
        Vec3 from = player.getEyePosition(1.0F);
        HitResult hitResult = player.pick(reach, 1.0F, false);
        double sqr = hitResult.getLocation().distanceToSqr(from);
        if (hitResult.getType() != HitResult.Type.MISS) {
            squared = sqr;
            reach = Math.sqrt(sqr);
        }
        Vec3 viewVector = player.getViewVector(1.0F);
        Vec3 to = from.add(viewVector.x * reach, viewVector.y * reach, viewVector.z * reach);
        AABB aabb = player.getBoundingBox().expandTowards(viewVector.scale(reach)).inflate(2.0);
        
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                player, from, to, aabb, entity -> !entity.isSpectator() && (entity.isPickable() || entity instanceof net.minecraft.world.entity.npc.Villager), squared
        );
        
        if (entityHitResult != null && entityHitResult.getLocation().distanceToSqr(from) < sqr) {
            LivingEntity interactionTarget = getLivingEntity(entityHitResult);
            if (interactionTarget == null) {
                return InteractionResultHolder.pass(stack);
            }
            
            if (!canCapture(interactionTarget, player)) {
                return InteractionResultHolder.pass(stack);
            }
            
            if (player.isLocalPlayer()) {
                ((LocalPlayer) player).connection.send(
                    ServerboundInteractPacket.createInteractionPacket(interactionTarget, false, InteractionHand.MAIN_HAND)
                );
            } else {
                captureEntity(player, interactionTarget);
            }
        }

        return InteractionResultHolder.success(stack);
    }

    private InteractionResultHolder<ItemStack> tryReleaseEntity(Level level, Player player, InteractionHand hand, ItemStack stack) {
        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        CompoundTag tag = ItemStackUtils.getItemStackNbtIfPresent(stack);
        if (tag == null) {
            return InteractionResultHolder.pass(stack);
        }

        Entity entity = EntityType.loadEntityRecursive(tag, level, Function.identity());
        if (entity != null) {
            Vec3 eyePos = player.getEyePosition();
            Vec3 lookDir = player.getViewVector(1.0F);
            Vec3 spawnPos = eyePos.add(lookDir.x * 2, lookDir.y * 2 - 1, lookDir.z * 2);
            entity.setPos(spawnPos);

            if (stack.has(DataComponents.CUSTOM_NAME)) {
                entity.setCustomName(stack.get(DataComponents.CUSTOM_NAME));
            }

            level.addFreshEntity(entity);
        }

        stack.shrink(1);
        if (stack.isEmpty()) {
            player.setItemInHand(hand, ItemStack.EMPTY);
        }

        return InteractionResultHolder.success(stack);
    }

    private LivingEntity getLivingEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity living) {
            return living;
        } else if (entityHitResult.getEntity() instanceof PartEntity<?> part && part.getParent() instanceof LivingEntity living) {
            return living;
        }
        return null;
    }

    private boolean canCapture(LivingEntity entity, Player player) {
        if (entity instanceof Player) {
            return false;
        }
        
        if (player.isCreative()) {
            return true;
        }
        
        float healthThreshold = Math.max(1.0f, entity.getMaxHealth() * (float) HEALTH_THRESHOLD);
        return entity.getHealth() < healthThreshold;
    }

    private void captureEntity(Player player, LivingEntity entity) {
        ItemStack itemStack = ModItems.ENTITY_DISPLAY.get().getDefaultInstance();
        
        double entitySize = entity.getBoundingBox().getSize();
        double scale = BASE_SCALE / entitySize;
        
        entity.setYRot(0.0F);
        entity.setYHeadRot(0.0F);
        entity.setYBodyRot(0.0F);
        entity.setXRot(0.0F);
        entity.stopRiding();
        
        CompoundTag tag = new CompoundTag();
        entity.save(tag);
        tag.putDouble("Scale", scale);
        ItemStackUtils.setItemStackNbt(itemStack, tag);
        
        if (entity.hasCustomName()) {
            itemStack.set(DataComponents.CUSTOM_NAME, entity.getCustomName());
        }
        
        if (!player.addItem(itemStack)) {
            player.drop(itemStack, false);
        }
        
        entity.discard();
    }

    public static void captureEntityServer(LivingEntity entity, Player player) {
        ItemStack itemStack = ModItems.ENTITY_DISPLAY.get().getDefaultInstance();
        
        double entitySize = entity.getBoundingBox().getSize();
        double scale = BASE_SCALE / entitySize;
        
        entity.setYRot(0.0F);
        entity.setYHeadRot(0.0F);
        entity.setYBodyRot(0.0F);
        entity.setXRot(0.0F);
        entity.stopRiding();
        
        CompoundTag tag = new CompoundTag();
        entity.save(tag);
        tag.putDouble("Scale", scale);
        ItemStackUtils.setItemStackNbt(itemStack, tag);
        
        if (entity.hasCustomName()) {
            itemStack.set(DataComponents.CUSTOM_NAME, entity.getCustomName());
        }
        
        if (!player.addItem(itemStack)) {
            player.drop(itemStack, false);
        }
        
        entity.discard();
    }
}
