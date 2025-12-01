package com.example.examplemod.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClientChatHandler {
    
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        IChatComponent message = event.message;
        if (message == null) {
            return;
        }
        
        String text = message.getUnformattedText();
        
        if (text != null && text.matches("<[^:>]+:[^>]+>(\\.withTag\\([^)]*\\))?") && !text.contains("[MetaItemHand]")) {
            copyToClipboard(text);
            
            if (Minecraft.getMinecraft().thePlayer != null) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new ChatComponentText("§a[MetaItemHand] §7Текст скопирован в буфер обмена!")
                );
            }
        }
    }
    
    private void copyToClipboard(String text) {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(text);
            clipboard.setContents(selection, null);
        } catch (Exception e) {
        }
    }
}

