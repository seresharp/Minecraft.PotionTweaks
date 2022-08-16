package com.serenam.potiontweaks.gui;

import com.serenam.potiontweaks.registry.ModItems;
import com.serenam.potiontweaks.registry.ModScreenHandlers;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

public class SingularityCrafterScreenHandler extends ScreenHandler
{
    private final Inventory inventory;

    public SingularityCrafterScreenHandler(int syncId, PlayerInventory playerInventory)
    {
        this(syncId, playerInventory, new SimpleInventory(2));
    }

    public SingularityCrafterScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory)
    {
        super(ModScreenHandlers.SINGULARITY_CRAFTER, syncId);
        checkSize(inventory, 2);
        this.inventory = inventory;

        inventory.onOpen(playerInventory.player);

        // Our inventory
        addSlot(new PotionSlot(inventory, 0, 17, 17));
        addSlot(new SingularitySlot(inventory, 1, 17, 35));

        // The player inventory
        for (int m = 0; m < 3; m++)
        {
            for (int l = 0; l < 9; l++)
            {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }

        // The player Hotbar
        for (int m = 0; m < 9; m++)
        {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player)
    {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index)
    {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasStack())
        {
            ItemStack origStack = slot.getStack();
            newStack = origStack.copy();
            if (index < inventory.size())
            {
                if (!insertItem(origStack, inventory.size(), slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!insertItem(origStack, 0, inventory.size(), false))
            {
                return ItemStack.EMPTY;
            }

            if (origStack.isEmpty())
            {
                slot.setStack(ItemStack.EMPTY);
            }
            else
            {
                slot.markDirty();
            }
        }

        return newStack;
    }

    private static class PotionSlot extends Slot
    {
        public PotionSlot(Inventory inventory, int index, int x, int y)
        {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack)
        {
            return matches(stack);
        }

        @Override
        public int getMaxItemCount()
        {
            return 1;
        }

        public boolean matches(ItemStack stack)
        {
            return stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION);
        }
    }

    private static class SingularitySlot extends Slot
    {
        public SingularitySlot(Inventory inventory, int index, int x, int y)
        {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack)
        {
            return matches(stack);
        }

        @Override
        public int getMaxItemCount()
        {
            return 1;
        }

        public boolean matches(ItemStack stack)
        {
            return stack.isOf(ModItems.SINGULARITY) || stack.isOf(ModItems.SPLASH_SINGULARITY) || stack.isOf(ModItems.LINGERING_SINGULARITY);
        }
    }
}
