package com.shynee.hardcore;

import me.Shynee.Start.Files.DataManager2;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import java.awt.*;
import java.util.List;

public class Bot extends ListenerAdapter implements Listener {
    FileConfiguration config = Hardcore.plugin.config;
    public DataManager2 data = Hardcore.plugin.data2;
    FileConfiguration config2 = Hardcore.plugin.data2.getConfig();

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        List<String> list = data.getConfig().getStringList("whitelist");

        if (config.getBoolean("discord.whitelist")) {
            if (!(config.getString("discord.token").equalsIgnoreCase("placeholder"))) {
                if (!(config.getString("discord.channelID").equalsIgnoreCase("placeholder"))) {
                    String[] message = e.getMessage().getContentRaw().split(" ");
                    if (e.getAuthor().isBot()) return;

                    if (message[0].equalsIgnoreCase("!whitelist")) {
                        if (message.length > 1) {
                            if (message[1].equalsIgnoreCase("help")) {
                                EmbedBuilder eb = new EmbedBuilder();
                                eb.setTitle("HardcoreBot Help");
                                eb.setDescription("**Commands:**\n \n`!whitelist add [Player name]` \n Adds the Minecraft Username to the whitelist \n \n`!whitelist remove [first]` \nRemoves the first player in the whitelist \n \n`!whitelist remove [last]` \nRemoves the last player in the whitelist \n \n`!whitelist remove [number]` \nRemoves the player in the number slot specified \n \n`!whitelist count` \nReturns the amount of players currently in the whitelist \n \n`!whitelist help` \nReturns helpful information like commands \n \n**Plugin Page:** \nhttps://www.spigotmc.org/resources/spigot-ultimate-hardcore-plugin.94050/");
                                eb.setColor(Color.RED);
                                e.getChannel().sendMessage(eb.build()).queue();
                            } else if (message[1].equalsIgnoreCase("count")) {
                                int count = list.size();
                                int c = count - 1;
                                e.getChannel().sendMessage("There are currently **" + c + " people** in the whitelist").queue();
                            } else if (e.getChannel().getId().equalsIgnoreCase(config.getString("discord.channelID"))) {
                                if (message[1].equalsIgnoreCase("add")) {
                                    if (!(message.length < 3)) {
                                        list.add(message[2]);
                                        data.getConfig().set("whitelist", list);
                                        data.saveConfig();
                                        e.getMessage().addReaction("\uD83D\uDC4D").queue();
                                    } else {
                                        e.getChannel().sendMessage("**Invalid command.** Please use \"!whitelist add [Player name (case sensitive)]\"").queue();
                                    }
                                } else if (message[1].equalsIgnoreCase("remove")) {
                                    if (e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                                        if (list.size() > 1) {
                                            if (!(message.length < 3)) {
                                                char[] chars = message[2].toCharArray();
                                                if (message[2].equalsIgnoreCase("last")) {
                                                    list.remove(list.size() - 1);
                                                    config2.set("whitelist", list);
                                                    data.saveConfig();
                                                    e.getChannel().sendMessage("**Player removed**").queue();
                                                } else if (message[2].equalsIgnoreCase("first")) {
                                                    list.remove(1);
                                                    config2.set("whitelist", list);
                                                    data.saveConfig();
                                                    e.getChannel().sendMessage("**Player removed**").queue();
                                                } else if (Character.isDigit(chars[0])) {
                                                    int a = Integer.parseInt(message[2]);
                                                    if (!(list.size() - 1 < a)) {
                                                        list.remove(a);
                                                        config2.set("whitelist", list);
                                                        data.saveConfig();
                                                        e.getChannel().sendMessage("**Player removed**").queue();
                                                    } else {
                                                        e.getChannel().sendMessage("Not enough players in the whitelist!").queue();
                                                    }
                                                } else {
                                                    e.getChannel().sendMessage("**Invalid command.** Please use \"!whitelist remove [first/last/number]\"").queue();
                                                }
                                            } else {
                                                e.getChannel().sendMessage("**Invalid command.** Please use \"!whitelist remove [first/last/number]\"").queue();
                                            }
                                        }
                                        else {
                                            e.getChannel().sendMessage("**No players in the whitelist**").queue();
                                        }
                                    } else {
                                        e.getChannel().sendMessage("You don't have the **Manage Messages** permission required to run this command.").queue();
                                    }
                                } else {
                                    e.getChannel().sendMessage("**Invalid command**. Please use \"!whitelist help\" for a list of commands.").queue();
                                }
                            } else {
                                e.getChannel().sendMessage("**You cannot send that command in this channel**").queue();
                            }
                        } else {
                            e.getChannel().sendMessage("**Invalid command**. Please use \"!whitelist help\" for a list of commands.").queue();
                        }
                    }
                }
                else {
                    e.getChannel().sendMessage("No channel specified, please check the config.yml file for more information.").queue();
                }
            }
            else {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Hardcore]: No token specified, please check the config.yml file for more information.");
            }
        }
    }
}