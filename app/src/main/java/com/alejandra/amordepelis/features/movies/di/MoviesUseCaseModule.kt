package com.alejandra.amordepelis.features.movies.di

import com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper.MovieFormDtoFactory
import com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper.MovieFormDtoFactoryImpl
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import com.alejandra.amordepelis.features.movies.domain.usecases.AddMovieUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.CreateMovieWithImageUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.GetMovieDetailsUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.GetMoviesUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.MoviesUseCases
import com.alejandra.amordepelis.features.movies.domain.usecases.SearchMoviesUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.SyncMoviesUseCase
import com.alejandra.amordepelis.features.movies.domain.validators.MovieFormValidator
import com.alejandra.amordepelis.features.movies.domain.validators.MovieFormValidatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoviesUseCaseModule {

    // ── Validación ────────────────────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideMovieFormValidator(): MovieFormValidator = MovieFormValidatorImpl()

    // ── Mapper / Factory ──────────────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideMovieFormDtoFactory(): MovieFormDtoFactory = MovieFormDtoFactoryImpl()

    // ── Use Cases ─────────────────────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideGetMoviesUseCase(repository: MoviesRepository): GetMoviesUseCase =
        GetMoviesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMovieDetailsUseCase(repository: MoviesRepository): GetMovieDetailsUseCase =
        GetMovieDetailsUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchMoviesUseCase(repository: MoviesRepository): SearchMoviesUseCase =
        SearchMoviesUseCase(repository)

    @Provides
    @Singleton
    fun provideAddMovieUseCase(repository: MoviesRepository): AddMovieUseCase =
        AddMovieUseCase(repository)

    @Provides
    @Singleton
    fun provideSyncMoviesUseCase(repository: MoviesRepository): SyncMoviesUseCase =
        SyncMoviesUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateMovieWithImageUseCase(
        repository: MoviesRepository,
        validator: MovieFormValidator,
        dtoFactory: MovieFormDtoFactory
    ): CreateMovieWithImageUseCase =
        CreateMovieWithImageUseCase(repository, validator, dtoFactory)

    // ── Aggregate (para MoviesViewModel) ─────────────────────────────────────

    @Provides
    @Singleton
    fun provideMoviesUseCases(
        getMoviesUseCase: GetMoviesUseCase,
        getMovieDetailsUseCase: GetMovieDetailsUseCase,
        searchMoviesUseCase: SearchMoviesUseCase,
        addMovieUseCase: AddMovieUseCase,
        syncMoviesUseCase: SyncMoviesUseCase
    ): MoviesUseCases = MoviesUseCases(
        getMovies = getMoviesUseCase,
        getMovieDetails = getMovieDetailsUseCase,
        searchMovies = searchMoviesUseCase,
        addMovie = addMovieUseCase,
        syncMovies = syncMoviesUseCase
    )
}
