package com.peterlopusan.traveldiary.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.data.models.user.CreateUserBody
import com.peterlopusan.traveldiary.data.models.user.UserInfo
import com.peterlopusan.traveldiary.databaseUrl
import com.peterlopusan.traveldiary.storageUrl
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.userInformation
import com.peterlopusan.traveldiary.utils.showLogs
import com.peterlopusan.traveldiary.utils.showToast

class AuthViewModel: ViewModel() {
    private var auth: FirebaseAuth =  Firebase.auth
    private val database = FirebaseDatabase.getInstance(databaseUrl).reference
    private val storage = FirebaseStorage.getInstance(storageUrl).reference
    var createBody = CreateUserBody()
    var userInfo: UserInfo? = null


    init {
        getInfoAboutMe()
        auth.useAppLanguage()
    }


    fun resetCreateBody() {
        createBody = CreateUserBody()
    }

    fun login(email: String, password: String): MutableLiveData<Boolean> {
        val isSuccessful: MutableLiveData<Boolean> = MutableLiveData()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            isSuccessful.value = task.isSuccessful
            task.exception?.message?.let { showToast(it) }
            getInfoAboutMe()
        }
        return isSuccessful
    }

    fun createAccount(countryCode: String): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        auth.createUserWithEmailAndPassword(createBody.email, createBody.password).addOnCompleteListener { task ->
            liveData.value = task.isSuccessful

            if (task.isSuccessful) {
                saveUserData(createBody.firstname, createBody.lastname, countryCode, false)
            }
        }.addOnFailureListener {
            showToast(it.message ?: "")
        }

        return liveData
    }

    private fun getInfoAboutMe() {
        try {
            database.child(auth.uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dbChild in snapshot.children) {
                        userInfo = dbChild.getValue(UserInfo::class.java)
                    }
                }

                override fun onCancelled(error: DatabaseError) {  }
            })
        } catch (ex: Exception) {
            showLogs(ex.message)
        }
    }

    fun logout() {
       auth.signOut()
    }

    fun isUserLogged(): Boolean {
        return auth.currentUser != null
    }

    fun resetPassword(email: String? = null) {
        auth.sendPasswordResetEmail(email ?: auth.currentUser?.email!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast(MainActivity.instance.getString(R.string.settings_screen_reset_password_mail_send))
            } else {
                showToast(MainActivity.instance.getString(R.string.settings_screen_reset_password_mail_send_failed))
            }
        }
    }

    fun saveUserData(firstname: String, lastname: String, countryCode: String, edit: Boolean) {
        val userId = auth.currentUser!!.uid
        val userInfo = UserInfo(
            userId = userId,
            firstname = firstname,
            lastname = lastname,
            countryCode = countryCode
        )
        database.child(userId).child(userInformation).setValue(userInfo).addOnSuccessListener {
            this.userInfo = userInfo
            if (edit) {
                showToast(MainActivity.instance.getString(R.string.change_user_data_screen_successfully_changed))
            } else {
                showToast(MainActivity.instance.getString(R.string.create_account_screen_welcome))
            }
        }.addOnFailureListener {
            showLogs(it.message)
        }
    }

    fun deleteAccount() {
        val uid = auth.uid!!

        database.child(uid).removeValue().addOnSuccessListener {
            auth.currentUser?.delete()?.addOnSuccessListener {
                MainActivity.navController.navigate(TravelDiaryRoutes.Login.name) {
                    popUpTo(TravelDiaryRoutes.MainScreen.name) { inclusive = true }
                }

                storage.child(uid).listAll().addOnSuccessListener {
                    it.items.forEach { item ->
                        item.delete().addOnSuccessListener {
                            showLogs("done")
                        }.addOnFailureListener { error ->
                            showLogs("storage ${error.message}")
                        }
                    }
                }
            }?.addOnFailureListener {
                showLogs("auth ${it.message}")
            }
        }.addOnFailureListener {
            showLogs("database ${it.message}")
        }
    }
}