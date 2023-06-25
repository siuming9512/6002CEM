package com.example.patrol.model

import java.util.Date

data class Route (val id: Int, val name: String, val desc: String?, val createDate: Date, val points: List<Point>)