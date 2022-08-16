package com.serenam.potiontweaks.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.world.World;

public class SingularityPotionItem extends PotionItem
{
    public SingularityPotionItem()
    {
        super(new FabricItemSettings().group(ItemGroup.BREWING));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
    {
        ItemStack original = stack.copy();
        super.finishUsing(stack, world, user);
        stack.increment(original.getCount() - stack.getCount());

        return stack;
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return getTranslationKey();
    }
}
