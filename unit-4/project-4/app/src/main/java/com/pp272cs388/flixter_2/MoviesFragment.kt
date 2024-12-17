package com.pp272cs388.flixter_2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pp272cs388.flixter_2.databinding.FragmentMoviesBinding
import okhttp3.Headers


private const val API_KEY = "3184d2c7bfb87d2da34ad6df28b46b79"


class MoviesFragment : Fragment(), OnListFragmentInteractionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMoviesBinding.inflate(inflater, container, false)


        recyclerView = binding.list // Access RecyclerView using ViewBinding
        progressBar = binding.progressBar // Access ProgressBar using ViewBinding

        recyclerView.layoutManager = GridLayoutManager(context, 2) // 2 columns for a grid layout
        updateAdapter(progressBar, recyclerView) // Update adapter to fetch and display movies

        return binding.root
    }

    private fun updateAdapter(progressBar: ProgressBar, recyclerView: RecyclerView) {
        val contentLoadingProgressBar = progressBar as? ContentLoadingProgressBar
        contentLoadingProgressBar?.show() // This will only be called if the cast is successful

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        client[
            "https://api.themoviedb.org/3/movie/popular", // Change this to popular
            params,
            object : JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JSON
                ) {
                    contentLoadingProgressBar?.hide() // This will only be called if the cast is successful

                    val resultsJSON = json.jsonObject.getJSONArray("results")
                    val gson = Gson()
                    val arrayMovieType = object : TypeToken<List<Movie>>() {}.type

                    val moviesRawJSON = resultsJSON.toString()
                    val models: List<Movie> = gson.fromJson(moviesRawJSON, arrayMovieType)

                    recyclerView.adapter = MoviesRecyclerViewAdapter(models, this@MoviesFragment)
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    contentLoadingProgressBar?.hide() // This will only be called if the cast is successful

                    t?.message?.let {
                        Log.e("MoviesFragmentError", errorResponse)
                    }
                }
            }]
    }


    override fun onItemClick(item: Movie) {
        val intent = Intent(context, MovieDetailActivity::class.java)
        intent.putExtra("movie_title", item.title)
        intent.putExtra("movie_overview", item.overview)
        intent.putExtra("movie_image", item.movie_poster)
        startActivity(intent)
    }


}