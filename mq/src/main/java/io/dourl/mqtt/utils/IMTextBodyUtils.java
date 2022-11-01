package io.dourl.mqtt.utils;

import android.text.Spannable;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dourl.mqtt.R;
import io.dourl.mqtt.base.MqttBaseApp;
import io.dourl.mqtt.model.BaseUser;
import io.dourl.mqtt.model.message.chat.BaseMsgBody;
import io.dourl.mqtt.model.message.chat.HintBody;
import io.dourl.mqtt.model.message.chat.TextBody;

/**
 * Created by zhangheng on 2018/3/20.
 */

public class IMTextBodyUtils {

    public static List<TextBody.TextEntity> createTextBody(String content) {
        return createTextBody(content, new ArrayList<>());
    }

    public static List<TextBody.TextEntity> createTextBody(String content, List<BaseUser> mAtUsers) {
        List<TextBody.TextEntity> textEntities = new ArrayList<>();
        try {
            List<TextBody.TextEntity> atEntitys = getAtEntitys(content, mAtUsers);
            List<TextBody.TextEntity> emojiconEntities = ImSmileUtils.getSmiles(content);
            emojiconEntities.addAll(atEntitys);
            Collections.sort(emojiconEntities, new ImSmileUtils.SortImoj());
            if (emojiconEntities.size() == 0) {
                TextBody.TextEntity textEntity = new TextBody.TextEntity(TextBody.TextEntity.TextEntityType.txt, content);
                textEntities.add(textEntity);
            } else {
                for (int i = 0; i < emojiconEntities.size(); i++) {
                    TextBody.TextEntity entity = emojiconEntities.get(i);
                    if (i == 0) {
                        if (entity.start != 0) {
                            textEntities.add(new TextBody.TextEntity(TextBody.TextEntity.TextEntityType.txt, content.substring(0, entity.start), 0, entity.start));
                        }
                        if (emojiconEntities.size() > i + 1) {
                            TextBody.TextEntity nextEntity = emojiconEntities.get(i + 1);
                            if (entity.end != nextEntity.start) {
                                textEntities.add(new TextBody.TextEntity(TextBody.TextEntity.TextEntityType.txt, content.substring(entity.end, nextEntity.start), entity.end, nextEntity.start));
                            }
                        } else {
                            if (entity.end != content.length()) {
                                textEntities.add(new TextBody.TextEntity(TextBody.TextEntity.TextEntityType.txt, content.substring(entity.end, content.length()), entity.end, content.length()));
                            }
                        }
                    } else {
                        if (emojiconEntities.size() > i + 1) {
                            TextBody.TextEntity nextEntity = emojiconEntities.get(i + 1);
                            if (entity.end != nextEntity.start) {
                                textEntities.add(new TextBody.TextEntity(TextBody.TextEntity.TextEntityType.txt, content.substring(entity.end, nextEntity.start), entity.end, nextEntity.start));
                            }
                        } else {
                            if (entity.end != content.length()) {
                                textEntities.add(new TextBody.TextEntity(TextBody.TextEntity.TextEntityType.txt, content.substring(entity.end, content.length()), entity.end, content.length()));
                            }
                        }

                    }
                }
            }
            textEntities.addAll(emojiconEntities);
            Collections.sort(textEntities, new ImSmileUtils.SortImoj());
            return textEntities;
        } catch (Exception e) {
           /* BugTagsUtils.sendException(new IllegalStateException(content, e));
            TextBody.TextEntity textEntity = new TextBody.TextEntity(TextBody.TextEntity.TextEntityType.txt, content);
            textEntities.add(textEntity);
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }*/
            return textEntities;
        }

    }


