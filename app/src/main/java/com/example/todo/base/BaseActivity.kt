package com.example.todo.base
import android.content.ContextWrapper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.R
import java.util.Locale
import io.reactivex.rxjava3.disposables.Disposable

open class BaseActivity : AppCompatActivity() {
    companion object {
        var  dLocale: Locale?= null
    }

    private  var disposable: Disposable? = null
    var localeUpdatedContext: ContextWrapper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
    }

}