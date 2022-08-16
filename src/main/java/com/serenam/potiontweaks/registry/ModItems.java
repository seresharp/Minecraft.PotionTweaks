package com.serenam.potiontweaks.registry;

import com.serenam.potiontweaks.PotionTweaksMod;
import com.serenam.potiontweaks.items.EmptyBrewerItem;
import com.serenam.potiontweaks.items.LingeringSingularityPotionItem;
import com.serenam.potiontweaks.items.SingularityPotionItem;
import com.serenam.potiontweaks.items.SplashSingularityPotionItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems
{
    // Items
    public static final SingularityPotionItem SINGULARITY = new SingularityPotionItem();
    public static final SplashSingularityPotionItem SPLASH_SINGULARITY = new SplashSingularityPotionItem();
    public static final LingeringSingularityPotionItem LINGERING_SINGULARITY = new LingeringSingularityPotionItem();

    // Block Items
    public static final BlockItem BETTER_BREWER = new BlockItem(ModBlocks.BETTER_BREWER, new FabricItemSettings()
            .group(ItemGroup.BREWING));

    public static final EmptyBrewerItem EMPTY_BREWER = new EmptyBrewerItem();

    public static final BlockItem POTION_COMBINER = new BlockItem(ModBlocks.POTION_COMBINER, new FabricItemSettings()
            .group(ItemGroup.BREWING));

    public static final BlockItem SINGULARITY_CRAFTER = new BlockItem(ModBlocks.SINGULARITY_CRAFTER, new FabricItemSettings()
            .group(ItemGroup.BREWING));

    public static void registerItems()
    {
        Registry.register(Registry.ITEM, new Identifier(PotionTweaksMod.MOD_ID, "singularity"), SINGULARITY);
        Registry.register(Registry.ITEM, new Identifier(PotionTweaksMod.MOD_ID, "splash_singularity"), SPLASH_SINGULARITY);
        Registry.register(Registry.ITEM, new Identifier(PotionTweaksMod.MOD_ID, "lingering_singularity"), LINGERING_SINGULARITY);

        Registry.register(Registry.ITEM, new Identifier(PotionTweaksMod.MOD_ID, "better_brewer"), BETTER_BREWER);
        Registry.register(Registry.ITEM, new Identifier(PotionTweaksMod.MOD_ID, "empty_brewer"), EMPTY_BREWER);

        Registry.register(Registry.ITEM, new Identifier(PotionTweaksMod.MOD_ID, "potion_combiner"), POTION_COMBINER);

        Registry.register(Registry.ITEM, new Identifier(PotionTweaksMod.MOD_ID, "singularity_crafter"), SINGULARITY_CRAFTER);
    }
}
