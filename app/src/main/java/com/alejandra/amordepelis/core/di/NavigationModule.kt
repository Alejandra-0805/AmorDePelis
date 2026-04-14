package com.alejandra.amordepelis.core.di

import com.alejandra.amordepelis.core.navigation.FeatureNavGraph
import com.alejandra.amordepelis.features.auth.data.repositories.AuthRepositoryImpl
import com.alejandra.amordepelis.features.auth.navigation.AuthNavGraph
import com.alejandra.amordepelis.features.home.navigation.HomeNavGraph
import com.alejandra.amordepelis.features.lists.navigation.ListsNavGraph
import com.alejandra.amordepelis.features.movies.navigation.MoviesNavGraph
import com.alejandra.amordepelis.features.user.navigation.UserNavGraph
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    @IntoSet
    abstract fun bindAuthNavGraph(impl: AuthNavGraph): FeatureNavGraph

    @Binds
    @IntoSet
    abstract fun bindHomeNavGraph(impl: HomeNavGraph): FeatureNavGraph

    @Binds
    @IntoSet
    abstract fun bindListsNavGraph(impl: ListsNavGraph): FeatureNavGraph

    @Binds
    @IntoSet
    abstract fun bindMoviesNavGraph(impl: MoviesNavGraph): FeatureNavGraph

    @Binds
    @IntoSet
    abstract fun bindUserNavGraph(impl: UserNavGraph): FeatureNavGraph

}