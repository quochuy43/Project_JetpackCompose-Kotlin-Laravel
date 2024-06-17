package com.example.project_appmovie.database.account

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.project_appmovie.database.account.Account

@Dao
interface AccountDAO {

    @Query("SELECT * FROM Account ORDER BY fullName DESC")
    fun getAllAccount(): LiveData<List<Account>>

    @Insert
    fun addAccount(account: Account)

    @Query("DELETE FROM Account WHERE id = :id")
    fun deleteAccount(id: Int)

    // Thêm chức năng để xử lí đăng nhập nữa
    @Query("SELECT * FROM Account WHERE email = :email")
    // Phương thức ni trả về 1 account hoặc null
    fun getAccountByEmail(email: String): Account?

    @Update
    suspend fun updateAccount(account: Account)
}