package org.dmitriykotik.minePromo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class MinePromo_Commands implements CommandExecutor {
    private MinePromo plugin;
    private PromoManager manager;

    public MinePromo_Commands(MinePromo plugin, PromoManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return sendHelp(sender);
        }
        switch (args[0].toLowerCase()) {
            case "create":
                if (!sender.hasPermission("minepromo.admin")) {
                    sender.sendMessage(Vars.Prefix + " " + Vars.AccessDenied);
                    return true;
                }
                if (args.length < 4) {
                    return sendHelp(sender);
                }
                long uses = args[1].equalsIgnoreCase("inf") ? -1 : Long.parseLong(args[1]);
                long duration = parseDuration(args[2]);
                String cmds = String.join(" ", java.util.Arrays.copyOfRange(args, 3, args.length));
                String code = manager.create(uses, duration, cmds);
                sender.sendMessage(Vars.Prefix + " " + Vars.CreatedPromo + " " + code);
                break;
            case "delete":
                if (!sender.hasPermission("minepromo.admin")) {
                    sender.sendMessage(Vars.Prefix + " " + Vars.AccessDenied);
                    return true;
                }
                if (args.length != 2) {
                    return sendHelp(sender);
                }
                boolean del = manager.delete(args[1]);
                sender.sendMessage(del ? Vars.Prefix + " " + Vars.DeletedPromo : Vars.Prefix + " " + Vars.PromoNotFound);
                break;
            case "edit":
                if (!sender.hasPermission("minepromo.admin")) {
                    sender.sendMessage(Vars.Prefix + " " + Vars.AccessDenied);
                    return true;
                }
                if (args.length < 5) {
                    return sendHelp(sender);
                }
                String editCode = args[1];
                long editUses = args[2].equalsIgnoreCase("inf") ? -1 : Long.parseLong(args[2]);
                long editDuration = parseDuration(args[3]);
                String editCmds = String.join(" ", java.util.Arrays.copyOfRange(args, 4, args.length));
                boolean ok = manager.edit(editCode, editUses, editDuration, editCmds);
                sender.sendMessage(ok ? Vars.Prefix + " " + Vars.EditedPromo : Vars.Prefix + " " + Vars.PromoNotFound);
                break;
            case "reload":
                if (!sender.hasPermission("minepromo.admin")) {
                    sender.sendMessage(Vars.Prefix + " " + Vars.AccessDenied);
                    return true;
                }
                sender.sendMessage(Vars.ReloadStart);
                plugin.Reload();
                sender.sendMessage(Vars.ReloadDone);
                break;
            case "help":
                return sendHelp(sender);
            default:
                if (!sender.hasPermission("minepromo.user")) {
                    sender.sendMessage(Vars.Prefix + " " + Vars.AccessDenied);
                    return true;
                }
                String codeArg = args[0];
                Promo p = manager.get(codeArg);
                if (p == null) {
                    sender.sendMessage(Vars.Prefix + " " + Vars.InvalidPromo);
                    return true;
                }
                if (!p.canUse()) {
                    sender.sendMessage(p.isExpired() ? Vars.Prefix + " " + Vars.PromoExpired : Vars.Prefix + " " + Vars.NoUsesLeft);
                    return true;
                }
                String[] execs = p.getCommands().split(";");
                for (String c : execs) {
                    String cmdline = c.replace("{user}", sender.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmdline);
                }
                p.applyUse();
                manager.save();
                sender.sendMessage(Vars.Prefix + " " + Vars.PromoApplied);
                break;
        }
        return true;
    }

    private long parseDuration(String s) {
        if (s.equalsIgnoreCase("inf")) return -1;
        long multiplier;
        if (s.endsWith("mo")) multiplier = 2_592_000_000L;
        else if (s.endsWith("d")) multiplier = 86_400_000;
        else if (s.endsWith("h")) multiplier = 3_600_000;
        else if (s.endsWith("m")) multiplier = 60_000;
        else multiplier = 1000;
        String num = s.replaceAll("[^0-9]", "");
        return Long.parseLong(num) * multiplier;
    }

    private boolean sendHelp(CommandSender sender) {
        sender.sendMessage(" §7-+- §aMinePromo §7-+-");
        if (sender.hasPermission("minepromo.admin")) {
            sender.sendMessage(" §7- §apromo create <max_uses> <valid_period> <commands> §f- §7" + Vars.HelpCreate);
            sender.sendMessage(" §7- §apromo edit <promocode> <max_uses> <valid_period> <commands> §f- §7" + Vars.HelpEdit);
            sender.sendMessage(" §7- §apromo delete <promocode> §f- §7" + Vars.HelpDelete);
            sender.sendMessage(" §7- §apromo reload §f- §7" + Vars.HelpReload);
            sender.sendMessage(" §7- §apromo <promocode> §f- §7" + Vars.HelpPromo);
        }
        else
        {
            sender.sendMessage(" §7- §apromo <promocode> §f- §7" + Vars.HelpPromo);
        }
        return true;
    }
}
