package com.bit.temperatureapps.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    // A repository class abstracts access to multiple data sources.

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }
}