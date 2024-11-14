package com.example.todo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

// Man hinh Calenda su dung week recycler view cho tung tuan , ben duoi su dung viewPager cho tung ngay trong tuan