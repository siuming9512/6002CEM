package com.example.patrol.model

import java.util.Date

data class Point (val id: Int, val name: String, val description: String?, val latitude: Double?, val longitude: Double?,  val tagId: String, val createDate: Date)