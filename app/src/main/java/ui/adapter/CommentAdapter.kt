package ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.TaskItemBinding
import data.local.entity.Comment
import data.local.entity.Task
import ui.adapter.TaskAdapter.Companion

//class CommentAdapter (private  val callback: (Comment)-> Unit): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){
//    companion object {
//        val  itemCallback = object : DiffUtil.ItemCallback<Comment>() {
//            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//
//    private val mAsync: AsyncListDiffer<Comment> = AsyncListDiffer(this,itemCallback)
//
////    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
////
////    }
//
////    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
////
////    }
//
////    override fun getItemCount(): Int {
////
////    }
////
////    fun submit(data: List<Comment>) {
////
////    }
////
////    class CommentViewHolder(private val binding: ) : RecyclerView.ViewHolder(binding.root) {
////
////    }
////}