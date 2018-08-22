package me.glaremasters.buypermissions.commands;

import me.glaremasters.buypermissions.BuyPermissions;
import me.glaremasters.buypermissions.commands.base.CommandBase;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static me.glaremasters.buypermissions.utils.ColorUtil.color;

/**
 * Created by GlareMasters
 * Date: 8/21/2018
 * Time: 7:40 PM
 */
public class CommandReload extends CommandBase {
    private BuyPermissions buyPermissions;

    public CommandReload(BuyPermissions buyPermissions) {
        super("reload", "Reload the configuration", "buypermissions.reload", false, null, null, 0,
                0);
        this.buyPermissions = buyPermissions;
    }

    public void execute(Player player, String[] args) {
        buyPermissions.reloadConfig();

        FileConfiguration config = buyPermissions.getConfig();


        player.sendMessage(color(config.getString("messages.reload-success")));

    }
}