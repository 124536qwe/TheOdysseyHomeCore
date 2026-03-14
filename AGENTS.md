# AGENTS.md - The Odyssey Home Core Mod Development Guide

This document provides guidelines for AI agents working on the The Odyssey Home Core mod codebase.

## Project Overview

- **Mod ID**: `the_odyssey_home_core`
- **Package**: `com.seeds.theodysseyhomecore`
- **Minecraft Version**: 1.21.1
- **NeoForge Version**: 21.1.219
- **Build Tool**: Gradle 9.2.1 with NeoForge ModDev plugin

## Build Commands

### Standard Build
```bash
./gradlew build          # Build the mod JAR
./gradlew clean          # Clean build artifacts
```

### Running the Game
```bash
./gradlew runClient      # Launch Minecraft client
./gradlew runServer      # Launch dedicated server (no GUI)
```

### Testing
```bash
./gradlew test           # Run unit tests
./gradlew gameTestServer # Run GameTest tests and exit
```

To run a **single test**:
```bash
./gradlew test --tests "FullyQualifiedTestClassName.testMethodName"
```

### Data Generation
```bash
./gradlew runData        # Run data generators (recipes, tags, loot tables, etc.)
```

### IDE Integration
```bash
./gradlew genIntelliJRuns  # Generate IntelliJ run configurations (after adding new event handlers)
```

### Publishing
```bash
./gradlew publish        # Publish to maven repository (configured in build.gradle)
```

## Code Style Guidelines

### General Principles
- Follow existing code conventions in the codebase
- Keep code consistent with NeoForge API patterns
- Use meaningful, descriptive names
- Write concise code - avoid unnecessary complexity

### Java Version
- Target **Java 21** (Mojang ships Java 21 to end users in 1.21.1)
- Use modern Java features where appropriate (records, switch expressions, etc.)

### Naming Conventions
- **Classes**: PascalCase (e.g., `EldritchFeast`, `Ingredient`)
- **Methods**: camelCase (e.g., `commonSetup`, `register`)
- **Fields/Variables**: camelCase (e.g., `LOG_DIRT_BLOCK`, `modEventBus`)
- **Constants**: SCREAMING_SNAKE_CASE (e.g., `MOD_ID`)
- **Packages**: lowercase with underscores avoided (e.g., `com.seeds.eldritchfeast.item`)

### Import Organization
Group imports in this order ( IntelliJ default order):
1. Java/Standard library imports
2. Minecraft imports (`net.minecraft.*`)
3. NeoForge imports (`net.neoforged.*`)
4. Third-party imports
5. Project imports (`com.seeds.eldritchfeast.*`)

Example:
```java
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.seeds.eldritchfeast.EldritchFeast;
```

### Formatting
- Use 4 spaces for indentation (no tabs)
- Max line length: 120 characters (soft guideline)
- Opening brace on same line as declaration
- One space after keywords (`if (`, `for (`, `while (`)
- No space between method name and parentheses

### Types and Generics
- Use diamond operator where type is obvious: `new Item.Properties()`
- Prefer interface types in method signatures: `List<String>` not `ArrayList<String>`
- Use `var` for local variables when type is obvious from right side

### NeoForge Patterns

#### Deferred Registers
Use `DeferredRegister` for all registries:
```java
public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
    DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
```

#### Registration
```java
public static final DeferredItem<Item> ITEM_NAME = ITEMS.register("item_name", 
    () -> new Item(new Item.Properties()));
```

#### Event Handling
```java
modEventBus.addListener(this::commonSetup);

@SubscribeEvent
public void onServerStarting(ServerStartingEvent event) {
    // Handle event
}
```

#### Mod Main Class
- Always annotate with `@Mod(MOD_ID)`
- Pass `IEventBus` and `ModContainer` to constructor
- Register config using `modContainer.registerConfig()`

### Error Handling
- Use NeoForge's logging: `EldritchFeast.LOGGER.info/debug/error/warn()`
- Never swallow exceptions silently
- Validate config values with custom validators (see `Config.java`)
- Use `ModConfigSpec.Builder` for type-safe configuration

### Logging
```java
// Recommended log levels
LOGGER.trace("Detailed debug info");
LOGGER.debug("Debug info");
LOGGER.info("General information");
LOGGER.warn("Warning message");
LOGGER.error("Error message");
```

### Resources and Assets
- Place assets in `src/main/resources/assets/<modid>/`
- Language files in `src/main/resources/assets/<modid>/lang/`
- Use data generators for recipes, tags, loot tables when possible

### Mixins
- Configure in `src/main/resources/<modid>.mixins.json`
- Keep mixins focused and well-documented

### Configuration
- Use `ModConfigSpec` for type-safe config (see `Config.java`)
- Config file: `run/config/<modid>-common.toml`
- Always provide comments for config options

## Project Structure

```
src/main/
├── java/com/seeds/eldritchfeast/
│   ├── EldritchFeast.java         # Main mod class
│   ├── EldritchFeastClient.java   # Client-only code
│   ├── Config.java                # Configuration
│   └── item/                      # Item classes
├── resources/
│   ├── assets/eldritch_feast/     # Textures, lang, models
│   └── eldritch_feast.mixins.json # Mixin config
└── templates/
    └── META-INF/neoforge.mods.toml # Mod metadata
```

## Common Tasks

### Adding a New Item
1. Create item class extending `Item` or use anonymous instantiation
2. Register with `DeferredRegister` in appropriate class
3. Add lang entry in `en_us.json`

### Adding a New Block
1. Create block class
2. Register block and block item
3. Add lang entry

### Adding Recipes/Tags
Use data generation or add JSON files in appropriate `src/main/resources/data/` directories.

## Dependencies

This is a NeoForge mod. Key dependencies are managed via Gradle:
- NeoForge (21.1.219)
- Minecraft (1.21.1)
- Parchment Mappings (2024.11.17)

## Additional Resources

- [NeoForge Documentation](https://docs.neoforged.net/)
- [NeoForge Discord](https://discord.neoforged.net/)
- [ParchmentMC](https://parchmentmc.org/)
