package com.example.todo.view.activity
import androidx.lifecycle.lifecycleScope
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import com.example.todo.R
import com.example.todo.base.BaseActivity
import com.example.todo.data.database.TaskRoomDatabaseClass
import com.example.todo.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.todo.view.fragment.CreateTaskFragment
import com.example.todo.view.fragment.HomeFragment
import io.reactivex.rxjava3.disposables.Disposable

class MainActivity : BaseActivity() {
    var homeFragment = HomeFragment()
    private var disposable: Disposable?=null

    private lateinit var  binding: ActivityMainBinding

    private val taskDatabase by lazy {TaskRoomDatabaseClass.getDatabase(this).taskDao()  }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main, homeFragment)
            commit()
        }
    }
}
