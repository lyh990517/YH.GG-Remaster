package yunho.compose.yhgg_remaster.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yunho.compose.data.MATCH_URL
import yunho.compose.data.SUMMONER_URL
import yunho.compose.yhgg_remaster.data.api.ApiHelper
import yunho.compose.yhgg_remaster.data.api.MatchDataSource
import yunho.compose.yhgg_remaster.data.api.SummonerDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideSummonerApi() = ApiHelper.create(SummonerDataSource::class.java, SUMMONER_URL)


    @Singleton
    @Provides
    fun provideMatchApi() = ApiHelper.create(MatchDataSource::class.java, MATCH_URL)
}