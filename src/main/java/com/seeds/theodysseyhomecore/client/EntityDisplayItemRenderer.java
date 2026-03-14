package com.seeds.theodysseyhomecore.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.seeds.theodysseyhomecore.util.ItemStackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class EntityDisplayItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final Map<UUID, Entity> entityMap = new HashMap<>();
    private Level currentLevel;

    private static final double DEFAULT_SCALE = 0.75;

    public EntityDisplayItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null) {
            renderDefault(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
            return;
        }
        
        CompoundTag tag = ItemStackUtils.getItemStackNbtIfPresent(stack);
        Entity entity;
        double scale = DEFAULT_SCALE;
        
        if (tag == null) {
            renderDefault(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
            return;
        }
        
        if (level != currentLevel) {
            currentLevel = level;
            entityMap.clear();
        }
        
        if (tag.contains("Scale")) {
            scale = tag.getDouble("Scale");
        }
        
        try {
            UUID entityUuid = tag.getUUID("UUID");
            entity = entityMap.computeIfAbsent(entityUuid, uuid -> {
                Entity loaded = EntityType.loadEntityRecursive(tag, level, e -> {
                    e.setPos(0, 0, 0);
                    return e;
                });
                return loaded == null ? null : loaded;
            });
            
            if (entity != null) {
                entity.refreshDimensions();
            }
        } catch (Exception e) {
            entity = null;
        }
        
        if (entity == null) {
            entity = new Pig(EntityType.PIG, level);
            entity.setPos(0, 0, 0);
        }

        poseStack.pushPose();
        EntityRenderDispatcher erd = minecraft.getEntityRenderDispatcher();
        MultiBufferSource.BufferSource immediate = minecraft.renderBuffers().bufferSource();
        erd.setRenderShadow(false);
        
        poseStack.translate(0.5F, 0.25F, 0.5F);
        poseStack.scale((float) scale, (float) scale, (float) scale);
        
        float rotation = 0.0F;
        if (displayContext == ItemDisplayContext.GUI) {
            rotation = Mth.HALF_PI * -0.5F;
        } else if (displayContext == ItemDisplayContext.FIXED) {
            rotation = Mth.PI * 0.75F;
        }
        poseStack.mulPose(com.mojang.math.Axis.YP.rotation(rotation));
        erd.render(entity, 0, 0, 0, 0, 1f, poseStack, immediate, packedLight);
        erd.setRenderShadow(true);
        immediate.endBatch();
        poseStack.popPose();
    }
    
    private void renderDefault(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Minecraft minecraft = Minecraft.getInstance();
        
        poseStack.pushPose();
        EntityRenderDispatcher erd = minecraft.getEntityRenderDispatcher();
        MultiBufferSource.BufferSource immediate = minecraft.renderBuffers().bufferSource();
        erd.setRenderShadow(false);
        
        poseStack.translate(0.5F, 0.25F, 0.5F);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        
        float rotation = 0.0F;
        if (displayContext == ItemDisplayContext.GUI) {
            rotation = Mth.HALF_PI * -0.5F;
        } else if (displayContext == ItemDisplayContext.FIXED) {
            rotation = Mth.PI * 0.75F;
        }
        poseStack.mulPose(com.mojang.math.Axis.YP.rotation(rotation));
        
        Entity pig = new Pig(EntityType.PIG, minecraft.level);
        pig.setPos(0, 0, 0);
        erd.render(pig, 0, 0, 0, 0, 1f, poseStack, immediate, packedLight);
        erd.setRenderShadow(true);
        immediate.endBatch();
        poseStack.popPose();
    }
}
