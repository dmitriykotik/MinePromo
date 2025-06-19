package org.dmitriykotik.minePromo;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PromoManager {
    private JavaPlugin plugin;
    private File file;
    private FileConfiguration cfg;
    private Map<String, Promo> promos;

    public PromoManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "promos.yml");
        if (!file.exists()) plugin.saveResource("promos.yml", false);
        this.cfg = YamlConfiguration.loadConfiguration(file);
        this.promos = new HashMap<>();
        load();
    }

    public void load() {
        Set<String> keys = cfg.getKeys(false);
        for (String key : keys) {
            long uses = cfg.getLong(key + ".uses", -1);
            long expires = cfg.getLong(key + ".expires", -1);
            String cmds = cfg.getString(key + ".commands", "");
            long usedCount = cfg.getLong(key + ".usedCount", 0);
            Promo p = new Promo(key, uses, expires, cmds, usedCount);
            promos.put(key, p);
        }
    }

    public void save() {
        for (String key : cfg.getKeys(false)) {
            cfg.set(key, null);
        }
        for (Promo p : promos.values()) {
            String code = p.getCode();
            cfg.set(code + ".uses", p.getUsesLeft());
            cfg.set(code + ".expires", p.getExpiresAt());
            cfg.set(code + ".commands", p.getCommands());
            cfg.set(code + ".usedCount", p.getUsedCount());
        }
        try {
            cfg.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save promos.yml: " + e.getMessage());
        }
    }

    public Promo get(String code) {
        return promos.get(code);
    }

    public String create(long uses, long durationMs, String commands) {
        String code;
        do {
            code = randomCode();
        } while (promos.containsKey(code));
        long expires = durationMs < 0 ? -1 : System.currentTimeMillis() + durationMs;
        Promo p = new Promo(code, uses, expires, commands, 0);
        promos.put(code, p);
        save();
        return code;
    }

    public boolean delete(String code) {
        if (promos.remove(code) != null) {
            save();
            return true;
        }
        return false;
    }

    public boolean edit(String code, long uses, long durationMs, String commands) {
        Promo p = promos.get(code);
        if (p == null) return false;
        long expires = durationMs < 0 ? -1 : System.currentTimeMillis() + durationMs;
        Promo updated = new Promo(code, uses, expires, commands, p.getUsedCount());
        promos.put(code, updated);
        save();
        return true;
    }

    private String randomCode() {
        String mask = plugin.getConfig().getString("properties.mask", "XXXXX-XXXXX-XXXXX");
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (char c : mask.toCharArray()) {
            if (c == 'X') {
                int idx = (int) (Math.random() * chars.length());
                sb.append(chars.charAt(idx));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}