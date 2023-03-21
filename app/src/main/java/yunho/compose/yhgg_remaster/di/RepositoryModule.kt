package yunho.compose.yhgg_remaster.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yunho.compose.yhgg_remaster.data.repository.SummonerRepository
import yunho.compose.yhgg_remaster.data.repository.SummonerRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSummonerRepository(summonerRepositoryImpl: SummonerRepositoryImpl): SummonerRepository
}