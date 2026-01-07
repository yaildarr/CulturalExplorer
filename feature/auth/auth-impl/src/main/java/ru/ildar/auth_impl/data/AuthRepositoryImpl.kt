package ru.ildar.auth_impl.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await
import ru.ildar.api.repository.AuthRepository
import ru.ildar.domain.model.AuthResult
import java.io.IOException
import java.net.SocketTimeoutException

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override suspend fun signUp(
        email: String,
        password: String
    ): AuthResult {
        return try{
            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            AuthResult.Success(userId = result.user?.uid ?: "")
        } catch (e: Exception){
            e.toAuthResult()
        }
    }

    override suspend fun isAuthenticated(): Boolean {
        Log.d("MyLog","$firebaseAuth.currentUser")
        return firebaseAuth.currentUser != null
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): AuthResult {
        return try{
            val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
            AuthResult.Success(userId = result.user?.uid ?: "")
        } catch (e: Exception){
            e.toAuthResult()
        }
    }

    private fun Exception.toAuthResult(): AuthResult {
        return when (this) {
            is FirebaseAuthWeakPasswordException ->
                AuthResult.Error("Пароль должен содержать минимум 6 символов")
            is FirebaseAuthUserCollisionException ->
                AuthResult.Error("Пользователь с таким email уже существует")
            is SocketTimeoutException, is IOException ->
                AuthResult.Error("Проверьте подключение к интернету")
            else ->
                AuthResult.Error("Ошибка регистрации: ${localizedMessage}")
        }
    }
}