package com.jonathan.themoviedb.ui.top_rated_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jonathan.themoviedb.data.api.Interface
import com.jonathan.themoviedb.data.api.POST_PER_PAGE
import com.jonathan.themoviedb.data.repository.MovieDataSource
import com.jonathan.themoviedb.data.repository.MovieDataSourceFactory
import com.jonathan.themoviedb.data.repository.NetworkState
import com.jonathan.themoviedb.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class TopRatedMoviesPagedListRepository(private val apiService: Interface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}