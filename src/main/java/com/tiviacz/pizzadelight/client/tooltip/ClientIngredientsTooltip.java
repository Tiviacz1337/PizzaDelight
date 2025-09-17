package com.tiviacz.pizzadelight.client.tooltip;

import com.tiviacz.pizzadelight.util.Utils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class ClientIngredientsTooltip implements ClientTooltipComponent {
    private final ArrayList<ItemStack> doughs;
    private final ArrayList<ItemStack> sauces;
    private final ArrayList<ItemStack> items;

    private int ySize = 0;

    public ClientIngredientsTooltip(IngredientsTooltip pizzaTooltip) {
        this.items = pizzaTooltip.getIngredients();
        this.doughs = pizzaTooltip.getDoughs();
        this.sauces = pizzaTooltip.getSauces();
    }

    @Override
    public int getHeight() {
        int height = 0;
        if(!Utils.isShiftPressed()) return height;
        height += (int)(Math.ceil((float)items.size() / 9) * 18) + (int)(Math.ceil((float)doughs.size() / 9) * 18) + (int)(Math.ceil((float)sauces.size() / 9) * 18);
        return height;
    }

    @Override
    public int getWidth(Font pFont) {
        int width = 0;
        if(!Utils.isShiftPressed()) return width;
        width += Math.min(items.size(), 9) * 18 + Math.min(items.size(), 9) * 2;
        return width;
    }

    @Override
    public void renderText(Font pFont, int pMouseX, int pMouseY, Matrix4f pMatrix, MultiBufferSource.BufferSource pBufferSource) {
        //if(!Utils.isShiftPressed()) return;
        //pFont.drawInBatch(Component.translatable("information.pizzadelight.recommended_ingredients_tip"), (float)pMouseX, (float)pMouseY, -1, true, pMatrix, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        //this.ySize += 10;
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        if(!Utils.isShiftPressed()) return;
        int yOffset = 0;

        if(!doughs.isEmpty()) {
            int j = 0;

            for(int i = 0; i < doughs.size(); i++) {
                renderItem(doughs.get(i), pX + j * 2 + j * 18, pY + this.ySize, pFont, pGuiGraphics);

                if(j < 8) {
                    j++;
                } else {
                    j = 0;
                    this.ySize += 18;
                    yOffset += 18;
                }
            }
        }

        if(!sauces.isEmpty()) {
            this.ySize += 18;
            int j = 0;

            for(int i = 0; i < sauces.size(); i++) {
                renderItem(sauces.get(i), pX + j * 2 + j * 18, pY + this.ySize, pFont, pGuiGraphics);

                if(j < 8) {
                    j++;
                } else {
                    j = 0;
                    this.ySize += 18;
                    yOffset += 18;
                }
            }
        }

        if(!items.isEmpty()) {
            this.ySize += 18;
            int j = 0;

            for(int i = 0; i < items.size(); i++) {
                renderItem(items.get(i), pX + j * 2 + j * 18, pY + this.ySize, pFont, pGuiGraphics);

                if(j < 8) {
                    j++;
                } else {
                    j = 0;
                    this.ySize += 18;
                    yOffset += 18;
                }
            }
        }
    }

    private void renderItem(ItemStack stack, int pX, int pY, Font pFont, GuiGraphics guiGraphics) {
        guiGraphics.renderFakeItem(stack, pX, pY);
        guiGraphics.renderItemDecorations(pFont, stack, pX, pY);
    }
}