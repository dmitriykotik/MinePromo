package org.dmitriykotik.minePromo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class MinePromo_TabCompleter implements TabCompleter{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            if (sender.hasPermission("minepromo.admin")){
                list.add("create");
                list.add("delete");
                list.add("edit");
                list.add("reload");
                list.add("help");
                return list;
            }
            else
            { 
                list.add("<promocode>");
                return list;
            }
        }
        return Collections.emptyList();
    }
}
