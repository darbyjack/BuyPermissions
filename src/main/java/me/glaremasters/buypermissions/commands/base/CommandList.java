package me.glaremasters.buypermissions.commands.base;

import me.glaremasters.buypermissions.BuyPermissions;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


import java.util.List;

import static me.glaremasters.buypermissions.utils.ColorUtil.color;

/**
 * Created by GlareMasters
 * Date: 8/21/2018
 * Time: 7:26 PM
 */
public class CommandList extends CommandBase {
    private BuyPermissions buyPermissions;

    public CommandList(BuyPermissions buyPermissions) {
        super("list", "List all commands you can buy!", "buypermissions.buy", false, null, null, 0,
                0);
        this.buyPermissions = buyPermissions;
    }
    public void execute(Player player, String[] args) {
        FileConfiguration c = buyPermissions.getConfig();
        List<String> commands = c.getStringList("currently-selling");
        player.sendMessage(color(c.getString("messages.list-commands")));
        for (String command : commands) {
            JSONMessage.create(color(c.getString("command-styling.prefix") + c.getString("command-styling.command-color").replace("{command}", command)))
                    .tooltip(color(c.getString("tooltip-styling.name") + c
                                    .getString("permissions.commands." + command + ".name") + "\n"
                                    + c
                                    .getString("tooltip-styling.description") + c
                                    .getString("permissions.commands." + command + ".description")
                                    + "\n" +
                                    c.getString("tooltip-styling.cost") + c
                                    .getDouble("permissions.commands." + command + ".cost") + "\n"
                                    + "\n"
                                    + c.getString("messages.click-to-buy")))
                    .runCommand("/bp buy " + command)
                    .send(player);
        }
        player.sendMessage(color(c.getString("messages.list-commands-end")));
    }
}
