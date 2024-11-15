package com.example.todo.base
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.R
import com.example.todo.utils.BaseContextWrapper
import com.example.todo.utils.LanguagePreference
import java.util.Locale
import io.reactivex.rxjava3.disposables.Disposable

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val language = LanguagePreference.getLanguage(newBase) // Lấy ngôn ngữ đã lưu
        val context = BaseContextWrapper.wrap(newBase, language)
        super.attachBaseContext(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
    }

}