package io.dourl.mqtt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import io.dourl.mqtt.Pics
import io.dourl.mqtt.R


const val NORMAL_VIEW = 0
const val ADS_VIEW = 1

class AdapterImage : ListAdapter<Pics, RecyclerView.ViewHolder>(ImageDiiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ADS_VIEW) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_ads, parent, false).let {
                // return AdsViewHolder(it)
            }
        }

        LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false).let {
            return ImageViewHolder(it)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == NORMAL_VIEW) {
            holder as ImageViewHolder
            getItem(position).let {
                holder.bind(it)
            }
        } else {
            //  holder as AdsViewHolder
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 4 == 0 && position != 0) {
            ADS_VIEW
        } else {
            NORMAL_VIEW
        }
    }
}


class ImageViewHolder(item: View) : RecyclerView.ViewHolder(item) {


    private val imageViewAvatar: ImageView = item.findViewById(R.id.imageViewAvatar)
    private val imageViewPhoto: ImageView = item.findViewById(R.id.imageViewPhoto)
    private val textViewName: TextView = item.findViewById(R.id.textViewName)

    fun bind(pics: Pics) {

        textViewName.text = pics.user

        imageViewAvatar.load(pics.userImageURL){
            transformations(CircleCropTransformation())
            placeholder(R.mipmap.ic_launcher_round)
            error(R.mipmap.ic_launcher_round)
        }
        imageViewPhoto.load(pics.largeImageURL){
            scale(Scale.FILL)
        }
       /* Glide.with(itemView.context)
            .load(pics.largeImageURL)
            .into(imageViewPhoto)

        Glide.with(itemView.context)
            .load(pics.userImageURL)
            .into(imageViewAvatar)*/
    }

}
