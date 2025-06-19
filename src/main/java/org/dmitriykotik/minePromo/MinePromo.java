package org.dmitriykotik.minePromo;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class MinePromo extends JavaPlugin {
    private PromoManager manager;

    @Override
    public void onEnable() {
        Logger log = this.getLogger();
        log.info("-=- MinePromo -======-");
        log.info("* Project: MinePromo");
        log.info("* Author: dmitriykotik");
        log.info("-====================-");
        log.info("");
        log.info(" -+- Initialization... -+-");
        log.info(" - Saving the configuration file...");
        this.saveDefaultConfig();
        log.info(" - File saved!");
        log.info(" - Initialization of variables...");
        Initialize();
        log.info(" - Done!");
        log.info(" - Adding commands to the DoorLocker class...");
        this.getCommand("promo").setExecutor(new MinePromo_Commands(this, manager));
        this.getCommand("promo").setTabCompleter(new MinePromo_TabCompleter());
        log.info(" - Done!");
        log.info(" -+- DONE -+-");
        log.info(" -+- Plugin successfully enabled -+-");

    }

    @Override
    public void onDisable() {
        Logger log = this.getLogger();
        log.info("-=- MinePromo -======-");
        log.info("* Project: MinePromo");
        log.info("* Author: dmitriykotik");
        log.info("-====================-");
        log.info("");
        log.info(" -+- Plugin successfully disabled -+-");
    }

    public void Reload() {
        Logger log = this.getLogger();
        log.info("-=- MinePromo -======-");
        log.info("* Project: MinePromo");
        log.info("* Author: dmitriykotik");
        log.info("-====================-");
        log.info("");
        log.info(" -+- Plugin successfully disabled -+-");

        log.info("-=- MinePromo -======-");
        log.info("* Project: MinePromo");
        log.info("* Author: dmitriykotik");
        log.info("-====================-");
        log.info(" - Saving the configuration file...");
        this.saveDefaultConfig();
        log.info(" - File saved!");
        log.info(" - Initialization of variables...");
        Initialize();
        log.info(" - Done!");
        log.info(" - Adding commands to the DoorLocker class...");
        log.info(" - Skip...");
        log.info(" - Done!");
        log.info(" -+- DONE -+-");
        log.info(" -+- Plugin successfully enabled -+-");
    }

    private void Initialize() {
        manager = new PromoManager(this);
        reloadConfig();
        FileConfiguration cfg = getConfig();
        // Properties
        Vars.Prefix = cfg.getString("properties.prefix");

        // Messages
        Vars.ReloadStart = cfg.getString("messages.reload_start");
        Vars.ReloadDone = cfg.getString("messages.reload_done");

        Vars.CommandNotFound = cfg.getString("messages.command_not_found");
        Vars.AccessDenied = cfg.getString("messages.access_denied");

        Vars.CreatedPromo = cfg.getString("messages.created_promo");
        Vars.DeletedPromo = cfg.getString("messages.deleted_promo");
        Vars.PromoNotFound = cfg.getString("messages.promo_not_found");
        Vars.EditedPromo = cfg.getString("messages.edited_promo");
        Vars.InvalidPromo = cfg.getString("messages.invalid_promo");
        Vars.PromoExpired = cfg.getString("messages.promo_expired");
        Vars.NoUsesLeft = cfg.getString("messages.no_uses_left");
        Vars.PromoApplied = cfg.getString("messages.promo_applied");

        Vars.HelpCreate = cfg.getString("messages.help_create");
        Vars.HelpEdit = cfg.getString("messages.help_edit");
        Vars.HelpDelete = cfg.getString("messages.help_delete");
        Vars.HelpReload = cfg.getString("messages.help_reload");
        Vars.HelpPromo = cfg.getString("messages.help_promo");
    }

    
}
