package com.miham.hayseed.presentation.changepassword

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.miham.hayseed.R
import com.miham.hayseed.databinding.ActivityChangePasswordBinding
import com.miham.hayseed.utils.hideSoftKeyboard
import com.miham.hayseed.utils.showDialogLoading
import com.miham.hayseed.utils.showDialogSuccess
//import com.miham.hayseed.utils.showDialogError
//import com.miham.hayseed.utils.showDialogLoading
//import com.miham.hayseed.utils.showDialogSuccess
import org.jetbrains.anko.toast

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordBinding: ActivityChangePasswordBinding
    private lateinit var dialogLoading: AlertDialog
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(changePasswordBinding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        currentUser = FirebaseAuth.getInstance().currentUser
        dialogLoading = showDialogLoading(this)
        onAction()
    }

    private fun onAction() {
        changePasswordBinding.apply {
            btnChangePassword.setOnClickListener {
                val oldPass = etOldPasswordChangePassword.text.toString().trim()
                val newPass = etNewPasswordChangePassword.text.toString().trim()
                val confirmNewPass = etConfirmPasswordChangePassword.text.toString().trim()

                if (checkValidation(oldPass, newPass, confirmNewPass)){
                    hideSoftKeyboard(this@ChangePasswordActivity, changePasswordBinding.root)
                    changePasswordToServer(oldPass, newPass)
                }
                toast("Change Password")
            }

            btnCloseChangePassword.setOnClickListener {
                toast("Change Password")
                finish() }
        }
    }

    private fun changePasswordToServer(oldPass: String, newPass: String) {
        dialogLoading.show()
        currentUser?.let { mCurrentUser ->
            val credential = EmailAuthProvider.getCredential(mCurrentUser.email.toString(), oldPass)

            mCurrentUser.reauthenticate(credential)
                .addOnSuccessListener {
                    mCurrentUser.updatePassword(newPass)
                        .addOnSuccessListener {
                            dialogLoading.dismiss()
                            val dialogSuccess = showDialogSuccess(this, getString(R.string.success_change_pass))
                            dialogSuccess.show()
                            toast("Selamat kata sandi telah diubah")
                            Handler(Looper.getMainLooper())
                                .postDelayed({
                                    dialogSuccess.dismiss()
                                    toast("Selamat kata sandi telah diubah")
                                    finish()
                                }, 3000)
                        }
                        .addOnFailureListener {
                     //       dialogLoading.dismiss()
  //                          showDialogError(this, it.message.toString())
                            toast("Error")
                        }
                }
                .addOnFailureListener {
                 //   dialogLoading.dismiss()
                   // showDialogError(this, it.message.toString())
                    toast("Error")
                }
        }
    }

    private fun checkValidation(oldPass: String, newPass: String, confirmNewPass: String): Boolean {
        changePasswordBinding.apply {
            when{
                oldPass.isEmpty() -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_field_your_old_password)
                    etOldPasswordChangePassword.requestFocus()
                }
                oldPass.length < 8 -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_field_your_password_more_than_8)
                    etOldPasswordChangePassword.requestFocus()
                }
                newPass.isEmpty() -> {
                    etNewPasswordChangePassword.error = getString(R.string.please_field_your_new_password)
                    etNewPasswordChangePassword.requestFocus()
                }
                newPass.length < 8 -> {
                    etNewPasswordChangePassword.error = getString(R.string.please_field_your_password_more_than_8)
                    etNewPasswordChangePassword.requestFocus()
                }
                confirmNewPass.isEmpty() -> {
                    etConfirmPasswordChangePassword.error = getString(R.string.please_field_your_confirm_new_password)
                    etConfirmPasswordChangePassword.requestFocus()
                }
                confirmNewPass.length < 8 -> {
                    etConfirmPasswordChangePassword.error = getString(R.string.please_field_your_password_more_than_8)
                    etConfirmPasswordChangePassword.requestFocus()
                }
                newPass != confirmNewPass -> {
                    etNewPasswordChangePassword.error = getString(R.string.your_password_didnt_match)
                    etNewPasswordChangePassword.requestFocus()
                    etConfirmPasswordChangePassword.error = getString(R.string.your_password_didnt_match)
                    etConfirmPasswordChangePassword.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }
}