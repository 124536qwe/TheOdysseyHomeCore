package com.seeds.theodysseyhomecore.mixin;

import com.seeds.theodysseyhomecore.item.KleinNetItem;
import com.seeds.theodysseyhomecore.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void onInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> ci) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (stack.is(ModItems.DEV_BUG_NET.get())) {
            Entity entity = (Entity) (Object) this;
            
            if (!(entity instanceof LivingEntity livingEntity)) {
                return;
            }
            
            if (player instanceof net.minecraft.client.player.LocalPlayer) {
                return;
            }
            
            boolean canCapture = canCapture(livingEntity, player);
            
            if (canCapture) {
                KleinNetItem.captureEntityServer(livingEntity, player);
                ci.setReturnValue(InteractionResult.SUCCESS);
                ci.cancel();
            }
        }
    }

    private boolean canCapture(LivingEntity entity, Player player) {
        if (entity instanceof Player) {
            return false;
        }
        
        if (player.isCreative()) {
            return true;
        }
        
        float healthThreshold = Math.max(1.0f, entity.getMaxHealth() * 0.1f);
        return entity.getHealth() < healthThreshold;
    }
}
