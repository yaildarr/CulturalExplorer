package ru.ildar.auth_impl.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await
import ru.ildar.api.repository.AuthRepository
import ru.ildar.domain.model.MyResult
import java.io.IOException
import java.net.SocketTimeoutException

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    override suspend fun signUp(
        email: String,
        password: String
    ): MyResult {
        return try{
            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            MyResult.Success(userId = result.user?.uid ?: "")
        } catch (e: Exception){
            e.toAuthResult()
        }
    }

    override suspend fun isAuthenticated(): Boolean {
        Log.d("MyLog","$firebaseAuth.currentUser")
        return firebaseAuth.currentUser != null
    }

    private fun Exception.toAuthResult(): MyResult {
        return when (this) {
            is FirebaseAuthWeakPasswordException ->
                MyResult.Error("Пароль должен содержать минимум 6 символов")
            is FirebaseAuthUserCollisionException ->
                MyResult.Error("Пользователь с таким email уже существует")
            is SocketTimeoutException, is IOException ->
                MyResult.Error("Проверьте подключение к интернету")
            else ->
                MyResult.Error("Ошибка регистрации: ${localizedMessage}")
        }
    }
}