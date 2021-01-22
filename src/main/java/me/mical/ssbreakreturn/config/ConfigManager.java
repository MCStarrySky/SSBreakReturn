package me.mical.ssbreakreturn.config;

import me.mical.ssbreakreturn.SSBreakReturn;
import org.bukkit.Material;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.config.PConfig;
import org.serverct.parrot.parrotx.data.autoload.Group;
import org.serverct.parrot.parrotx.data.autoload.Load;
import org.serverct.parrot.parrotx.utils.BasicUtil;
import org.serverct.parrot.parrotx.utils.EnumUtil;
import org.serverct.parrot.parrotx.utils.i18n.I18n;

import java.util.Objects;

@Group(path = "Options")
public class ConfigManager extends PConfig {
    private static ConfigManager INSTANCE;

    @Load(path = "Material")
    private String material;

    public Material getMaterial() {
        return EnumUtil.valueOf(Material.class, material.toUpperCase()) ;
    }

    public boolean setMaterial(String material) {
        Material mat = EnumUtil.valueOf(Material.class, material.toUpperCase());
        if (BasicUtil.isNull(plugin, mat, I18n.SET, "(配置文件 段: Settings 节点: Material)", "Material 未知")) {
            getConfig().set("Options.Material", material);
            save();
            return true;
        }
        return false;
    }

    public static ConfigManager getInstance() {
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new ConfigManager();
        }
        return INSTANCE;
    }

    public ConfigManager() {
        super(ParrotXAPI.getPlugin(SSBreakReturn.class), "config", "主配置文件");
    }
}
