package com.example.individualproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DefaultActivity : AppCompatActivity() {
    private lateinit var loginB: Button
    private lateinit var registerB: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default)

        loginB = findViewById(R.id.loginButton)
        registerB = findViewById(R.id.registerButton)


        registerB.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        loginB.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}