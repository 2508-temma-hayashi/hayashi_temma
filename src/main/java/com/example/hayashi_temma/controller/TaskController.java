package com.example.hayashi_temma.controller;

import ch.qos.logback.core.model.Model;
import com.example.hayashi_temma.controller.form.TaskForm;
import com.example.hayashi_temma.service.TaskService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class TaskController {
    @Autowired
    TaskService taskService;

    @Autowired
    private HttpSession session;

    @GetMapping
    public ModelAndView top(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate start,
                            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end,
                            @RequestParam(required = false) String content,
                            @RequestParam(required = false) Integer status) {
        ModelAndView mav = new ModelAndView();

        List<TaskForm> TaskData = taskService.findTasks(start, end, content, status);
        mav.setViewName("/top");
        mav.addObject("tasks", TaskData);
        //絞り込み条件を再表示
        mav.addObject("start", start);
        mav.addObject("end", end);
        mav.addObject("content", content);
        mav.addObject("status", status);


        return mav;
    }

    //削除機能
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/todo");
    }

    //ステータス変更
    @PostMapping("/updateStatus")
    public ModelAndView updateStatus(@RequestParam Integer id,
                                     @RequestParam Integer status) {
        taskService.updateStatus(id, status);
        return new ModelAndView("redirect:/todo");
    }

    //新規追加画面表示
    @GetMapping("/new")
    public ModelAndView newTask() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        TaskForm taskForm = new TaskForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", taskForm);
        List<String> errorMessages = (List<String>) session.getAttribute("errorMessages");
        if (errorMessages != null) {
            mav.addObject("errorMessages", errorMessages);
        }
        session.removeAttribute("errorMessages");
        session.removeAttribute("errorReportId");
        return mav;
    }

    //新規追加投稿機能
    @PostMapping("/new")
    public ModelAndView addContent(@ModelAttribute("formModel") @Validated TaskForm taskForm, BindingResult result){
        List<String> errorMessages = new ArrayList<>();
        String content = taskForm.getContent();
        LocalDateTime limitDate = taskForm.getLimitDate();
        //入力チェック
        if (StringUtils.isBlank(content)) {
            errorMessages.add("タスクを入力してください");
        }else if (content.length() > 140) {
            errorMessages.add("タスクは140文字以内で入力してください");
        }

        if (limitDate == null) {
            errorMessages.add("期限を設定してください");
        } else if (limitDate.isBefore(LocalDateTime.now())) {
            errorMessages.add("無効な日付です");
        }

        if (!errorMessages.isEmpty()) {
            session.setAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/todo/new");
        }
        // 投稿をテーブルに格納
        taskService.saveTask(TaskForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/todo");
    }
}
