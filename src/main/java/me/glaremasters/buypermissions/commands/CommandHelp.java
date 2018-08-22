package me.glaremasters.buypermissions.commands;

import me.glaremasters.buypermissions.BuyPermissions;
import me.glaremasters.buypermissions.commands.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import static me.glaremasters.buypermissions.utils.ColorUtil.color;

/**
 * Created by GlareMasters
 * Date: 8/21/2018
 * Time: 7:39 PM
 */
public class CommandHelp extends CommandBase {
    private BuyPermissions buyPermissions;

    public CommandHelp(BuyPermissions buyPermissions) {
        super("help", "List all commands", "buypermissions.help", false, null, null, 0, 0);
        this.buyPermissions = buyPermissions;
    }
    public void execute(CommandSender sender, String[] args) {
        FileConfiguration config = buyPermissions.getConfig();

        sender.sendMessage(color(config.getString("messages.buy")));
        sender.sendMessage(color(config.getString("messages.list")));
        sender.sendMessage(color(config.getString("messages.reload")));
    }
}
