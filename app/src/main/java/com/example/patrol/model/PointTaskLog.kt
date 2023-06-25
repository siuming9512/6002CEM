package com.example.patrol.model

import java.util.Date

data class PointTaskLog (val id: Int, val point: Point, var checked: Boolean, var taskLogId: Int?, var checkedTime: Date?, var username: String?, var attachmentCount: Int?)