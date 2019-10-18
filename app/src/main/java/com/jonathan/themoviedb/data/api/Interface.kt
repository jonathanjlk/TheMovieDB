package com.jonathan.themoviedb.data.api


import com.jonathan.themoviedb.data.vo.MovieDetails
import com.jonathan.themoviedb.data.vo.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Interface {
//https://api.themoviedb.org/3/movie/top_rated?api_key=11004c5dda64d0bae607c7af2636e983&page=1
//https://api.themoviedb.org/3/movie/475557?api_key=11004c5dda64d0bae607c7af2636e983
//https://api.themoviedb.org/3/discover/movie?api_key=11004c5dda64d0bae607c7af2636e983&sort_by=release_date.asc&release_date.gte=2012-01-01&release_date.lte=2018-12-31&page=1

    //https://api.themoviedb.org/3/movie/475557/videos?api_key=11004c5dda64d0bae607c7af2636e983
//https://www.youtube.com/watch?v=xRjvmVaFHkk
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("release_date.gte") from: String, @Query("release_date.lte") to: String, @Query(
            "page"
        ) page: Int
    ): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>
}