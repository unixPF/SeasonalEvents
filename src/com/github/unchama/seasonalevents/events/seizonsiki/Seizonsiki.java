package com.github.unchama.seasonalevents.events.seizonsiki;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.unchama.seasonalevents.SeasonalEvents;
import com.github.unchama.seichiassist.SeichiAssist;
import com.github.unchama.seichiassist.data.Mana;
import com.github.unchama.seichiassist.data.PlayerData;

public class Seizonsiki {
	private SeasonalEvents parent;
	private SeizonsikiListener listener;
	private boolean enable = false;
	public boolean isdrop = false;
	public static final String DROPDAY = "2017-01-16";
	public static final String DROPDAYDISP = "2017/01/15";
	public static final String FINISH = "2017-01-22";
	public static final String FINISHDISP = "2017/01/21";

	public Seizonsiki(SeasonalEvents parent) {
		try {
			this.parent = parent;
			// リスナー生成
			listener = new SeizonsikiListener(this);

			// イベント開催中か判定
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date finishdate = format.parse(FINISH);
			Date dropdate = format.parse(DROPDAY);
			if (now.before(finishdate)) {
				enable = true;
			}
			if (now.before(dropdate)) {
				isdrop = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public boolean getEnable() {
		return enable;
	}

	public Listener getListener() {
		return listener;
	}

	// プレイヤーにゾンビが倒されたとき発生
	public void killEvent(Player killer, Location loc) {
		if (isdrop) {
			double dp = parent.config.getDropPer();
			double rand = Math.random() * 100;
			if (rand < dp) {
				// 報酬をドロップ
				killer.getWorld().dropItemNaturally(loc, makePrize());
			}
		}
	}

	// チャラゾンビの肉判定
	public boolean checkPrize(ItemStack item) {
		// Lore取得
		if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return false;
		}
		List<String> lore = item.getItemMeta().getLore();
		List<String> plore = getPrizeLore();

		// 比較
		return lore.containsAll(plore);
	}

	// アイテム使用時の処理
	public void usePrize(Player player) {
		UUID uuid = player.getUniqueId();
		PlayerData pd = SeichiAssist.playermap.get(uuid);
		Mana mana = pd.activeskilldata.mana;

		double max = mana.calcMaxManaOnly(pd.level);
		mana.increaseMana(max * 0.1, player, pd.level);
		player.playSound(player.getLocation(),Sound.ENTITY_WITCH_DRINK, 1.0F, 1.2F);
	}

	private ItemStack makePrize() {
		ItemStack prize = new ItemStack(Material.GOLDEN_APPLE, 1);
		ItemMeta itemmeta = Bukkit.getItemFactory().getItemMeta(Material.GOLDEN_APPLE);
		itemmeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ゾんご");
		itemmeta.setLore(getPrizeLore());
		prize.setItemMeta(itemmeta);
		return prize;
	}

	private List<String> getPrizeLore() {
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.RESET + "" +  ChatColor.GRAY + "成ゾン式で暴走していたチャラゾンビから没収した。");
		lore.add(ChatColor.RESET + "" +  ChatColor.GRAY + "ゾンビたちが栽培しているりんご。");
		lore.add(ChatColor.RESET + "" +  ChatColor.GRAY + "良質な腐葉土で1つずつ大切に育てられた。");
		lore.add(ChatColor.RESET + "" +  ChatColor.GRAY + "栄養豊富で、食べるとマナが10%回復する。");
		lore.add(ChatColor.RESET + "" +  ChatColor.GRAY + "腐りやすいため賞味期限を超えると効果が無くなる。");
		lore.add("");
		lore.add(ChatColor.RESET + "" +  ChatColor.DARK_GREEN + "賞味期限：" + FINISHDISP);
		lore.add(ChatColor.RESET + "" +  ChatColor.RESET + "" +  ChatColor.AQUA + "マナ回復（10％）" + ChatColor.GRAY + " （期限内）");
		return lore;
	}
}
