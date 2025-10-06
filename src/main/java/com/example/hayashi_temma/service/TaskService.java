package com.example.hayashi_temma.service;

import com.example.hayashi_temma.controller.form.TaskForm;
import com.example.hayashi_temma.repository.TaskRepository;
import com.example.hayashi_temma.repository.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    public List<TaskForm> findTasks(LocalDate start, LocalDate end, String content, Integer status) {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        if(start != null){
            startDateTime = start.atStartOfDay();
        }else{
            startDateTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        }


        List<Task> results;
        if(end != null){
            endDateTime = end.atTime(23, 59, 59);
        }else{
            endDateTime = LocalDateTime.now();
        }

        if (!StringUtils.isBlank(content) && status != null) {
            results = taskRepository.findByLimitDateBetweenAndContentContainingAndStatusOrderByLimitDateAsc(
                    startDateTime, endDateTime, content, status);
        } else if (!StringUtils.isBlank(content)) {
            results = taskRepository.findByLimitDateBetweenAndContentContainingOrderByLimitDateAsc(
                    startDateTime, endDateTime, content);
        } else if (status != null) {
            results = taskRepository.findByLimitDateBetweenAndStatusOrderByLimitDateAsc(
                    startDateTime, endDateTime, status);
        } else {
            results = taskRepository.findByLimitDateBetweenOrderByLimitDateAsc(startDateTime, endDateTime);
        }
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

    //削除
    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }

    //ステータス更新
    public void updateStatus(Integer id, Integer status) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setStatus(status);
        task.setUpdatedDate(LocalDateTime.now());
        taskRepository.save(task);
    }
}
