package com.seeds.theodysseyhomecore;

import com.seeds.theodysseyhomecore.block.BlastFurnaceCore;
import com.seeds.theodysseyhomecore.block.ModBlocks;
import com.seeds.theodysseyhomecore.item.ModCreativeTabs;
import com.seeds.theodysseyhomecore.item.ModItems;
import com.seeds.theodysseyhomecore.multiblock.MultiblockRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

@Mod(EldritchFeast.MOD_ID)
public class EldritchFeast {
    public static final String MOD_ID = "the_odyssey_home_core";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EldritchFeast(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        BlastFurnaceCore.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        MultiblockRegistry.init();
    }
}
