package com.seeds.theodysseyhomecore.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ChoppingBoardBlockEntity;
import com.seeds.theodysseyhomecore.item.ModItems;
import com.seeds.theodysseyhomecore.util.ItemStackUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(ChoppingBoardBlockEntity.class)
public abstract class ChoppingBoardBlockEntityMixin {
    @Shadow
    private ResourceLocation modelId;
    @Shadow
    private int maxCutCount;
    @Shadow
    private int currentCutCount;
    @Shadow
    private ItemStack currentCutStack;
    @Shadow
    private ItemStack result;

    @Unique
    private static final ResourceLocation PORK_MODEL = ResourceLocation.fromNamespaceAndPath("kaleidoscope_cookery", "raw_pork_belly");

    @Unique
    private static final EntityCutResult DEFAULT_RESULT = new EntityCutResult(
            List.of(Items.PORKCHOP, Items.PORKCHOP),
            PORK_MODEL,
            "entity_display_default"
    );

    @Unique
    private static final Map<String, EntityCutResult> RECIPE_CUT_RESULTS = Map.of(
            "entity_display_villager", new EntityCutResult(
                    List.of(
                            ModItems.HEART_OF_VILLAGER.get(),
                            ModItems.LIVER_OF_VILLAGER.get(),
                            ModItems.LUNG_OF_VILLAGER.get(),
                            ModItems.KIDNEY_OF_VILLAGER.get(),
                            ModItems.BODY_OF_VILLAGER.get()
                    ),
                    PORK_MODEL,
                    "entity_display_villager"
            )
    );

    @Unique
    private record EntityCutResult(
            List<net.minecraft.world.level.ItemLike> outputs,
            ResourceLocation modelId,
            String recipeName
    ) {}

    @Unique
    private EntityCutResult eldritchfeast$currentCutResult = DEFAULT_RESULT;

    @Unique
    private boolean eldritchfeast$isEntityDisplay = false;

    @Inject(method = "onPutItem", at = @At("HEAD"), cancellable = true)
    private void eldritchfeast$handleEntityDisplayItem(Level level, LivingEntity user, ItemStack putOnItem, CallbackInfoReturnable<Boolean> cir) {
        if (putOnItem.is(ModItems.ENTITY_DISPLAY.get())) {
            CompoundTag tag = ItemStackUtils.getItemStackNbtIfPresent(putOnItem);
            EntityCutResult cutResult = DEFAULT_RESULT;

            if (tag != null && tag.contains("id")) {
                String entityId = tag.getString("id");
                if (entityId.contains(":")) {
                    entityId = entityId.substring(entityId.indexOf(":") + 1);
                }
                String recipeName = "entity_display_" + entityId;
                cutResult = RECIPE_CUT_RESULTS.getOrDefault(recipeName, DEFAULT_RESULT);
            }

            this.eldritchfeast$currentCutResult = cutResult;
            this.eldritchfeast$isEntityDisplay = true;
            this.currentCutStack = putOnItem.split(1);
            this.modelId = cutResult.modelId();
            this.maxCutCount = 4;
            this.currentCutCount = 0;
            this.result = new ItemStack(Items.PORKCHOP);

            ChoppingBoardBlockEntity board = (ChoppingBoardBlockEntity) (Object) this;
            board.refresh();
            level.playSound(null, board.getBlockPos(),
                    net.minecraft.sounds.SoundEvents.WOOD_PLACE,
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    1, 1.2F);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "onCutItem", at = @At("HEAD"), cancellable = true)
    private void eldritchfeast$handleCutComplete(Level level, LivingEntity user, net.minecraft.world.item.ItemStack cutterItem, CallbackInfoReturnable<Boolean> cir) {
        // 只有当物品是 entity_display 且切割完成时才执行自定义逻辑
        if (this.eldritchfeast$isEntityDisplay && this.currentCutCount >= this.maxCutCount && this.maxCutCount > 0 && !this.currentCutStack.isEmpty()) {
            ChoppingBoardBlockEntity board = (ChoppingBoardBlockEntity) (Object) this;

            // 生成所有产物
            for (net.minecraft.world.level.ItemLike item : this.eldritchfeast$currentCutResult.outputs()) {
                this.eldritchfeast$spawnItem(level, board.getBlockPos(), new ItemStack(item));
            }

            this.resetBoardData();
            level.playSound(null, board.getBlockPos(),
                    net.minecraft.sounds.SoundEvents.WOOD_PLACE,
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    1, 2 + level.random.nextFloat() * 0.2f);
            cir.setReturnValue(true);
        }
    }

    @Unique
    private void resetBoardData() {
        this.modelId = null;
        this.result = ItemStack.EMPTY;
        this.currentCutStack = ItemStack.EMPTY;
        this.currentCutCount = 0;
        this.maxCutCount = 0;
        this.eldritchfeast$currentCutResult = DEFAULT_RESULT;
        this.eldritchfeast$isEntityDisplay = false;
        ChoppingBoardBlockEntity board = (ChoppingBoardBlockEntity) (Object) this;
        board.refresh();
    }

    @Unique
    private void eldritchfeast$spawnItem(Level level, BlockPos pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity(level,
                pos.getX() + 0.5,
                pos.getY() + 0.25,
                pos.getZ() + 0.5,
                stack, 0, 0, 0);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
    }
}
