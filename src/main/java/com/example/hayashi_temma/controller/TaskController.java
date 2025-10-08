package com.example.hayashi_temma.controller;

import org.springframework.ui.Model;
import com.example.hayashi_temma.controller.form.TaskForm;
import com.example.hayashi_temma.service.TaskService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
        return mav;
    }

    //新規追加投稿機能
    @PostMapping("/new")
    public ModelAndView addContent(
            @ModelAttribute("formModel") @Validated TaskForm taskForm,
            BindingResult result) {

        //エラーメッセージ
        List<String> errorMessages = getErrorMessages(result);

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("/new");
            mav.addObject("errorMessages", errorMessages);
            mav.addObject("formModel", taskForm); // 入力内容を保持
            return mav;
        }

        //登録
        taskService.saveTask(taskForm);

        //一覧画面へ
        return new ModelAndView("redirect:/todo");
    }

    //編集画面表示
    @GetMapping("/edit/{id}")
    public ModelAndView editTask(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        List<String> errors = new ArrayList<>();

        // IDチェック
        if (id == null || id < 1) {
            errors.add("不正なパラメータです");
            session.setAttribute("errorMessages", errors);
            return new ModelAndView("redirect:/todo");
        }

        // DBから取得
        TaskForm taskForm = taskService.findById(id);
        if (taskForm == null) {
            errors.add("不正なパラメータです");
            session.setAttribute("errorMessages", errors);
            return new ModelAndView("redirect:/todo");
        }

        mav.setViewName("/edit");
        mav.addObject("formModel", taskForm);
        return mav;
    }



    //編集処理
    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Integer id,
                             @ModelAttribute("formModel") @Validated TaskForm taskForm,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            model.addAttribute("errorMessages", errorMessages);
            return "/edit";
        }

        //更新処理
        taskService.updateTask(id, taskForm);
        return "redirect:/todo";
    }

    public static List<String> getErrorMessages(BindingResult result) {
        List<String> errorMessages = new ArrayList<>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        return errorMessages;
    }

}
