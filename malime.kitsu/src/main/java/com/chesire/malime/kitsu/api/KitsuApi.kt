package com.chesire.malime.kitsu.api

import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.kitsu.models.request.LoginRequest
import com.chesire.malime.kitsu.models.request.RefreshAuthRequest
import com.chesire.malime.kitsu.models.response.AddItemResponse
import com.chesire.malime.kitsu.models.response.LibraryResponse
import com.chesire.malime.kitsu.models.response.LoginResponse
import com.chesire.malime.kitsu.models.response.UpdateItemResponse
import okhttp3.RequestBody
import retrofit2.Call
import javax.inject.Inject

class KitsuApi @Inject constructor(private val kitsuService: KitsuService) {
    fun login(username: String, password: String): Call<LoginResponse> {
        return kitsuService.login(LoginRequest(username, password))
    }

    fun refreshAuthToken(refreshToken: String): Call<LoginResponse> {
        return kitsuService.refreshAuth(RefreshAuthRequest(refreshToken))
    }

    fun getUser(): Call<LibraryResponse> = kitsuService.getUser()

    fun getUserLibrary(userId: Int, offset: Int, type: ItemType): Call<LibraryResponse> {
        // Ideally wanted to keep the api without logic, but this is nicer than in the manager
        return if (type == ItemType.Anime) {
            kitsuService.getUserAnime(userId, offset)
        } else {
            kitsuService.getUserManga(userId, offset)
        }
    }

    fun search(title: String, type: ItemType) = kitsuService.search(type.text, title)

    fun addItem(data: RequestBody): Call<AddItemResponse> = kitsuService.addItem(data)

    fun deleteItem(seriesId: Int): Call<Any> = kitsuService.deleteItem(seriesId)

    fun updateItem(seriesId: Int, updateModel: RequestBody): Call<UpdateItemResponse> {
        return kitsuService.updateItem(seriesId, updateModel)
    }
}
