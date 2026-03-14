package com.seeds.theodysseyhomecore.item;

import com.seeds.theodysseyhomecore.EldritchFeast;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EldritchFeast.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ELDRITCH_FOOD_TAB = CREATIVE_MODE_TABS.register("eldritch_food", 
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.the_odyssey_home_core.eldritch_food"))
                    .icon(() -> new ItemStack(ModItems.MEAT_OF_VILLAGER.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.HEART_OF_VILLAGER.get());
                        output.accept(ModItems.HEAD_OF_VILLAGER.get());
                        output.accept(ModItems.ARM_OF_VILLAGER.get());
                        output.accept(ModItems.LEG_OF_VILLAGER.get());
                        output.accept(ModItems.BODY_OF_VILLAGER.get());
                        output.accept(ModItems.MEAT_OF_VILLAGER.get());
                        output.accept(ModItems.KIDNEY_OF_VILLAGER.get());
                        output.accept(ModItems.LIVER_OF_VILLAGER.get());
                        output.accept(ModItems.LUNG_OF_VILLAGER.get());
                        output.accept(ModItems.GHAST_TENTACLE.get());
                        output.accept(ModItems.GHAST_SLICE.get());
                        output.accept(ModItems.RAW_HOGLIN_HAM.get());
                        output.accept(ModItems.COOKED_HOGLIN_HAM.get());
                        output.accept(ModItems.RAW_STRIDER_MEAT.get());
                        output.accept(ModItems.COOKED_STRIDER_MEAT.get());
                        output.accept(ModItems.WARPED_TUMOR.get());
                        output.accept(ModItems.CRIMSON_FRUIT.get());
                        output.accept(ModItems.SOUL_BERRY.get());
                        output.accept(ModItems.DEV_BUG_NET.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
