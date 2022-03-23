package com.jachai.jachaimart.elearning.service


import com.jachai.jachaimart.elearning.model.response.ClassListResponse
import com.jachai.jachaimart.elearning.model.response.DiciplineListResponse
import com.jachai.jachaimart.elearning.model.response.ProgramListResponse
import com.jachai.jachaimart.model.response.category.ShopByCategoryResponse
import com.jachai.jachaimart.model.response.home.BannerResponse
import com.jachai.jachaimart.model.response.home.CategoryResponse
import com.jachai.jachaimart.model.response.home.RestaurantNearMeResponse
import com.jachai.jachaimart.model.shop.ShopDetailsResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ElearningService {

    @GET("program")
    fun getAllProgram(): Call<ProgramListResponse>

    @GET("curriculum/all")
    fun getAllClass(@Query("programId") programId: String ): Call<ClassListResponse>

    @GET("discipline/all")
    fun getAllDicipline(@Query("programId") programId: String, @Query("curriculumId") curriculumId: String ): Call<DiciplineListResponse>

    @GET("course")
    fun getAllCourse(@Query("programId") programId: String, @Query("curriculumId") curriculumId: String, @Query("disciplineId") disciplineId: String ): Call<DiciplineListResponse>

}