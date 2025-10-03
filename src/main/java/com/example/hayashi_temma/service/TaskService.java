package com.example.hayashi_temma.service;

import com.example.hayashi_temma.controller.form.TaskForm;
import com.example.hayashi_temma.repository.TaskRepository;
import com.example.hayashi_temma.repository.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository TaskRepository;

    public List<TaskForm> findTasks() {
        //なんでここが昇順なのか確認
        List<Task> results = TaskRepository.findAllByOrderByLimitDateAsc();
        List<TaskForm> tasks = setTaskForm(results);
        return tasks;
    }

    private List<TaskForm> setTaskForm(List<Task> results){
        List<TaskForm> tasks = new ArrayList<>();

        for (Task result : results) {
            TaskForm form = new TaskForm();
            Task task = result;
            form.setId(task.getId());
            form.setContent(task.getContent());
            form.setStatus(task.getStatus());
            form.setLimitDate(task.getLimitDate());
            tasks.add(form);
        }
        return tasks;
    }

}
