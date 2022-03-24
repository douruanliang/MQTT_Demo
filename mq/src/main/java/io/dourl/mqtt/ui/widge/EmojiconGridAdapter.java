package io.dourl.mqtt.ui.widge;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import io.dourl.mqtt.R;
import io.dourl.mqtt.model.message.emoji.Emojicon;
import io.dourl.mqtt.utils.ImSmileUtils;

public class EmojiconGridAdapter extends ArrayAdapter<Emojicon>{

    private Emojicon.Type emojiconType;


    public EmojiconGridAdapter(Context context, int textViewResourceId, List<Emojicon> objects, Emojicon.Type emojiconType) {
        super(context, textViewResourceId, objects);
        this.emojiconType = emojiconType;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            if(emojiconType == Emojicon.Type.BIG_EXPRESSION){
                convertView = View.inflate(getContext(), R.layout.im_row_big_expression, null);
            }else{
                convertView = View.inflate(getContext(), R.layout.im_row_expression, null);
            }
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_name);
        Emojicon emojicon = getItem(position);
        if(textView != null && emojicon.getName() != null){
            textView.setText(emojicon.getName());
        }

        if(ImSmileUtils.DELETE_KEY.equals(emojicon.getEmojiText())){
            imageView.setImageResource(R.drawable.im_delete_expression);
        }else{
            if(emojicon.getIcon() != 0){
                imageView.setImageResource(emojicon.getIcon());
            }else if(emojicon.getIconPath() != null){
                //imageView.setImageUrl(emojicon.getIconPath());
            }
        }
        
        
        return convertView;
    }
    
}
