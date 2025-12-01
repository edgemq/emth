package com.example.examplemod.command;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandMetaItemHand extends CommandBase {
    
    @Override
    public String getCommandName() {
        return "emth";
    }
    
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/emth";
    }
    
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            sender.addChatMessage(new ChatComponentText("§cЭта команда доступна только игрокам!"));
            return;
        }
        
        EntityPlayer player = (EntityPlayer) sender;
        ItemStack heldItem = player.getHeldItem();
        
        if (heldItem == null) {
            sender.addChatMessage(new ChatComponentText("§cВ вашей руке нет предмета!"));
            return;
        }
        
        String modId = getModId(heldItem);
        String itemId = getItemId(heldItem);
        
        String formatted = "<" + modId + ":" + itemId + ">";
        
        NBTTagCompound nbt = heldItem.getTagCompound();
        if (nbt != null) {
            String nbtString = formatNBT(nbt);
            formatted += ".withTag(" + nbtString + ")";
        }
        
        IChatComponent message = new ChatComponentText(formatted);
        ChatStyle style = new ChatStyle();
        style.setColor(EnumChatFormatting.GREEN);
        style.setChatClickEvent(new net.minecraft.event.ClickEvent(
            net.minecraft.event.ClickEvent.Action.SUGGEST_COMMAND, formatted
        ));
        style.setChatHoverEvent(new net.minecraft.event.HoverEvent(
            net.minecraft.event.HoverEvent.Action.SHOW_TEXT,
            new ChatComponentText("§7Кликните, чтобы скопировать")
        ));
        message.setChatStyle(style);
        
        sender.addChatMessage(message);
    }
    
    private String getModId(ItemStack stack) {
        if (stack.getItem() == null) {
            return "minecraft";
        }
        
        GameRegistry.UniqueIdentifier uniqueName = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        if (uniqueName != null) {
            return uniqueName.modId;
        }
        
        String unlocalizedName = stack.getItem().getUnlocalizedName();
        if (unlocalizedName != null && !unlocalizedName.isEmpty()) {
            String[] parts = unlocalizedName.split("\\.");
            if (parts.length >= 3) {
                return parts[1];
            }
        }
        
        return "minecraft";
    }
    
    private String getItemId(ItemStack stack) {
        if (stack.getItem() == null) {
            return "air";
        }
        
        // Используем GameRegistry для получения уникального идентификатора
        GameRegistry.UniqueIdentifier uniqueName = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        if (uniqueName != null) {
            String itemId = uniqueName.name;
            
            // Добавляем метаданные, если они не равны 0
            if (stack.getItemDamage() != 0) {
                return itemId + ":" + stack.getItemDamage();
            }
            
            return itemId;
        }
        
        // Fallback: пытаемся извлечь из unlocalizedName
        String unlocalizedName = stack.getItem().getUnlocalizedName();
        if (unlocalizedName != null && !unlocalizedName.isEmpty()) {
            String[] parts = unlocalizedName.split("\\.");
            if (parts.length > 0) {
                String itemName = parts[parts.length - 1];
                
                if (stack.getItemDamage() != 0) {
                    return itemName + ":" + stack.getItemDamage();
                }
                
                return itemName;
            }
        }
        
        return "unknown";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
    
    private String formatNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return "{}";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        if (!nbt.hasNoTags()) {
            boolean first = true;
            for (Object key : nbt.func_150296_c()) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                
                String keyStr = (String) key;
                NBTBase tag = nbt.getTag(keyStr);
                
                sb.append(keyStr).append(": ");
                sb.append(formatNBTValue(tag));
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
    
    private String formatNBTValue(NBTBase tag) {
        if (tag == null) {
            return "null";
        }
        
        byte tagType = tag.getId();
        
        switch (tagType) {
            case 1:
                net.minecraft.nbt.NBTTagByte byteTag = (net.minecraft.nbt.NBTTagByte) tag;
                return byteTag.func_150290_f() + " as byte";
            case 2:
                net.minecraft.nbt.NBTTagShort shortTag = (net.minecraft.nbt.NBTTagShort) tag;
                return shortTag.func_150289_e() + " as short";
            case 3:
                net.minecraft.nbt.NBTTagInt intTag = (net.minecraft.nbt.NBTTagInt) tag;
                return intTag.func_150287_d() + " as int";
            case 4:
                net.minecraft.nbt.NBTTagLong longTag = (net.minecraft.nbt.NBTTagLong) tag;
                return longTag.func_150291_c() + " as long";
            case 5:
                net.minecraft.nbt.NBTTagFloat floatTag = (net.minecraft.nbt.NBTTagFloat) tag;
                float floatVal = floatTag.func_150288_h();
                if (floatVal == (int) floatVal) {
                    return String.valueOf((int) floatVal) + ".0";
                }
                return String.valueOf(floatVal);
            case 6:
                net.minecraft.nbt.NBTTagDouble doubleTag = (net.minecraft.nbt.NBTTagDouble) tag;
                double doubleVal = doubleTag.func_150286_g();
                if (doubleVal == (long) doubleVal) {
                    return String.valueOf((long) doubleVal) + ".0";
                }
                return String.valueOf(doubleVal);
            case 7:
                return tag.toString();
            case 8:
                net.minecraft.nbt.NBTTagString stringTag = (net.minecraft.nbt.NBTTagString) tag;
                return "\"" + stringTag.func_150285_a_() + "\"";
            case 9:
                return formatNBTList((NBTTagList) tag);
            case 10:
                return formatNBT((NBTTagCompound) tag);
            case 11:
                return tag.toString();
            default:
                return tag.toString();
        }
    }
    
    private String formatNBTList(NBTTagList list) {
        if (list == null || list.tagCount() == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        int listType = list.func_150303_d();
        
        for (int i = 0; i < list.tagCount(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            
            NBTBase tag = null;
            
            if (listType == 10) {
                tag = list.getCompoundTagAt(i);
            } else {
                try {
                    java.lang.reflect.Method method = list.getClass().getMethod("func_150305_b", int.class);
                    tag = (NBTBase) method.invoke(list, i);
                } catch (Exception e) {
                    tag = list.getCompoundTagAt(i);
                }
            }
            
            if (tag != null) {
                sb.append(formatNBTValue(tag));
            }
        }
        
        sb.append("]");
        return sb.toString();
    }
}

