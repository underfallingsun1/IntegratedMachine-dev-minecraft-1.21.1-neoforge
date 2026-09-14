package com.afs.integratedMachine.network.server;

import com.afs.integratedMachine.item.utils.SpecialLeftClickItem;
import com.afs.integratedMachine.network.payload.ClientNotice;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class HandleClientNotice implements IPayloadHandler<ClientNotice> {
    public static HandleClientNotice INSTANCE = new HandleClientNotice();

    @Override
    public void handle(ClientNotice payload, IPayloadContext context) {
        if(payload == ClientNotice.LEFT_CLICK){
            Player player = context.player();
            Level level = player.level();
            ItemStack stackMain = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(stackMain.getItem() instanceof SpecialLeftClickItem si){
                si.onLeftClick(level, player, stackMain);
                return;
            }ItemStack stackOff = player.getItemInHand(InteractionHand.OFF_HAND);
            if(stackOff.getItem() instanceof SpecialLeftClickItem si){
                si.onLeftClick(level, player, stackOff);
                return;
            }
        }
    }
}
