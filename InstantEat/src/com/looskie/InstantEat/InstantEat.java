package com.looskie.InstantEat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class InstantEat extends JavaPlugin {
    public HashMap<Material,Integer> foods = new HashMap<Material,Integer>();

    public void onEnable(){
        loadConfig();
        loadFoods();
        getServer().getPluginManager().registerEvents(new InstantEatEvents(this),this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(cmd.getName().equalsIgnoreCase("instantreload")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(player.hasPermission("instanteat.reload")) {
                    reloadConfig();
                    player.sendMessage(ChatColor.GREEN + "Reloaded the config!");
                } else {
                    player.sendMessage(ChatColor.RED + "Sorry, but you do not have permission for this!" + "red");
                }
            }
        }
        return true;
    }

    private void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
    public void loadFoods(){
        for(String s : getConfig().getConfigurationSection("Times").getKeys(false)){
            foods.put(Material.valueOf(s),getConfig().getInt("Times."+s));
        }
    }
}
