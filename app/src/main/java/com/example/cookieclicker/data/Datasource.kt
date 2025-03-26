package com.example.cookieclicker.data

import com.example.cookieclicker.R
import com.example.cookieclicker.model.Cookie

class Datasource {
    val cookieList = listOf(
        Cookie(R.drawable.chocolatechip, 2, 0),
        Cookie(R.drawable.chocolatecookie, 3, 5),
        Cookie(R.drawable.sugarcookie, 5, 20),
        Cookie(R.drawable.holidaycookie, 10, 50),
    )
}