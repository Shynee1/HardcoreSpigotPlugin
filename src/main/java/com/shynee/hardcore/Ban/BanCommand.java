package com.shynee.hardcore.Ban;
import com.shynee.hardcore.Bot;
import com.shynee.hardcore.Hardcore;
import me.Shynee.Start.Files.DataManager;
import me.Shynee.Start.Files.DataManager2;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.List;


public class BanCommand implements CommandExecutor, Listener {

    public DataManager data = Hardcore.plugin.data;
    public DataManager2 data2 = Hardcore.plugin.data2;
    FileConfiguration config2 = Hardcore.plugin.data.getConfig();
    FileConfiguration config = Hardcore.plugin.getConfig();
    FileConfiguration config3 = Hardcore.plugin.data2.getConfig();

    Bot b = new Bot();

    @EventHandler
    @SuppressWarnings("deprecation")
    public void banAction(PlayerDeathEvent e) {

        List<String> list = data2.getConfig().getStringList("whitelist");

        Player player = (Player) e.getEntity();
        if (data.getConfig().getBoolean("hardcore") == true) {
            if (e.getEntity() instanceof Player) {

                Location loc = player.getLocation();
                World world = player.getWorld();

                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta sm = (SkullMeta) skull.getItemMeta();
                sm.setOwner(player.getName());
                skull.setItemMeta(sm);


                if (config.getBoolean("lightning")) {
                    player.getWorld().strikeLightningEffect(player.getLocation());
                }
                if (config.getBoolean("zombies.spawn")) {
                    Zombie z = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
                    z.getEquipment().setHelmet(skull);
                    if (config.getInt("zombies.strength") == 1) {
                        z.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(Integer.MAX_VALUE, 1));
                    } else if (config.getInt("zombies.strength") == 2) {
                        z.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(Integer.MAX_VALUE, 2));
                    }

                    z.setCanPickupItems(config.getBoolean("zombies.pickup items"));
                }
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "You have died, but you can still spectate other players.");
                if (config.getBoolean("teleport")) {
                        player.sendMessage(net.md_5.bungee.api.ChatColor.YELLOW + "You can also teleport to other players using: /teleport <player name>");
                }
                Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getDisplayName() + " has died. May he rest in peace");
                if (config.getBoolean("discord.whitelist")) {
                    if (config3.getStringList("whitelist").size() > 1) {
                        String s = config3.getStringList("whitelist").get(1);

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + s);
                        list.remove(1);

                        config3.set("whitelist", list);
                        data2.saveConfig();

                        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + s + " has been added to the server!");
                    }
                    else{
                        Bukkit.getServer().broadcastMessage(ChatColor.RED + "No players in the whitelist queue");
                    }
                  }
                }
            }
        }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) {

            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (player.isOp()) {
            if (cmd.getName().equalsIgnoreCase("/stop")) {
                player.sendMessage(ChatColor.GREEN + "You have successfully stopped the plugin");
                config2.set("hardcore", false);
                data.saveConfig();
                return true;

            }
            return true;
        }
        player.sendMessage(ChatColor.RED + "You must have permissions to use this command");
        return true;
    }
}


