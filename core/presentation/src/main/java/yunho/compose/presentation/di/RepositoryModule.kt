package yunho.compose.yhgg_remaster.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yunho.compose.data.repository.MatchRepositoryImpl
import yunho.compose.data.repository.SummonerRepositoryImpl
import yunho.compose.domain.repository.MatchRepository
import yunho.compose.domain.repository.SummonerRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSummonerRepository(summonerRepositoryImpl: SummonerRepositoryImpl): SummonerRepository

    @Binds
    abstract fun bindMatchRepository(matchRepositoryImpl: MatchRepositoryImpl): MatchRepository
}