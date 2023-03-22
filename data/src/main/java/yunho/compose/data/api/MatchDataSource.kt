package yunho.compose.yhgg_remaster.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import yunho.compose.data.API_KEY
import yunho.compose.domain.model.MatchDTO


interface MatchDataSource {
    @GET("/lol/match/v5/matches/by-puuid/{puuid}/ids")
    suspend fun getMatchIdBypuuid(
        @Path("puuid") puuid: String,
        @Query("start") start: Int,
        @Query("count") count: Int,
        @Query("api_key") APIKey: String = API_KEY,
    ): Response<List<String>>

    @GET("/lol/match/v5/matches/{matchId}")
    suspend fun getMatchInfoByMatchID(
        @Path("matchId") MatchId: String,
        @Query("api_key") APIKey: String = API_KEY
    ): Response<MatchDTO>
}