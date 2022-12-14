package com.serenam.potiontweaks.registry;

import com.serenam.potiontweaks.PotionTweaksMod;
import com.serenam.potiontweaks.blocks.BetterBrewerBlock;
import com.serenam.potiontweaks.blocks.EmptyBrewerBlock;
import com.serenam.potiontweaks.blocks.PotionCombinerBlock;
import com.serenam.potiontweaks.blocks.SingularityCrafterBlock;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks
{
    public static final Block BETTER_BREWER = new BetterBrewerBlock();
    public static final Block EMPTY_BREWER = new EmptyBrewerBlock();

    public static final Block POTION_COMBINER = new PotionCombinerBlock();

    public static final Block SINGULARITY_CRAFTER = new SingularityCrafterBlock();

    public static void registerBlocks()
    {
        Registry.register(Registry.BLOCK, new Identifier(PotionTweaksMod.MOD_ID, "better_brewer"), BETTER_BREWER);
        Registry.register(Registry.BLOCK, new Identifier(PotionTweaksMod.MOD_ID, "empty_brewer"), EMPTY_BREWER);

        Registry.register(Registry.BLOCK, new Identifier(PotionTweaksMod.MOD_ID, "potion_combiner"), POTION_COMBINER);

        Registry.register(Registry.BLOCK, new Identifier(PotionTweaksMod.MOD_ID, "singularity_crafter"), SINGULARITY_CRAFTER);
    }
}
