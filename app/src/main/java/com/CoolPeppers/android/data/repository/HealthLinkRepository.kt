package com.CoolPeppers.android.data.repository

import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.model.Request
import com.CoolPeppers.android.data.model.Service
import kotlinx.coroutines.delay

class ClinicRepository {

    suspend fun fetchDoctors(): List<Doctor> {
        delay(2000)
        return listOf(
            Doctor(1, "Иван","Иванов", "Терапевт", R.drawable.doctor1),
            Doctor(2, "Мария", "Иванов","Хирург", R.drawable.doctor2),
            Doctor(3, "Алексей", "Сидоров","Стоматолог", R.drawable.doctor3)
        )
    }

    suspend fun fetchServices(): List<Service> {
        delay(2000)
        return listOf(
            Service(1, "Кардиолог", "Полная диагностика состояния сердца.", R.drawable.heart ),
            Service(2, "Травматолог", "Лечение сломанных костей", R.drawable.broke),
            Service(3, "Стоматолог", "Профессиональное удаление зубов любой сложности.", R.drawable.tooth),
            Service(3, "Кардиолог", "Полная диагностика состояния сердца.", R.drawable.heart ),
            Service(4, "Травматолог", "Лечение сломанных костей", R.drawable.broke),
            Service(5, "Стоматолог", "Профессиональное удаление зубов любой сложности.", R.drawable.tooth)
        )
    }

    suspend fun fetchClinics(): List<Clinic> {
        delay(2000)
        return listOf(
            Clinic(1, "Клиника Здоровье", "Москва", "Арбатская", R.drawable.clinic1),
            Clinic(2, "Медицинский Центр Лидер", "Москва","Новая", R.drawable.clinic2),
            Clinic(3, "Клиника Улыбка", "Москва","Коломенская", R.drawable.clinic3)
        )
    }

    suspend fun fetchAppointments(): List<Request> {
        delay(2000)
        return listOf(
            Request(1, fetchDoctors()[0], "25.12.2024", "08:00 - 10:00"),
            Request(2, fetchDoctors()[1], "12.12.2024", "10:00 - 12:00"),
            Request(3, fetchDoctors()[2], "13.12.2024", "12:00 - 14:00")
        )
    }

}