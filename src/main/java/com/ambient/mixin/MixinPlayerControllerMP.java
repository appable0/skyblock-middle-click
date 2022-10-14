package com.ambient.mixin;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {

    @Shadow
    private NetHandlerPlayClient netClientHandler;

    /**
     * @author appable
     * @reason fix mods that don't correctly handle middle clicks in windows
     */
    @Overwrite
    public ItemStack windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn) {
        int updatedButton = mode != 3 ? mouseButtonClicked : 2;
        int updatedMode = mouseButtonClicked != 2 ? mode : 3;
        short short1 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
        ItemStack itemstack = playerIn.openContainer.slotClick(slotId, updatedButton, updatedMode, playerIn);
        this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(windowId, slotId, updatedButton, updatedMode, itemstack, short1));
        return itemstack;
    }
}
