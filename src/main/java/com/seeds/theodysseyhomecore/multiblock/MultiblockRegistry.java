package com.seeds.theodysseyhomecore.multiblock;

import java.util.HashMap;
import java.util.Map;

public class MultiblockRegistry {
    private static final Map<String, MultiblockPattern> PATTERNS = new HashMap<>();

    public static void register(MultiblockPattern pattern) {
        PATTERNS.put(pattern.getName(), pattern);
    }

    public static MultiblockPattern getPattern(String name) {
        return PATTERNS.get(name);
    }

    public static void init() {
        register(BlastFurnacePattern.INSTANCE);
    }
}
