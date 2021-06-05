package ipvc.estg.plantme.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
    @FormUrlEncoded
    @POST("/user")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<User>
}