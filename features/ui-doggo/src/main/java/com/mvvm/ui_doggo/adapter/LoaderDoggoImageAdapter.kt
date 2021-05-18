package com.mvvm.ui_doggo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.mvvm.data.model.DoggoImageModel
import com.mvvm.data.responses.UsersResponse
import com.mvvm.ui_doggo.R

class LoaderDoggoImageAdapter :
    PagingDataAdapter<DoggoImageModel, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? DoggoImageViewHolder)?.bind(item = getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DoggoImageViewHolder.getInstance(parent)
    }

    /**
     * view holder class for doggo item
     */
    class DoggoImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            //get instance of the DoggoImageViewHolder
            fun getInstance(parent: ViewGroup): DoggoImageViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.list_item, parent, false)
                return DoggoImageViewHolder(view)
            }
        }

        var ivDoggoMain: ImageView = view.findViewById(R.id.iv_item)

        fun bind(item: DoggoImageModel?) {
            //loads image from network using coil extension function
            ivDoggoMain.load(item?.url)
        }

    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<DoggoImageModel>() {
            override fun areItemsTheSame(oldItem: DoggoImageModel, newItem: DoggoImageModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DoggoImageModel, newItem: DoggoImageModel) =
                oldItem.id == newItem.id
        }
    }

}