package com.example.hayashi_temma.controller;

import ch.qos.logback.core.model.Model;
import com.example.hayashi_temma.controller.form.TaskForm;
import com.example.hayashi_temma.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();

        List<TaskForm> TaskData = taskService.findTasks();
        mav.setViewName("/top");
        mav.addObject("tasks", TaskData);

        return mav;
    }

    //削除機能
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

}
