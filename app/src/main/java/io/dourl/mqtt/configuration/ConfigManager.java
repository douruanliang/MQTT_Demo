package io.dourl.mqtt.configuration;


import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.dourl.mqtt.manager.GsonManager;

public class ConfigManager {

    public static final String STICKER = "sticker";
    private static ConfigManager sInstance;
    public static String colors = "[{\"name\":\"Purple\",\"color\":\"#ffa756fb\"},{\"name\":\"Blue\",\"color\":\"#ff4f77f5\"},{\"name\":\"Cyan\",\"color\":\"#ff36c1f0\"},{\"name\":\"Green\",\"color\":\"#ff81db2a\"},{\"name\":\"Yellow\",\"color\":\"#fffdbd4b\"},{\"name\":\"Orange\",\"color\":\"#fffd8943\"},{\"name\":\"Red\",\"color\":\"#fffc4964\"}]";


    public static ConfigManager getInstance() {
        if (sInstance == null) {
            synchronized (ConfigManager.class) {
                if (sInstance == null) {
                    sInstance = new ConfigManager();
                }
            }
        }
        return sInstance;
    }

    private ConfigManager() {
    }


    public void saveConfig(String result) {

    }


    public void requestOnlineConfig() {

    }

    /*public String getUrl(Context context, ServersResult.UrlType urlType) {
        if (urlType != null) {
            switch (urlType) {
                case about:
                    if (mServersResult != null) {
                        return mServersResult.data.about_url;
                    } else {
                        return context.getString(R.string.about_url);
                    }
                case privacy:
                    if (mServersResult != null) {
                        return mServersResult.data.privacy_url;
                    } else {
                        return context.getString(R.string.privacy_url);
                    }
                case support:
                    if (mServersResult != null) {
                        return mServersResult.data.support_url;
                    } else {
                        return context.getString(R.string.support_url);
                    }
                case game_guide:
                    if (mServersResult != null) {
                        return mServersResult.data.game_guide;
                    } else {
                        return "";
                    }
                case clan_wealth_rank:
                    if (mServersResult != null) {
                        return mServersResult.data.wealth_rank_url;
                    } else {
                        return "";
                    }
                case update_log_url:
                    if (mServersResult != null) {
                        return mServersResult.data.update_log_url;
                    } else {
                        return "";
                    }
                default:
                    return context.getString(R.string.support_url);
            }
        } else {
            return context.getString(R.string.support_url);
        }
         }*/


    public List<ColorModel> getColors() {
        return GsonManager.getGson().fromJson(colors, new TypeToken<ArrayList<ColorModel>>() {
        }.getType());
    }


}
