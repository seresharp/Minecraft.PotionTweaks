package com.serenam.potiontweaks.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.serenam.potiontweaks.PotionTweaksMod;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BetterBrewerScreen extends HandledScreen<BetterBrewerScreenHandler>
{
    private static final Identifier TEXTURE = new Identifier(PotionTweaksMod.MOD_ID, "textures/gui/container/better_brewer.png");
    private static final int[] BUBBLE_PROGRESS = new int[] { 29, 24, 20, 16, 11, 6, 0 };

    public BetterBrewerScreen(BetterBrewerScreenHandler handler, PlayerInventory inventory, Text title)
    {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // Draw background
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // Draw blaze fuel indicator
        int fuelBlaze = handler.getFuelBlaze();
        int fuelBlazeWidth = MathHelper.clamp((18 * fuelBlaze + 20 - 1) / 20, 0, 18);
        if (fuelBlazeWidth > 0)
        {
            drawTexture(matrices, x + 38, y + 23, 176, 29, fuelBlazeWidth, 4);
        }

        // Draw emerald fuel indicator
        int fuelEmerald = handler.getFuelEmerald();
        int fuelEmeraldWidth = MathHelper.clamp((18 * fuelEmerald + 20 - 1) / 20, 0, 18);
        if (fuelEmeraldWidth > 0)
        {
            drawTexture(matrices, x + 38, y + 41, 176, 33, fuelEmeraldWidth, 4);
        }

        // Draw arrow and bubbles
        int brewTime = handler.getBrewTime();
        if (brewTime > 0)
        {
            int brewTimeHeight = (int)(28f * (1f - (brewTime / 600f)));
            if (brewTimeHeight > 0)
            {
                drawTexture(matrices, x + 149, y + 36, 176, 0, 9, brewTimeHeight);
            }

            int bubbleHeight = BUBBLE_PROGRESS[brewTime / 2 % 7];
            if (bubbleHeight > 0)
            {
                drawTexture(matrices, x + 69, y + 65 - bubbleHeight, 185, 29 - bubbleHeight, 12, bubbleHeight);
            }
        }

        // Draw locked slots indicator
        if (handler.getSlotsLocked())
        {
            for (int i = 0; i < 5; i ++)
            {
                drawTexture(matrices, x + 71 + (18 * i), y + 17, 176, 37, 16, 16);
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init()
    {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
