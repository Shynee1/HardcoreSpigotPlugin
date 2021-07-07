package com.shynee.hardcore.Ban;

import com.shynee.hardcore.Hardcore;
import me.Shynee.Start.Files.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {
    public DataManager data = Hardcore.plugin.data;
    FileConfiguration config2 = Hardcore.plugin.data.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) {

            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (player.isOp()) {
            if (cmd.getName().equalsIgnoreCase("/start")) {
                player.sendMessage(ChatColor.GREEN + "You have successfully started the plugin");
                data.getConfig().set("hardcore", true);
                data.saveConfig();
                return true;

            }
            return true;
        }
        player.sendMessage(ChatColor.RED + "You must have permissions to use this command");
        return true;
    }
}
