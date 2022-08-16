package com.serenam.potiontweaks.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class SingularityPotionItem extends PotionItem
{
    public SingularityPotionItem()
    {
        super(new FabricItemSettings()
                .group(ItemGroup.BREWING)
                .maxDamageIfAbsent(1000));
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if (user.getStackInHand(hand).isDamaged())
        {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        return super.use(world, user, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        if (stack.isDamaged())
        {
            return UseAction.NONE;
        }

        return super.getUseAction(stack);
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return getTranslationKey();
    }
}
