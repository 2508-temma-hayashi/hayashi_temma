package com.example.hayashi_temma.repository;

import com.example.hayashi_temma.controller.form.TaskForm;
import com.example.hayashi_temma.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByLimitDateBetweenOrderByLimitDateAsc(LocalDateTime start, LocalDateTime end);
    List<Task> findByLimitDateBetweenAndContentContainingOrderByLimitDateAsc(LocalDateTime start, LocalDateTime end, String content);
    List<Task> findByLimitDateBetweenAndStatusOrderByLimitDateAsc(LocalDateTime start, LocalDateTime end, Integer status);
    List<Task> findByLimitDateBetweenAndContentContainingAndStatusOrderByLimitDateAsc(LocalDateTime start, LocalDateTime end, String content, Integer status);
}

