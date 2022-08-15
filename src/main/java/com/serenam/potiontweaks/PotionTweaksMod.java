package com.serenam.potiontweaks;

import com.serenam.potiontweaks.registry.ModBlockEntities;
import com.serenam.potiontweaks.registry.ModBlocks;
import com.serenam.potiontweaks.registry.ModItems;
import com.serenam.potiontweaks.registry.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.ActionResult;

public class PotionTweaksMod implements ModInitializer
{
    public static final String MOD_ID = "potiontweaks";

    @Override
    public void onInitialize()
    {
        ModScreenHandlers.registerScreenHandlers();
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModBlockEntities.registerBlockEntities();

        UseEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) ->
        {
            if (player.isSpectator())
            {
                return ActionResult.PASS;
            }

            if (entity.getType() == EntityType.VILLAGER && player.getStackInHand(hand).isOf(ModItems.EMPTY_BREWER))
            {
                player.getStackInHand(hand).getItem().useOnEntity(player.getStackInHand(hand), player, (VillagerEntity)entity, hand);
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        }));
    }
}