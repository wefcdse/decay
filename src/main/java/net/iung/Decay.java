package net.iung;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;

public class Decay implements ModInitializer {

    /* 声明和初始化我们的自定义方块实例。
       我们将方块材质（material）设置为 METAL（金属）。

       `strength` 会将方块的硬度和抗性设为同一个值。
       硬度决定了方块需要多久挖掘，抗性决定了方块抵御爆破伤害（如爆炸）的能力。
       石头的硬度为 1.5f，抗性为 6.0f，黑曜石的硬度为 50.0f，抗性为 1200.0f。

       可以在`Blocks`类中查找所有原版方块的统计。
    */
    public static final Block COPY_BLOCK = new RuinsBlock(FabricBlockSettings.of(Material.METAL).hardness(4.0f));

    private static ConfiguredFeature<?, ?> OVERWORLD_WOOL_ORE_CONFIGURED_FEATURE = new ConfiguredFeature
            (Feature.ORE, new OreFeatureConfig(
                    OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    COPY_BLOCK.getDefaultState(),
                    8)); // 矿脉大小

    public static PlacedFeature OVERWORLD_WOOL_ORE_PLACED_FEATURE = new PlacedFeature(
            RegistryEntry.of(OVERWORLD_WOOL_ORE_CONFIGURED_FEATURE),
            Arrays.asList(
                    CountPlacementModifier.of(5), // 每个区块的矿脉数量
                    SquarePlacementModifier.of(), // 水平传播
                    HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(24))
            )); // 高度



    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, new Identifier("decay", "copy_block"), COPY_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("decay", "copy_block"), new BlockItem(COPY_BLOCK, new FabricItemSettings()));

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new Identifier("decay", "overworld_wool_ore"), OVERWORLD_WOOL_ORE_CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("decay", "overworld_wool_ore"),
                OVERWORLD_WOOL_ORE_PLACED_FEATURE);
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                        new Identifier("decay", "overworld_wool_ore")));
    }
}