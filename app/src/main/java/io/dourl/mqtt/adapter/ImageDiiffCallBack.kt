package io.dourl.mqtt.adapter

import androidx.recyclerview.widget.DiffUtil
import io.dourl.mqtt.Pics

open class ImageDiiffCallBack(
) : DiffUtil.ItemCallback<Pics>() {
    override fun areItemsTheSame(oldItem: Pics, newItem: Pics): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Pics, newItem: Pics): Boolean {
        return oldItem == newItem
    }
}