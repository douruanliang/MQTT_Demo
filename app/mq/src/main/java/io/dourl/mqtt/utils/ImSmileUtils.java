/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dourl.mqtt.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;
/*
import com.being.fame.model.message.chat.TextBody;
import com.being.fame.model.message.emoji.DefaultEmojiconDatas;
import com.being.fame.model.message.emoji.Emojicon;
import com.being.fame.model.message.emoji.EmojiconGroupEntity;
import com.being.fame.ui.widget.chat.EmojiconInfoProvider;*/

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dourl.mqtt.model.message.chat.TextBody;
import io.dourl.mqtt.model.message.emoji.DefaultEmojiconDatas;
import io.dourl.mqtt.model.message.emoji.Emojicon;

public class ImSmileUtils {
    public static final String DELETE_KEY = "im_delete_delete_expression";

    public static final String Expression_1 = "[Smile]";
    public static final String Expression_2 = "[Grimace]";
    public static final String Expression_3 = "[Drool]";
    public static final String Expression_4 = "[Scowl]";
    public static final String Expression_5 = "[CoolGuy]";
    public static final String Expression_6 = "[Sob]";
    public static final String Expression_7 = "[Shy]";
    public static final String Expression_8 = "[Silent]";
    public static final String Expression_9 = "[Sleep]";
    public static final String Expression_10 = "[Cry]";
    public static final String Expression_11 = "[Awkward]";
    public static final String Expression_12 = "[Angry]";
    public static final String Expression_13 = "[Tongue]";
    public static final String Expression_14 = "[Grin]";
    public static final String Expression_15 = "[Surprise]";
    public static final String Expression_16 = "[Frown]";
    public static final String Expression_17 = "[Cool]";
    public static final String Expression_18 = "[Blush]";
    public static final String Expression_19 = "[Scream]";
    public static final String Expression_20 = "[Puke]";
    public static final String Expression_21 = "[Chuckle]";
    public static final String Expression_22 = "[Joyful]";
    public static final String Expression_23 = "[Slight]";
    public static final String Expression_24 = "[Smug]";
    public static final String Expression_25 = "[Hungry]";
    public static final String Expression_26 = "[Drowsy]";
    public static final String Expression_27 = "[Panic]";
    public static final String Expression_28 = "[Sweat]";
    public static final String Expression_29 = "[Laugh]";
    public static final String Expression_30 = "[Commando]";
    public static final String Expression_31 = "[Determined]";
    public static final String Expression_32 = "[Scold]";
    public static final String Expression_33 = "[Shocked]";
    public static final String Expression_34 = "[Shhh]";
    public static final String Expression_35 = "[Dizzy]";
    public static final String Expression_36 = "[Crazy]";
    public static final String Expression_37 = "[Toasted]";
    public static final String Expression_38 = "[Skull]";
    public static final String Expression_39 = "[Hammer]";
    public static final String Expression_40 = "[Bye]";
    public static final String Expression_41 = "[Speechless]";
    public static final String Expression_42 = "[NosePick]";
    public static final String Expression_43 = "[Clap]";
    public static final String Expression_44 = "[Embarrass]";
    public static final String Expression_45 = "[Trick]";
    public static final String Expression_46 = "[BahL]";
    public static final String Expression_47 = "[BahR]";
    public static final String Expression_48 = "[Yawn]";
    public static final String Expression_49 = "[PoohPooh]";
    public static final String Expression_50 = "[Shrunken]";
    public static final String Expression_51 = "[TearingUp]";
    public static final String Expression_52 = "[Sly]";
    public static final String Expression_53 = "[Kiss]";
    public static final String Expression_54 = "[Scare]";
    public static final String Expression_55 = "[Whimper]";
    public static final String Expression_56 = "[Cleaver]";
    public static final String Expression_57 = "[Watermelon]";
    public static final String Expression_58 = "[Bear]";
    public static final String Expression_59 = "[Basketball]";
    public static final String Expression_60 = "[PingPong]";
    public static final String Expression_61 = "[Coffee]";
    public static final String Expression_62 = "[Meal]";
    public static final String Expression_63 = "[Pig]";
    public static final String Expression_64 = "[Rose]";
    public static final String Expression_65 = "[Wilt]";
    public static final String Expression_66 = "[Lips]";
    public static final String Expression_67 = "[Heart]";
    public static final String Expression_68 = "[BorkenHeart]";
    public static final String Expression_69 = "[Cake]";
    public static final String Expression_70 = "[Lightning]";
    public static final String Expression_71 = "[Bomb]";
    public static final String Expression_72 = "[Knife]";
    public static final String Expression_73 = "[Football]";
    public static final String Expression_74 = "[Ladybug]";
    public static final String Expression_75 = "[Poop]";
    public static final String Expression_76 = "[Moon]";
    public static final String Expression_77 = "[Sun]";
    public static final String Expression_78 = "[Gift]";
    public static final String Expression_79 = "[Hug]";
    public static final String Expression_80 = "[ThumbsUp]";
    public static final String Expression_81 = "[ThumbsDown]";
    public static final String Expression_82 = "[Shake]";
    public static final String Expression_83 = "[Peace]";
    public static final String Expression_84 = "[Salute]";
    public static final String Expression_85 = "[Beckon]";
    public static final String Expression_86 = "[Fist]";
    public static final String Expression_87 = "[Bad]";
    public static final String Expression_88 = "[Like]";
    public static final String Expression_89 = "[NO]";
    public static final String Expression_90 = "[OK]";
    public static final String Expression_91 = "[Love]";
    public static final String Expression_92 = "[BlowKiss]";
    public static final String Expression_93 = "[Waddle]";
    public static final String Expression_94 = "[Tremble]";
    public static final String Expression_95 = "[Aaagh]";
    public static final String Expression_96 = "[Twirl]";
    public static final String Expression_97 = "[Kneel]";
    public static final String Expression_98 = "[Back]";
    public static final String Expression_99 = "[Skipping]";
    public static final String Expression_100 = "[GiveUp]";


