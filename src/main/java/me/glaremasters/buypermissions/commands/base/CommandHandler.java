package me.glaremasters.buypermissions.commands.base;

import me.glaremasters.buypermissions.utils.IHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by GlareMasters
 * Date: 8/21/2018
 * Time: 7:17 PM
 */
public class CommandHandler implements CommandExecutor, TabCompleter, IHandler {

    private List<CommandBase> commands;

    @Override
    public void enable() {
        commands = new ArrayList<>();
    }

    @Override
    public void disable() {
        commands.clear();
        commands = null;
    }

    public void register(CommandBase command) {
        commands.add(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (!cmd.getName().equalsIgnoreCase("bp")) {
            return true;
        }

        if (args.length == 0 || args[0].isEmpty()) {
            getCommand("help").execute(sender, args);
            return true;
        }

        for (CommandBase command : commands) {
            if (!command.getName().equalsIgnoreCase(args[0]) && !command.getAliases()
                    .contains(args[0].toLowerCase())) {
                continue;
            }

            if (!command.allowConsole() && !(sender instanceof Player)) {
                sender.sendMessage("Console can't use this!");
                return true;
            }

            if (!sender.hasPermission(command.getPermission())) {
                sender.sendMessage(ChatColor.RED + "You don't have permission!");
                return true;
            }

            args = Arrays.copyOfRange(args, 1, args.length);

            if ((command.getMinimumArguments() != -1 && command.getMinimumArguments() > args.length)
                    || (command.getMaximumArguments() != -1
                    && command.getMaximumArguments() < args.length)) {
                return true;
            }

            if (command.allowConsole()) {
                command.execute(sender, args);
                return true;
            } else {
                command.execute((Player) sender, args);
                return true;
            }
        }

        return true;
    }

    private CommandBase getCommand(String name) {
        return commands.stream().filter(
                command -> command.getName() != null && command.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public List<CommandBase> getCommands() {
        return commands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bp")) {
            if (args.length == 1) {
                List<String> commandNames = new ArrayList<>();

                if (!args[0].equals("")) {
                    for (String commandName : commands.stream().map(CommandBase::getName)
                            .collect(Collectors.toList())) {
                        if (!commandName.startsWith(args[0].toLowerCase())) {
                            continue;
                        }

                        commandNames.add(commandName);
                    }
                } else {
                    commandNames =
                            commands.stream().map(CommandBase::getName)
                                    .collect(Collectors.toList());
                }

                Collections.sort(commandNames);
                return commandNames;
            }
        }

        return null;
    }

}
