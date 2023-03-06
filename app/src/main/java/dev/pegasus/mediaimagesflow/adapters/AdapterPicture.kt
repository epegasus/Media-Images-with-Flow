package dev.pegasus.mediaimagesflow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.pegasus.mediaimagesflow.R
import dev.pegasus.mediaimagesflow.databinding.ItemPictureBinding
import dev.pegasus.mediaimagesflow.interfaces.OnPictureClickListener
import dev.pegasus.mediaimagesflow.models.Picture

/**
 * @Author: SOHAIB AHMED
 * @Date: 06,March,2023
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

class AdapterPicture(private val onPictureClickListener: OnPictureClickListener) : ListAdapter<Picture, AdapterPicture.CustomViewHolder>(diffUtilPictures) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemPictureBinding>(layoutInflater, R.layout.item_picture, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.apply {
            item = currentItem
            itemClick = onPictureClickListener
        }
    }

    inner class CustomViewHolder(val binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffUtilPictures = object : DiffUtil.ItemCallback<Picture>() {
            override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
                return oldItem.dateCreatedMillis == newItem.dateCreatedMillis
            }

            override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
                return oldItem == newItem
            }
        }
    }

}