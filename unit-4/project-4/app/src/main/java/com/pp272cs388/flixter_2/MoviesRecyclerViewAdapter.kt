package com.pp272cs388.flixter_2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pp272cs388.flixter_2.R.id

class MoviesRecyclerViewAdapter(
    private val movies: List<Movie>,
    private val mListener: OnListFragmentInteractionListener?
)
    : RecyclerView.Adapter<MoviesRecyclerViewAdapter.MovieViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movies, parent, false)
        return MovieViewHolder(view)
    }

    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: Movie? = null
        val mMovieTitle: TextView = mView.findViewById<View>(id.movie_title) as TextView
        val mMovieDescription: TextView = mView.findViewById<View>(R.id.movie_overview) as TextView
        val mMovieImage: ImageView = mView.findViewById<View>(R.id.movie_poster) as ImageView
    }


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val baseImageUrl = "https://image.tmdb.org/t/p/w500"
        val movie = movies[position]

        val imageUrl = baseImageUrl + movie.movie_poster

        Glide.with(holder.mView)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .centerInside()
            .into(holder.mMovieImage)

        holder.mItem = movie
        holder.mMovieTitle.text = movie.title
        holder.mMovieDescription.text = movie.overview

        holder.mView.setOnClickListener {
            holder.mItem?.let { movie ->
                mListener?.onItemClick(movie)
            }
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}