    private static final Factory spannableFactory = Factory
            .getInstance();

    private static final Map<Pattern, Object> emoticons = new HashMap<Pattern, Object>();


    static {
        Emojicon[] emojicons = DefaultEmojiconDatas.getData();
        for (Emojicon emojicon : emojicons) {
            addPattern(emojicon.getEmojiText(), emojicon);
        }
        EmojiconInfoProvider emojiconInfoProvider = new EmojiconInfoProvider() {
            @Override
            public Emojicon getEmojiconInfo(String emojiconIdentityCode) {
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        };
        if (emojiconInfoProvider != null && emojiconInfoProvider.getTextEmojiconMapping() != null) {
            for (Entry<String, Object> entry : emojiconInfoProvider.getTextEmojiconMapping().entrySet()) {
                addPattern(entry.getKey(), entry);
            }
        }

    }

    /**
     * add text and icon to the map
     *
     * @param emojiText-- text of emoji
     * @param icon        -- resource id or local path
     */
    public static void addPattern(String emojiText, Object icon) {
        emoticons.put(Pattern.compile(Pattern.quote(emojiText)), icon);
    }


    /**
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start() && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Object value = entry.getValue();
                    if (value instanceof String && !((String) value).startsWith("http")) {
                        File file = new File((String) value);
                        if (!file.exists() || file.isDirectory()) {
                            return false;
                        }
                        spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable.setSpan(new ImageSpan(context, ((Emojicon) value).getIcon()),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        return hasChanges;
    }

    /**
     * get smiles entry
     *
     * @param text
     * @return
     */
    public static ArrayList<TextBody.TextEntity> getSmiles(CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        ArrayList<TextBody.TextEntity> ImgEntity = new ArrayList<>();
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                ImgEntity.add(new TextBody.TextEntity(TextBody.TextEntity.TextEntityType.emotion, ((Emojicon) entry.getValue()).getEmojiText(), matcher.start(), matcher.end()));
            }
        }
        Collections.sort(ImgEntity, new SortImoj());
        return ImgEntity;
    }

    public static class SortImoj implements Comparator<TextBody.TextEntity> {
        @Override
        public int compare(TextBody.TextEntity textEntity, TextBody.TextEntity t1) {
            return textEntity.start > t1.start ? 1 : -1;
        }
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static int getSmilesSize() {
        return emoticons.size();
    }


}
