package tn.sim5.agriconnect.utils


import tn.sim5.agriconnect.models.Blog
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface BlogApi {

    @GET("/api")
    fun getAllBlogs(): Call<List<Blog>>

    @GET("/images/{blog.image}")
    fun image(): Call<List<Blog>>

    @Multipart
    @POST("/blog")
    fun addBlog(
        @Part("titre") titre: RequestBody,
        @Part("description") description: RequestBody,
        @Part("lieu") lieu: RequestBody,
        @Part("date") date: RequestBody,
        @Part("prix") prix: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<Blog>

    @Multipart
    @PUT("/blog/{blogId}")
    fun updateBlogWithImage(
        @Path("blogId") blogId: String,
        @Part("titre") titre: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<Blog>
}
