package com.github.unchama.seasonalevents;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.unchama.seasonalevents.events.seizonsiki.Seizonsiki;
import com.github.unchama.seasonalevents.util.Config;

public class SeasonalEvents extends JavaPlugin {
	public static Config config;
	private Seizonsiki seizonsiki;

	@Override
	public void onEnable() {
		// コンフィグ読み込み
		config = new Config(this);
		config.loadConfig();
//		System.out.println("debug");

		// 各packageの初期化
		// 成ゾン式イベント
		seizonsiki = new Seizonsiki(this);

		// リスナー登録
		if (seizonsiki.getEnable()) {
			getServer().getPluginManager().registerEvents(seizonsiki.getListener(), this);
		}
	}

	@Override
	public void onDisable() {
	}
}
