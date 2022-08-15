package com.serenam.potiontweaks.gui;

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

public class BetterBrewerScreenHandler extends ScreenHandler
{
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public BetterBrewerScreenHandler(int syncId, PlayerInventory playerInventory)
    {
        this(syncId, playerInventory, new SimpleInventory(11), new ArrayPropertyDelegate(4));
    }

    public BetterBrewerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate)
    {
        super(ModScreenHandlers.BETTER_BREWER, syncId);
        checkSize(inventory, 11);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;

        inventory.onOpen(playerInventory.player);

        addProperties(propertyDelegate);

        // Our inventory
        // Blaze powder, emerald, and bottle slots
        addSlot(new FuelSlot(inventory, 0, 17, 17, Items.BLAZE_POWDER));
        addSlot(new FuelSlot(inventory, 1, 17, 35, Items.EMERALD));
        addSlot(new FuelSlot(inventory, 2, 17, 53, Items.GLASS_BOTTLE));

        // Ingredient slots
        for (int i = 0; i < 5; i++)
        {
            addSlot(new IngredientSlot(inventory, i + 3, 71 + (18 * i), 17));
        }

        // Output slots
        addSlot(new PotionSlot(inventory, 8, 84, 51));
        addSlot(new PotionSlot(inventory, 9, 107, 58));
        addSlot(new PotionSlot(inventory, 10, 130, 51));

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

    public int getBrewTime()
    {
        return propertyDelegate.get(0);
    }

    public int getFuelBlaze()
    {
        return propertyDelegate.get(1);
    }

    public int getFuelEmerald()
    {
        return propertyDelegate.get(2);
    }

    public boolean getSlotsLocked()
    {
        return propertyDelegate.get(3) != 0;
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

    // Classes copy/pasted from BrewingStandScreenHandler because they're private
    private static class PotionSlot extends Slot
    {
        public PotionSlot(Inventory inventory, int index, int x, int y)
        {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack)
        {
            return false;
        }

        @Override
        public int getMaxItemCount()
        {
            return 1;
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack)
        {
            Potion potion = PotionUtil.getPotion(stack);
            if (player instanceof ServerPlayerEntity)
            {
                Criteria.BREWED_POTION.trigger((ServerPlayerEntity)player, potion);
            }

            super.onTakeItem(player, stack);
        }
    }

    private static class IngredientSlot extends Slot
    {
        public IngredientSlot(Inventory inventory, int index, int x, int y)
        {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack)
        {
            return BrewingRecipeRegistry.isValidIngredient(stack);
        }

        @Override
        public int getMaxItemCount()
        {
            return 64;
        }
    }

    private static class FuelSlot extends Slot
    {
        private final Item FuelItem;

        public FuelSlot(Inventory inventory, int index, int x, int y, Item item)
        {
            super(inventory, index, x, y);
            FuelItem = item;
        }

        @Override
        public boolean canInsert(ItemStack stack)
        {
            return matches(stack);
        }

        @Override
        public int getMaxItemCount()
        {
            return FuelItem.getMaxCount();
        }

        public boolean matches(ItemStack stack)
        {
            return stack.isOf(FuelItem);
        }
    }
}
