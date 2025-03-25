package com.CoolPeppers.android.data.api.remote

import com.CoolPeppers.android.data.model.Appointment
import com.CoolPeppers.android.data.model.AuthRequest
import com.CoolPeppers.android.data.model.AuthResponse
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.model.LoginRequest
import com.CoolPeppers.android.data.model.RefreshToken
import com.CoolPeppers.android.data.model.Service
import com.CoolPeppers.android.data.model.SlotResponse
import com.CoolPeppers.android.data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.sql.Ref

interface  ApiService {

    //Методы для клиник
    @GET(ApiConstants.GET_CLINICS_URL)
    suspend fun getClinics(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100,
        @Query("search") search: String = ""
    ): List<Clinic>

    @GET(ApiConstants.GET_CLINIC_URL)
    suspend fun getClinicById(
        @Path(ApiConstants.CLINIC_ID) clinicId: Int
    ): Clinic

    @POST(ApiConstants.ADD_DELETE_CLINIC_FAVORITE_URL)
    suspend fun addClinicToFavorite(
        @Path(ApiConstants.CLINIC_ID) clinicId: Int
    )

    @DELETE(ApiConstants.ADD_DELETE_CLINIC_FAVORITE_URL)
    suspend fun deleteClinicFromFavorite(
        @Path(ApiConstants.CLINIC_ID) clinicId: Int
    )

    //Методы для врачей
    @GET(ApiConstants.GET_DOCTORS_URL)
    suspend fun getDoctors(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100,
        @Query("search") search: String = "",
        @Query(ApiConstants.SERVICE_ID) serviceId: Int?,
        @Query(ApiConstants.CLINIC_ID) clinicId: Int?
    ): List<Doctor>

    @GET(ApiConstants.GET_DOCTOR_URL)
    suspend fun getDoctorById(
        @Path(ApiConstants.DOCTOR_ID) doctorId: Int
    ): Doctor

    @POST(ApiConstants.ADD_DELETE_DOCTOR_FAVORITE_URL)
    suspend fun addDoctorToFavorite(
        @Path(ApiConstants.DOCTOR_ID) doctorId: Int
    )

    @DELETE(ApiConstants.ADD_DELETE_DOCTOR_FAVORITE_URL)
    suspend fun deleteDoctorFromFavorite(
        @Path(ApiConstants.DOCTOR_ID) doctorId: Int
    )

    //Методы для услуг
    @GET(ApiConstants.GET_SERVICES_URL)
    suspend fun getServices(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100,
        @Query("search") search: String = "",
        @Query(ApiConstants.CLINIC_ID) clinicId: Int?
    ): List<Service>

    @GET(ApiConstants.GET_SERVICE_URL)
    suspend fun getServiceById(
        @Path(ApiConstants.SERVICE_ID) serviceId: Int
    ): Service

    //Методы для записи
    @POST(ApiConstants.CREATE_GET_APPOINTMENT_URL)
    suspend fun createAppointment(
        @Query(ApiConstants.CLINIC_ID) clinicId: Int,
        @Query(ApiConstants.DOCTOR_ID) doctorId: Int,
        @Query(ApiConstants.SERVICE_ID) serviceId: Int,
        @Query(ApiConstants.SLOT_ID) slotId: Int
    ) : Appointment

    @GET(ApiConstants.CREATE_GET_APPOINTMENT_URL)
    suspend fun getAppointments(
    ): List<Appointment>

    @DELETE(ApiConstants.DELETE_APPOINTMENT_URL)
    suspend fun deleteAppointment(
        @Query(ApiConstants.APPOINTMENT_ID) appointmentId: Int
    )
    //Методы для оплаты
    @POST(ApiConstants.CREATE_PAYMENT_URL)
    suspend fun createPayment(
        //TODO : - метод доделать, не использовать пока что
    )

    @POST(ApiConstants.CONFIRM_PAYMENT_URL)
    suspend fun confirmPayment(
        //TODO: - метод доделать, не использовать пока что
    )
    //Методы для авторизации
    @POST(ApiConstants.REG_USER_URL)
    suspend fun regUser(
        @Body request: AuthRequest
    )

    @FormUrlEncoded
    @POST(ApiConstants.LOGIN_USER_URL)
    suspend fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): AuthResponse

    @POST(ApiConstants.REFRESH_TOKEN_URL)
    suspend fun refreshToken(
        @Body request: RefreshToken
    ): AuthResponse

    //Методы пользователя
    @GET(ApiConstants.GET_UPDATE_USER_PROFILE_URL)
    suspend fun getUser(): User

    @Multipart
    @PUT(ApiConstants.GET_UPDATE_USER_PROFILE_URL)
    suspend fun updateUser(
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("age") age: RequestBody?,
        @Part("blood_type") bloodType: RequestBody,
        @Part photo: MultipartBody.Part?
    ): User

    //Методы для слотов записи
    @POST(ApiConstants.CREATE_GET_APPOINTMENT_SLOT_SINGLE_URL)
    suspend fun createSingleSlot(
        @Query(ApiConstants.DOCTOR_ID) doctorId: Int,
        @Query(ApiConstants.SLOT_TIME) slotTime: String
    ): SlotResponse

    @GET(ApiConstants.CREATE_GET_APPOINTMENT_SLOT_SINGLE_URL)
    suspend fun getSlots(
        @Query(ApiConstants.DOCTOR_ID) doctorId: Int
    ) : List<SlotResponse>

    @GET(ApiConstants.GET_SLOT_BY_ID_URL)
    suspend fun getSlotById(
        @Path("slot_id") slotId: Int
    ): SlotResponse

    @POST(ApiConstants.GENERATE_APPOINTMENT_SLOTS_URL)
    suspend fun generateSlots(
        @Query(ApiConstants.DOCTOR_ID) doctorId: Int,
        @Query(ApiConstants.START_TIME) startTime: String,
        @Query(ApiConstants.END_TIME) endTime: String
    )
}