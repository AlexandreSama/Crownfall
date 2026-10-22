package fr.crownfall.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrownfallPlugin extends JavaPlugin {

    private ModBridge bridge;

    @Override
    public void onEnable() {
        try {
            bridge = new ModBridge(this);
            getLogger().info("Pont avec le mod Crownfall établi (API v" + bridge.apiVersion() + ").");
        } catch (NoClassDefFoundError e) {
            // Le mod est absent, ou Youer n'expose pas ses classes aux plugins.
            getLogger().severe("API Crownfall introuvable : le mod n'est pas chargé ou ses classes ne sont pas visibles depuis les plugins.");
        }
    }

    @Override
    public void onDisable() {
        if (bridge != null) {
            bridge.close();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (bridge == null) {
            sender.sendMessage(ChatColor.RED + "Pont mod ↔ plugin : inactif (voir la console).");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Pont mod ↔ plugin : actif, API v" + bridge.apiVersion()
                    + ", " + bridge.eventsReceived() + " événement(s) reçu(s).");
        }
        return true;
    }
}
