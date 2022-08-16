package com.serenam.potiontweaks.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SplashSingularityPotionItem extends SplashPotionItem
{
    public SplashSingularityPotionItem()
    {
        super(new FabricItemSettings()
                .group(ItemGroup.BREWING)
                .maxDamageIfAbsent(1000));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        ItemStack stack = user.getStackInHand(hand).copy();
        if (stack.isDamaged())
        {
            return TypedActionResult.pass(stack);
        }

        super.use(world, user, hand);
        user.getStackInHand(hand).increment(stack.getCount() - user.getStackInHand(hand).getCount());

        return TypedActionResult.success(stack, world.isClient);
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return getTranslationKey();
    }
}
