package com.peter.thelandlord.extensions.recyclerviewextensions

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface OnClickListener{
    fun onItemClickListener(position: Int, view: View)
}

fun RecyclerView.addOnItemClickListener(onClickListener: OnClickListener){
    this.addOnChildAttachStateChangeListener(
        object: RecyclerView.OnChildAttachStateChangeListener{
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClickListener(holder.adapterPosition, view)
                }
            }

        }
    )
}