package com.serenam.potiontweaks.registry;

import com.serenam.potiontweaks.PotionTweaksMod;
import com.serenam.potiontweaks.blocks.BetterBrewerBlockEntity;
import com.serenam.potiontweaks.blocks.PotionCombinerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities
{
    public static final BlockEntityType<BetterBrewerBlockEntity> BETTER_BREWER
            = FabricBlockEntityTypeBuilder.create(BetterBrewerBlockEntity::new, ModBlocks.BETTER_BREWER).build(null);

    public static final BlockEntityType<PotionCombinerBlockEntity> POTION_COMBINER
            = FabricBlockEntityTypeBuilder.create(PotionCombinerBlockEntity::new, ModBlocks.POTION_COMBINER).build(null);

    public static void registerBlockEntities()
    {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(PotionTweaksMod.MOD_ID, "better_brewer"), BETTER_BREWER);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(PotionTweaksMod.MOD_ID, "potion_combiner"), POTION_COMBINER);
    }
}
