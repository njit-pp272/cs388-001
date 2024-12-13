package com.pp272cs388.flixter_1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import okhttp3.Headers


private const val API_KEY = "3184d2c7bfb87d2da34ad6df28b46b79"


class MoviesFragment : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        client[
            "https://api.themoviedb.org/3/movie/now_playing",
            params,
            object : JsonHttpResponseHandler()
            {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    progressBar.hide()

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
                    progressBar.hide()

                    t?.message?.let {
                        Log.e("MoviesFragmentError", errorResponse)
                    }
                }
            }]

    }

    override fun onItemClick(item: Movie) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
    }

}