package com.github.unchama.seasonalevents;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.unchama.seasonalevents.events.seizonsiki.Seizonsiki;
import com.github.unchama.seasonalevents.events.valentine.Valentine;
import com.github.unchama.seasonalevents.util.Config;

public class SeasonalEvents extends JavaPlugin {
	public static Config config;

	@Override
	public void onEnable() {
		// コンフィグ読み込み
		config = new Config(this);
		config.loadConfig();
		// 成ゾン式イベント
		new Seizonsiki(this);
		// バレンタインイベント
		new Valentine(this);

		// System.out.println("debug");
	}

	@Override
	public void onDisable() {
	}
}
