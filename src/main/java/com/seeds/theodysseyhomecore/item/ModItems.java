package com.seeds.theodysseyhomecore.item;

import com.seeds.theodysseyhomecore.EldritchFeast;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(EldritchFeast.MOD_ID);

    public static final DeferredHolder<Item, Item> HEART_OF_VILLAGER = ITEMS.register("heart_of_villager", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HEAD_OF_VILLAGER = ITEMS.register("head_of_villager", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ARM_OF_VILLAGER = ITEMS.register("arm_of_villager", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LEG_OF_VILLAGER = ITEMS.register("leg_of_villager", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BODY_OF_VILLAGER = ITEMS.register("body_of_villager", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MEAT_OF_VILLAGER = ITEMS.register("meat_of_villager", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> KIDNEY_OF_VILLAGER = ITEMS.register("kidney_of_villager", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LIVER_OF_VILLAGER = ITEMS.register("liver_of_villager", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> LUNG_OF_VILLAGER = ITEMS.register("lung_of_villager", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GHAST_TENTACLE = ITEMS.register("ghast_tentacle", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GHAST_SLICE = ITEMS.register("ghast_slice", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> RAW_HOGLIN_HAM = ITEMS.register("raw_hoglin_ham", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COOKED_HOGLIN_HAM = ITEMS.register("cooked_hoglin_ham", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> RAW_STRIDER_MEAT = ITEMS.register("raw_strider_meat", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> COOKED_STRIDER_MEAT = ITEMS.register("cooked_strider_meat", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WARPED_TUMOR = ITEMS.register("warped_tumor", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CRIMSON_FRUIT = ITEMS.register("crimson_fruit", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SOUL_BERRY = ITEMS.register("soul_berry", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> CORN = ITEMS.register("corn", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CORN_SEED = ITEMS.register("corn_seed", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EGGPLANT = ITEMS.register("eggplant", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EGGPLANT_SEED = ITEMS.register("eggplant_seed", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> STARFRUIT = ITEMS.register("starfruit", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> STARFRUIT_SEED = ITEMS.register("starfruit_seed", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> CARROT_SLICE = ITEMS.register("carrot_slice", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> ENTITY_DISPLAY = ITEMS.register("entity_display", EntityDisplayItem::new);
    public static final DeferredHolder<Item, Item> DEV_BUG_NET = ITEMS.register("klein_net", KleinNetItem::new);

    public static final DeferredHolder<Item, Item> ENGINEERING_HAMMER = ITEMS.register("engineering_hammer", 
            () -> new EngineeringHammerItem(new Item.Properties()));

    public static final DeferredHolder<Item, Item> BLAST_FURNACE_CORE = ITEMS.register("blast_furnace_core", 
            () -> new net.minecraft.world.item.BlockItem(
                    com.seeds.theodysseyhomecore.block.BlastFurnaceCore.BLAST_FURNACE_CORE.get(), 
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
