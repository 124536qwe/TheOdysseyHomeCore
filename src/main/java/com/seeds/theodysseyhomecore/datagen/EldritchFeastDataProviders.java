package com.seeds.theodysseyhomecore.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class EldritchFeastDataProviders {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var registries = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new EldritchFeastRecipeProvider(packOutput, registries));
    }
}
