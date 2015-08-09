package net.kevyporter.reportmod;

import java.util.ArrayList;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * Created by Kevy on 18/06/2015.
 */
public class FormattedMessage {
    private static IChatComponent CHAT_PREFIX;
    public static String SEPARATION_MESSAGE = "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC";
    private IChatComponent chatComponent;
    private ArrayList<FormattedMessage> appendedMessages;

    public FormattedMessage(String text) {
        this.chatComponent = new ChatComponentText(text);
    }

    public FormattedMessage(String text, EnumChatFormatting color) {
        this(text);
        this.addFormatting(color);
    }

    public FormattedMessage addFormatting(EnumChatFormatting formatting) {
        ChatStyle style = this.chatComponent.getChatStyle();
        switch (formatting) {
            case ITALIC:
                style.setItalic(true);
                break;
            case BOLD:
                style.setBold(true);
                break;
            case UNDERLINE:
                style.setUnderlined(true);
                break;
            case OBFUSCATED:
                style.setObfuscated(true);
                break;
            case STRIKETHROUGH:
                style.setStrikethrough(true);
                break;
            default:
                style.setColor(formatting);
                break;
        }
        this.chatComponent.setChatStyle(style);

        return this;
    }

    public FormattedMessage appendMessage(FormattedMessage message) {
        if(this.appendedMessages == null) {
            this.appendedMessages = new ArrayList<FormattedMessage>();
        }
        this.appendedMessages.add(message);
        if(message.appendedMessages != null) {
            this.appendedMessages.addAll(message.appendedMessages);
        }

        return this;
    }

    public FormattedMessage makeClickable(ClickEvent.Action action, String execute, FormattedMessage description) {
        ChatStyle style = this.chatComponent.getChatStyle();

        style.setChatClickEvent(new ClickEvent(action, execute));
        style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, description.assembleMessage(false)));

        this.chatComponent.setChatStyle(style);

        return this;
    }

    public FormattedMessage makeLink(String url) {
        FormattedMessage description = new FormattedMessage("Click to visit ", EnumChatFormatting.GRAY);
        description.appendMessage(new FormattedMessage(url, EnumChatFormatting.AQUA).addFormatting(EnumChatFormatting.UNDERLINE));
        this.makeClickable(ClickEvent.Action.OPEN_URL, url, description);

        return this;
    }

    public FormattedMessage makeHover(FormattedMessage text) {
        ChatStyle style = this.chatComponent.getChatStyle();

        style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text.assembleMessage(false)));

        this.chatComponent.setChatStyle(style);

        return this;
    }

    public void send() {
        this.send(true);
    }

    public void send(boolean prefix) {
        try {
            FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(this.assembleMessage(prefix));
        } catch(Exception e) {
            System.out.println("[ERROR]: Failed to send Formatted message.");
            e.printStackTrace();
        }
    }

    protected IChatComponent assembleMessage(boolean prefix) {
        IChatComponent result;
        if(prefix) {
            result = CHAT_PREFIX.createCopy();
        } else if(this.appendedMessages == null) {
            return this.chatComponent;
        } else {
            result = new ChatComponentText("");
        }

        result.appendSibling(this.chatComponent);
        if(this.appendedMessages != null) {
            for (FormattedMessage m : this.appendedMessages) {
                result.appendSibling(m.chatComponent);
            }
        }

        return result;
    }

    static {
        IChatComponent name1 = new ChatComponentText("Report");
        IChatComponent name2 = new ChatComponentText("Mod");
        IChatComponent bracket1 = new ChatComponentText("[");
        IChatComponent bracket2 = new ChatComponentText("]: ");
        name1.getChatStyle().setColor(EnumChatFormatting.RED);
        name2.getChatStyle().setColor(EnumChatFormatting.GRAY);
        bracket1.getChatStyle().setColor(EnumChatFormatting.WHITE);
        bracket2.getChatStyle().setColor(EnumChatFormatting.WHITE);

        CHAT_PREFIX = bracket1.appendSibling(name1).appendSibling(name2).appendSibling(bracket2);
    }

    public static void printSeparationMessage(EnumChatFormatting color) {
        new FormattedMessage(SEPARATION_MESSAGE, color).addFormatting(EnumChatFormatting.BOLD).send(false);
    }
}
