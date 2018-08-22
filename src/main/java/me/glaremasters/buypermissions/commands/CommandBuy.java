package me.glaremasters.buypermissions.commands;

import me.glaremasters.buypermissions.BuyPermissions;
import me.glaremasters.buypermissions.commands.base.CommandBase;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static me.glaremasters.buypermissions.utils.ColorUtil.color;

/**
 * Created by GlareMasters
 * Date: 8/21/2018
 * Time: 7:16 PM
 */
public class CommandBuy extends CommandBase {

    private BuyPermissions buyPermissions;

    public CommandBuy(BuyPermissions buyPermissions) {
        super("buy", "Buy commands!", "buypermissions.buy", false, null, null, 1, 1);
        this.buyPermissions = buyPermissions;
    }

    @Override
    public void execute(Player player, String[] args) {
        FileConfiguration c = buyPermissions.getConfig();
        Economy e = buyPermissions.getEconomy();
        Permission p = buyPermissions.getPermissions();

        if (!c.getStringList("currently-selling").contains(args[0])) {
            player.sendMessage(color(c.getString("messages.not-for-sale")));
            return;
        }
        String buyCommand = args[0].toLowerCase();
        String permissionNode = c.getString("permissions.commands." + buyCommand + ".perm");
        double cost = c.getDouble("permissions.commands." + buyCommand + ".cost");
        if (player.hasPermission(permissionNode)) {
            player.sendMessage(color(c.getString("messages.already-have")));
            return;
        }

        if (c.getStringList("currently-selling").contains(args[0])) {

            double balance = e.getBalance(player);

            if (balance >= cost) {
                e.withdrawPlayer(player, cost);

                p.playerAdd(null, player, permissionNode);

                player.sendMessage(color(c.getString("messages.perm-added").replace("{command}", args[0])));
                player.sendMessage(color(c.getString("messages.new-balance").replace("{balance}", String.valueOf(balance))));
            }
        }
    }

}
