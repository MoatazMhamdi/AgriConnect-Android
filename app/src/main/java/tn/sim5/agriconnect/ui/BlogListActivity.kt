package tn.sim5.agriconnect.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.sim5.agriconnect.BlogAdapter
import tn.sim5.agriconnect.models.Blog
import tn.sim5.agriconnect.utils.RetrofitInstance

import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.sim5.agriconnect.BlogDetailActivity
import tn.sim5.agriconnect.R
import tn.sim5.agriconnect.utils.RetrofitInstance.apiBlog

class BlogListActivity : AppCompatActivity() {

    private lateinit var blogAdapter: BlogAdapter
    private lateinit var recyclerViewBlogs: RecyclerView
    private lateinit var fabButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blog_list_row)

        recyclerViewBlogs = findViewById(R.id.recyclerViewBlogs)
        fabButton = findViewById(R.id.floatingActionButton)

        // Setup RecyclerView
        blogAdapter = BlogAdapter(blogApi = apiBlog)
        recyclerViewBlogs.apply {
            layoutManager = LinearLayoutManager(this@BlogListActivity)
            adapter = blogAdapter
        }

        // Make API call to get all blogs
        getAllBlogs()

        // Set click listener for FAB
        fabButton.setOnClickListener {
            // Navigate to add_blog.xml (BlogDetailActivity) when FAB is clicked
            val intent = Intent(this@BlogListActivity, BlogDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllBlogs() {
        RetrofitInstance.apiBlog.getAllBlogs().enqueue(object : Callback<List<Blog>> {
            override fun onResponse(call: Call<List<Blog>>, response: Response<List<Blog>>) {
                if (response.isSuccessful) {
                    response.body()?.let { blogs ->
                        Log.d("BlogListActivity", "Data received: $blogs")
                        blogAdapter.updateBlogList(blogs)
                    } ?: Log.d(
                        "BlogListActivity",
                        "Successful response, but the response body is null"
                    )
                } else {
                    Log.e(
                        "BlogListActivity",
                        "Unsuccessful response: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<List<Blog>>, t: Throwable) {
                Log.e("BlogListActivity", "Call failed: ${t.message}")
            }
        })
    }
}