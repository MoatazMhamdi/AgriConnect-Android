package tn.sim5.agriconnect

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.sim5.agriconnect.models.Blog
import tn.sim5.agriconnect.utils.BlogApi
import com.squareup.picasso.Picasso

class BlogAdapter(private val blogApi: BlogApi) : RecyclerView.Adapter<BlogAdapter.ViewHolder>() {

    private var blogList: List<Blog> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.blog_list_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(blogList[position])
    }

    override fun getItemCount(): Int = blogList.size

    fun updateBlogList(newBlogList: List<Blog>) {
        blogList = newBlogList
        notifyDataSetChanged()
    }

    // Add this function to provide access to blogApi
    fun getAllBlogs(): BlogApi {
        return blogApi
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(blog: Blog) {
            val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
            val imageViewPublished: ImageView = itemView.findViewById(R.id.imageViewPublished)
            val textViewLocation: TextView = itemView.findViewById(R.id.textViewLocation)
            val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
            val textViewPrix: TextView = itemView.findViewById(R.id.textViewPrix)
            val textViewImageDescription: TextView =
                itemView.findViewById(R.id.textViewImageDescription)
            val buttonsLayout: LinearLayout = itemView.findViewById(R.id.buttonsLayout)
            val button1: Button = itemView.findViewById(R.id.button1)
            val button2: Button = itemView.findViewById(R.id.button2)
            val button3: Button = itemView.findViewById(R.id.button3)
            val recyclerViewBlogs: RecyclerView = itemView.findViewById(R.id.recyclerViewBlogs)


            // Initialize your recyclerViewBlogs here, not in bindView
            val layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewBlogs.layoutManager = layoutManager

            textViewTitle.text = blog.titre
            textViewImageDescription.text = blog.description
            textViewLocation.text = blog.lieu
            textViewDate.text = blog.date
            textViewPrix.text = blog.prix.toString()
            button1.text = "Button 1"
            button2.text = "Button 2"
            button3.text = "Button 3"

            val imageUrl =
                if (blog.image != null) "http://10.0.2.2:9090/images/${blog.image}" else null

            if (imageUrl != null) {
                Picasso.get()
                    .load(imageUrl)
                    .into(imageViewPublished, object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            // Image loaded successfully
                        }

                        override fun onError(e: Exception?) {
                            Log.e("Picasso", "Error loading image: $e")
                            Log.d("Picasso", "Image URL: $imageUrl")
                        }
                    })
            } else {
                // Handle the case where blog.image is null
                imageViewPublished.visibility = View.GONE // or set a placeholder image
            }



            itemView.setOnClickListener {
                Log.d("BlogAdapter", "Selected item: ID = ${blog._id}")
                // Handle item click as needed, e.g., start a new activity
            }
        }
    }}

