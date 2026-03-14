package com.seeds.theodysseyhomecore.tag;

import com.seeds.theodysseyhomecore.EldritchFeast;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface EldritchTags {
    TagKey<Block> COLD_SOURCE_BLOCKS = blockTag("cold_source_blocks");

    static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(EldritchFeast.MOD_ID, name));
    }
}
