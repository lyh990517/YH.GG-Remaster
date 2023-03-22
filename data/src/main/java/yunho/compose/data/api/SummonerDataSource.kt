package yunho.compose.yhgg_remaster.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import yunho.compose.data.API_KEY
import yunho.compose.domain.model.LeagueEntryDTO
import yunho.compose.domain.model.SummonerDTO



interface SummonerDataSource {
    @GET("/lol/summoner/v4/summoners/by-name/{summonerName}")
    suspend fun getSummonerInfoByName(
        @Path("summonerName") SummonerName: String,
        @Query("api_key") APIKey : String = API_KEY
    ): Response<SummonerDTO>

    @GET("/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
    suspend fun getSummonerEntriesByEncryptedSummonerID(
        @Path("encryptedSummonerId") encryptedSummonerId : String,
        @Query("api_key") APIKey : String = API_KEY
    ): Response<List<LeagueEntryDTO>>

}