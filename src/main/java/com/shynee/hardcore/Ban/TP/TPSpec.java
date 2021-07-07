package com.shynee.hardcore.Ban.TP;


import com.shynee.hardcore.Hardcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TPSpec implements CommandExecutor{

    FileConfiguration config = Hardcore.plugin.getConfig();


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (config.getBoolean("teleport") == true) {
            if (sender instanceof Player) {

                if (player.getGameMode() == GameMode.SPECTATOR) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "You need to enter some arguments");
                        player.sendMessage(ChatColor.YELLOW + "To teleport to other players use: /teleport <player name>");
                    } else if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);

                        try {
                            player.teleport(target.getLocation());
                        } catch (NullPointerException e) {
                            player.sendMessage(ChatColor.RED + "Player does not exist or is not online right now");
                        }

                    } else if (args.length == 2) {
                        player.sendMessage(ChatColor.RED + "Too many arguments");
                        player.sendMessage(ChatColor.YELLOW + "To teleport to other players use: /teleport <player name>");

                    }
                } else {
                    player.sendMessage(ChatColor.RED + "That command only works for spectators");
                    return true;
                }
            }
            return true;
        }
        else {
            player.sendMessage(ChatColor.RED + "Command not allowed on this sever");
        }
        return true;
    }
}
