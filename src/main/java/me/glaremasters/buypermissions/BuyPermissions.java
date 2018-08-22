package me.glaremasters.buypermissions;

import me.glaremasters.buypermissions.commands.CommandBuy;
import me.glaremasters.buypermissions.commands.CommandHelp;
import me.glaremasters.buypermissions.commands.CommandReload;
import me.glaremasters.buypermissions.commands.base.CommandHandler;
import me.glaremasters.buypermissions.commands.base.CommandList;
import me.glaremasters.buypermissions.updater.SpigotUpdater;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public final class BuyPermissions extends JavaPlugin {

    private static Economy econ = null;
    private static Permission perms = null;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        setupEconomy();
        setupPermissions();

        commandHandler = new CommandHandler();
        commandHandler.enable();

        getCommand("bp").setExecutor(commandHandler);

        Stream.of(
                new CommandBuy(this), new CommandHelp(this), new CommandList(this), new CommandReload(this)
        ).forEach(commandHandler::register);

        if (getConfig().getBoolean("updater.check")) {
            SpigotUpdater updater = new SpigotUpdater(this, 52557);
            try {
                if (updater.checkForUpdates()) {
                    getLogger()
                            .warning(
                                    "You appear to be running a version other than our latest stable release."
                                            + " You can download our newest version at: " + updater
                                            .getResourceURL());
                } else {
                    getLogger().warning("You are currently the latest version of the plugin! - "
                            + getDescription().getVersion());
                }
            } catch (Exception e) {
                getLogger().info("Could not check for updates! Stacktrace:");
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDisable() {
        commandHandler.disable();
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
}
