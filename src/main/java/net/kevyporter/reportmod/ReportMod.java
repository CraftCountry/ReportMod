package net.kevyporter.reportmod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.util.Locale;

/**
 * Created by Kevy on 22/06/2015.
 */
@Mod(modid = "reportmod", name = "Report Mod", version = "1.0")
public class ReportMod extends CommandBase {

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        ClientCommandHandler.instance.registerCommand(this);
    }

    /* Command */
    private boolean isOp(ICommandSender sender) {
        return (MinecraftServer.getServer().isSinglePlayer())
                || (!(sender instanceof EntityPlayerMP))
                || MinecraftServer.getServer().getConfigurationManager().func_152596_g(((EntityPlayerMP) sender).getGameProfile());
    }

    public String getCommandName() {
        return "report";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public boolean canSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "/report <player> <reason>";
    }

    public void processCommand(ICommandSender sender, String[] args) {
        try {
            if (Minecraft.getMinecraft().func_147104_D().serverIP.toLowerCase(Locale.ENGLISH).contains("hypixel.net")) {
                if (args.length >= 2) {
                    StringBuilder builder = new StringBuilder();
                    for(int i = 1; i < args.length; i++) {
                        builder.append(args[i]);
                        builder.append(" ");
                    }
                    String name = args[0];
                    String reason = builder.toString();
                    final String reportURL = "https://hypixel.net/forums/report-abuse-hackers.37/create-thread";
                    String report = getReport(name, reason);
                    StringSelection toCopy = new StringSelection(report);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(toCopy, null);
                    new FormattedMessage("Report has been copied to the clipboard. Opening website.", EnumChatFormatting.DARK_AQUA).send();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1500L);
                                URI uri = new URI(reportURL);
                                Desktop.getDesktop().browse(uri);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + getCommandUsage(sender)));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /* Other */
    public String getReport(String name, String reason) {
        String report = "[size=5][list][color=blue][b]In-Game Name: [/b][/color]" + name + "[/list]\n" +
                "[list][color=blue][b]Reason: [/b][/color]" + reason + "[/list]\n" +
                "[list][color=blue][b]Proof: [/b][/color]provide proof here\n" +
                "[/list][/size]";
        return report;
    }
}