package com.pp272cs388.flixter_2

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movieTitle = intent.getStringExtra("movie_title")
        val movieOverview = intent.getStringExtra("movie_overview")
        val movieImage = intent.getStringExtra("movie_image")

        val titleTextView = findViewById<TextView>(R.id.movie_title)
        val overviewTextView = findViewById<TextView>(R.id.movie_overview)
        val movieImageView = findViewById<ImageView>(R.id.movie_poster)

        titleTextView.text = movieTitle
        overviewTextView.text = movieOverview
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500" + movieImage)
            .into(movieImageView)
    }
}
