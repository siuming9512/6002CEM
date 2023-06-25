package com.example.patrol.model

import java.util.Date

data class TaskLog (val id: Int, val patrolTask: RouteTask, val point: Point, val taskLogAttachments: List<TaskLogAttachment>, val createDate: Date)