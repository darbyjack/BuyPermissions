package me.glaremasters.buypermissions.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import me.glaremasters.buypermissions.BuyPermissions;
import me.rayzr522.jsonmessage.JSONMessage;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

import static co.aikar.commands.ACFBukkitUtil.color;

/**
 * Created by GlareMasters
 * Date: 10/4/2018
 * Time: 12:33 AM
 */
@CommandAlias("bp")
public class Commands extends BaseCommand {

    @Dependency private BuyPermissions bp;

    @Subcommand("list")
    @CommandPermission("bp.list")
    public void onList(Player player) {
        FileConfiguration c = bp.getConfig();
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

    @Subcommand("buy")
    @CommandPermission("bp.buy")
    public void onBuy(Player player, String permission) {
        FileConfiguration c = bp.getConfig();
        Economy e = bp.getEconomy();
        Permission p = bp.getPermissions();


        if (!c.getStringList("currently-selling").contains(permission)) {
            player.sendMessage(color(c.getString("messages.not-for-sale")));
            return;
        }

        String buyCommand = permission.toLowerCase();
        String pNode = c.getString("permissions.commands." + buyCommand + ".perm");
        double cost = c.getDouble("permissions.commands." + buyCommand + ".cost");
        if (player.hasPermission(pNode)) {
            player.sendMessage(color(c.getString("messages.already-have")));
            return;
        }

        double balance = e.getBalance(player);
        if (balance >= cost) {
            e.withdrawPlayer(player, cost);

            p.playerAdd(null, player, pNode);

            player.sendMessage(color(c.getString("messages.perm-added").replace("{command}", permission)));
            player.sendMessage(color(c.getString("messages.new-balance").replace("{balance}", String.valueOf(balance))));
        }
    }

    @Subcommand("reload")
    @CommandPermission("bp.reload")
    public void onReload(Player player) {
        bp.reloadConfig();
        player.sendMessage(color(bp.getConfig().getString("messages.reload-success")));
    }

    @HelpCommand
    @Syntax("")
    @CommandPermission("bp.help")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
