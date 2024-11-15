package com.example.todo.data

import com.example.todo.data.model.Task

object TaskDataSource {
    val sampleTasks = listOf(
        Task(
            idTask = 1,
            nameTask = "Finish Homework",
            contentTask = "Complete all exercises in chapter 3.",
            startTime = 1678369200000L, // timestamp
            endTime = 1678455600000L, // timestamp
            priority = "High"
        ),
        Task(
            idTask = 2,
            nameTask = "Grocery Shopping",
            contentTask = "Buy milk, bread, and eggs.",
            startTime = 1678383600000L, // timestamp
            endTime = 1678390800000L, // timestamp
            priority = "Medium"
        ),
        Task(
            idTask = 3,
            nameTask = "Doctor Appointment",
            contentTask = "Visit the doctor for a routine check-up.",
            startTime = 1678470000000L, // timestamp
            endTime = 1678473600000L, // timestamp
            priority = "Low"
        ),
        Task(
            idTask = 4,
            nameTask = "Project Meeting",
            contentTask = "Discuss project progress and deadlines.",
            startTime = 1678477200000L, // timestamp
            endTime = 1678480800000L, // timestamp
            priority = "High"
        ),
        Task(
            idTask = 5,
            nameTask = "Call Mom",
            contentTask = "Check in with mom and catch up.",
            startTime = 1678484400000L, // timestamp
            endTime = 1678488000000L, // timestamp
            priority = "Medium"
        )
    )
}
