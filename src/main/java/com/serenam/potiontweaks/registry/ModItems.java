package com.serenam.potiontweaks.registry;

import com.serenam.potiontweaks.PotionTweaksMod;
import com.serenam.potiontweaks.items.EmptyBrewerItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems
{
    // Block Items
    public static final BlockItem BETTER_BREWER = new BlockItem(ModBlocks.BETTER_BREWER, new FabricItemSettings()
            .group(ItemGroup.MISC));

    public static final EmptyBrewerItem EMPTY_BREWER = new EmptyBrewerItem();

    public static void registerItems()
    {
        Registry.register(Registry.ITEM, new Identifier(PotionTweaksMod.MOD_ID, "better_brewer"), BETTER_BREWER);
        Registry.register(Registry.ITEM, new Identifier(PotionTweaksMod.MOD_ID, "empty_brewer"), EMPTY_BREWER);
    }
}
