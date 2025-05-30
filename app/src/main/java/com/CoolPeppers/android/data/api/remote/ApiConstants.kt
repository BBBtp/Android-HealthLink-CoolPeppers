package com.CoolPeppers.android.data.api.remote

object ApiConstants {
    // Базовый урл
    const val BASE_URL = "http://79.137.192.44:8080/"
    //Клиники
    const val GET_CLINICS_URL = "/api/v1/clinics/"
    const val GET_CLINIC_URL = "/api/v1/clinics/{clinic_id}"
    const val ADD_DELETE_CLINIC_FAVORITE_URL = "/api/v1/clinics/{clinic_id}/favorite"
    const val CLINIC_ID = "clinic_id"
    //Докторы
    const val GET_DOCTORS_URL = "/api/v1/doctors/"
    const val GET_DOCTOR_URL = "/api/v1/doctors/{doctor_id}"
    const val ADD_DELETE_DOCTOR_FAVORITE_URL = "/api/v1/doctors/{doctor_id}/favorite"
    const val DOCTOR_ID = "doctor_id"
    //Услуги
    const val GET_SERVICES_URL = "/api/v1/services/"
    const val GET_SERVICE_URL = "/api/v1/services/{service_id}"
    const val SERVICE_ID = "service_id"
    //Запись
    const val CREATE_GET_APPOINTMENT_URL = "/api/v1/appointments/"
    const val DELETE_APPOINTMENT_URL = "/api/v1/appointments/{appointment_id}"
    const val APPOINTMENT_ID = "appointment_id"
    //Оплата
    const val CREATE_PAYMENT_URL = "/api/v1/payment/create"
    const val CONFIRM_PAYMENT_URL = "/api/v1/payment/confirm"
    //Авторизация
    const val REG_USER_URL = "/api/v1/auth/register"
    const val LOGIN_USER_URL = "/api/v1/auth/login"
    const val REFRESH_TOKEN_URL = "/api/v1/auth/refresh"
    const val USERNAME = "username"
    const val PASSWORD = "password"
    //Профиль
    const val GET_UPDATE_USER_PROFILE_URL = "/api/v1/users/me"
    //Слоты для записи
    const val CREATE_GET_APPOINTMENT_SLOT_SINGLE_URL = "/api/v1/appointment_slots/slots"
    const val GET_SLOT_BY_ID_URL = "/api/v1/appointment_slots/slots/{slot_id}"
    const val GENERATE_APPOINTMENT_SLOTS_URL = "/api/v1/appointment_slots/generate_slots"
    const val SLOT_ID = "slot_id"
    const val SLOT_TIME = "slot_time"
    const val START_TIME = "start_time"
    const val END_TIME = "end_time"
    //Чаты
    const val GET_CHAT_BY_ID_URL = "/api/v1/chat/chats/{chat_id}/"
    const val GET_USER_CHATS_URL = "/api/v1/chat/chats/user/"
    const val CREATE_CHAT_URL = "/api/v1/chat/chats/"
    //Вебсокет
    const val  GET_WEBSOCKET = "api/v1//ws/{user_id}"
    //Симптомы
    const val GET_SYMPTOMS_URL = "/api/v1/match/symptoms/"
    const val MATCH_SERVICES_URL = "/api/v1/match/match-services"
}