    private static List<TextBody.TextEntity> getAtEntitys(String content, List<BaseUser> mAtUsers) {

        Map<Pattern, Object> ats = new HashMap<Pattern, Object>();

        for (BaseUser user : mAtUsers) {
            if (!ats.containsValue(user))
                ats.put(Pattern.compile(Pattern.quote(MqttBaseApp.getApp().getString(R.string.at, user.getFullNameOrUserName()))), user);
        }

        Spannable spannable = Spannable.Factory.getInstance().newSpannable(content);
        List<TextBody.TextEntity> atEntities = new ArrayList<>();
        for (Map.Entry<Pattern, Object> entry : ats.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                atEntities.add(new TextBody.TextEntity(TextBody.TextEntity.TextEntityType.txt, MqttBaseApp.getApp().getString(R.string.at, ((BaseUser) entry.getValue()).getFullNameOrUserName()), matcher.start(), matcher.end()));
            }
        }


//        List<TextBody.TextEntity> atEntities = new ArrayList<>();
//        for (BaseUser user : mAtUsers) {
//            String find = AppConstant.getApp().getString(R.string.at, user.getFullNameOrUserName());
//            if (content.contains(find)) {
//                int frist = content.indexOf(find);
//                atEntities.add(new TextBody.TextEntity(TextBody.TextEntity.TextEntityType.txt, find, frist, frist + find.length()));
//            }
//        }
        return atEntities;
    }

    public static List<BaseMsgBody.UserEntity> getAtExtra(String content, List<BaseUser> mAtUsers) {

        Map<Pattern, Object> ats = new HashMap<Pattern, Object>();

       /* for (BaseUser user : mAtUsers) {
            if (!ats.containsValue(user))
                ats.put(Pattern.compile(Pattern.quote(AppConstant.getApp().getString(R.string.at, user.getFullNameOrUserName()))), user);
        }*/

        Spannable spannable = Spannable.Factory.getInstance().newSpannable(content);
        List<BaseMsgBody.UserEntity> extraEntities = new ArrayList<>();
        for (Map.Entry<Pattern, Object> entry : ats.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                extraEntities.add(new BaseMsgBody.UserEntity(((BaseUser) entry.getValue()).getUid(), ((BaseUser) entry.getValue()).getFullNameOrUserName()));
            }
        }

//        List<BaseMsgBody.UserEntity> extraEntities = new ArrayList<>();
//        for (BaseUser user : mAtUsers) {
//            String find = AppConstant.getApp().getString(R.string.at, user.getFullNameOrUserName());
//            if (content.contains(find)) {
//                extraEntities.add(new BaseMsgBody.UserEntity(user.getUid(), user.getFullNameOrUserName()));
//            }
//        }
        return extraEntities;
    }


    public static String processHintTxt(HintBody body) {
        String hintTxt = "";
        String fillTxt = "";
        /*try {
            if (body.getContent() != null)
                for (HintBody.ContentEntity item : body.getContent()) {
                    if (LocaleHelper.getLanguage().contains("zh")) {
                        if (TextUtils.isEmpty(fillTxt)) {
                            fillTxt = item.getC_zh();
                        } else {
                            fillTxt += "," + item.getC_zh();
                        }
                    } else {
                        if (TextUtils.isEmpty(fillTxt)) {
                            fillTxt = item.getC_en();
                        } else {
                            fillTxt += "," + item.getC_en();
                        }
                    }
                }
            switch (body.getType()) {
                case TYPE_GROUP_CREAT:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_creat, fillTxt);
                    break;
                case TYPE_GROUP_ADD_USER:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_add_user, fillTxt);
                    break;
                case TYPE_GROUP_INVITE_USER:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_invite_user, fillTxt);
                    break;
                case TYPE_GROUP_USER_ADD:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_user_add, fillTxt);
                    break;
                case TYPE_GROUP_DELETE_USER:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_delete_user, fillTxt);
                    break;
                case TYPE_GROUP_TRANS_OWNER:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_trans_owner, fillTxt);
                    break;
                case TYPE_GROUP_UPDATE_ICON:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_update_icon);
                    break;
                case TYPE_GROUP_UPDATE_NAME:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_update_name, fillTxt);
                    break;
                case TYPE_GROUP_UPDATE_COLOR:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_update_color);
                    break;
                case TYPE_GROUP_ADD_GAME:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_add_game, fillTxt);
                    break;
                case TYPE_GROUP_UPDATE_TYPE:
                    ClanDetailModel.ClanType type = ClanDetailModel.ClanType.valueof(Integer.parseInt(fillTxt));
                    int typeString = R.string.type_group_public;
                    switch (type) {
                        case approved:
                            typeString = R.string.type_group_protected;
                            break;
                        case privacy:
                            typeString = R.string.type_group_private;
                            break;
                        case open:
                            typeString = R.string.type_group_public;
                            break;
                    }
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_update_type, AppConstant.getApp().getResources().getString(typeString));
                    break;
                case TYPE_CHAT_REJECT:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_chat_reject);
                    break;
                case TYPE_GROUP_ADD_MANAGER:
                    hintTxt = AppConstant.getApp().getResources().getString(R.string.hint_group_add_manager, fillTxt);
                    break;
                default:
                    return fillTxt;
            }
        } catch (Exception e) {

        }*/
        return hintTxt;
    }
}
