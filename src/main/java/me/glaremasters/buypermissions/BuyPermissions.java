package me.glaremasters.buypermissions;

import co.aikar.commands.BukkitCommandManager;
import me.glaremasters.buypermissions.commands.Commands;
import me.glaremasters.buypermissions.updater.SpigotUpdater;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import static co.aikar.commands.ACFBukkitUtil.color;

public final class BuyPermissions extends JavaPlugin {

    private static Economy econ = null;
    private static Permission perms = null;
    private BukkitCommandManager manager;
    private String logPrefix = "&b[&7BuyPermissions&b]&r ";

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        logo();
        saveDefaultConfig();
        info("Hooking into Vault...");
        setupEconomy();
        setupPermissions();
        int amount = getConfig().getStringList("currently-selling").size();
        info("Hooked into Economy and Permissions!");

        info("Loading Commands...");
        this.manager = new BukkitCommandManager(this);
        manager.enableUnstableAPI("help");
        manager.registerCommand(new Commands());
        info("Loaded " + String.valueOf(amount) + " commands to sell!");

        info("Checking for updates...");
        getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
            SpigotUpdater updater = new SpigotUpdater(BuyPermissions.this, 52557);
            @Override
            public void run() {
                updateCheck(updater);
            }
        });

        info("Ready to go! That only took " + (System.currentTimeMillis() - start) + "ms");

    }


    public Economy getEconomy() {
        return econ;
    }

    public Permission getPermissions() {
        return perms;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager()
                .getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager()
                .getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private void updateCheck(SpigotUpdater updater) {
        try {
            if (updater.checkForUpdates()) {
                info("You appear to be running a version other than our latest stable release." + " You can download our newest version at: " + updater.getResourceURL());
            }
        } catch (Exception ex) {
            info("Could not check for updates! Stacktrace:");
            ex.printStackTrace();
        }
    }

    /**
     * Useful tool for colorful texts to console
     * @param msg the msg you want to log
     */
    public void info(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(color(logPrefix + msg));
    }

    private void logo() {
        info("");
        info("  ____              ____                     _         _                   ____  ");
        info(" | __ ) _   _ _   _|  _ \\ ___ _ __ _ __ ___ (_)___ ___(_) ___  _ __  ___  |___ \\ ");
        info(" |  _ \\| | | | | | | |_) / _ \\ '__| '_ ` _ \\| / __/ __| |/ _ \\| '_ \\/ __|   __) |");
        info(" | |_) | |_| | |_| |  __/  __/ |  | | | | | | \\__ \\__ \\ | (_) | | | \\__ \\  / __/ ");
        info(" |____/ \\__,_|\\__, |_|   \\___|_|  |_| |_| |_|_|___/___/_|\\___/|_| |_|___/ |_____|");
        info("              |___/                                                              ");
        info("");
    }
}
