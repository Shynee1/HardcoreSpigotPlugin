package com.shynee.hardcore;

import com.shynee.hardcore.Ban.BanCommand;
import com.shynee.hardcore.Ban.StartCommand;
import com.shynee.hardcore.Ban.TP.TPSpec;
import me.Shynee.Start.Files.DataManager;
import me.Shynee.Start.Files.DataManager2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;


public final class Hardcore extends JavaPlugin {

    public static Hardcore plugin;
    FileConfiguration config = this.getConfig();
    public DataManager data;
    public DataManager2 data2;

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        if (!getServer().hasWhitelist()){
            getServer().setWhitelist(true);
        }
        this.data = new DataManager(this);
        this.data2 = new DataManager2(this);
        getCommand("/start").setExecutor(new StartCommand());
        getCommand("teleport").setExecutor(new TPSpec());
        getCommand("reloadconfig").setExecutor(this);
        if (config.getBoolean("discord.whitelist")) {

            JDABuilder bot = JDABuilder.createDefault(data.getConfig().getString("token"));
            bot.setActivity(Activity.playing("Minecraft"));
            Bot botclass = new Bot();
            bot.addEventListeners(botclass);
            JDA jda;
            try {
                jda = bot.build();
                jda.awaitReady();
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        BanCommand b = new BanCommand();
        getServer().getPluginManager().registerEvents(b, this);
        getCommand("/stop").setExecutor(b);

    }

    @Override
    public void onDisable() {

    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) {

            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (player.isOp()){
            if (cmd.getName().equalsIgnoreCase("reloadconfig")) {
                this.reloadConfig();
                data.reloadConfig();
                player.sendMessage(ChatColor.GREEN + "You have successfully reloaded the config!");
                return true;

            }
        }
        return true;
    }

}
