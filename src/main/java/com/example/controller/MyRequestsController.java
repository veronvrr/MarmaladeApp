//package com.example.controller;
//
//import com.example.model.AppUser;
//import com.example.model.LessonRequest;
//import com.example.service.LessonRequestService;
//import com.example.service.UserService;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.util.List;
//
//@Controller
//public class MyRequestsController {
//
//    private final LessonRequestService requestService;
//    private final UserService userService;
//
//    public MyRequestsController(LessonRequestService requestService, UserService userService) {
//        this.requestService = requestService;
//        this.userService = userService;
//    }
//
//    @GetMapping("/my/requests")
//    public String myRequests(@AuthenticationPrincipal UserDetails principal, Model model) {
//        if (principal == null) return "redirect:/login";
//        String username = principal.getUsername();
//        List<LessonRequest> list = requestService.findByStudentUsername(username);
//        model.addAttribute("requests", list);
//        AppUser u = userService.findByUsername(username);
//        model.addAttribute("user", u);
//        return "users/my-requests";
//    }
//
////    @PostMapping("/my/requests/{id}/cancel")
////    public String cancelRequest(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
////        if (principal == null) return "redirect:/login";
////        // лучше проверять, что этот request принадлежит текущему юзеру
////        // простой вариант:
////        requestService.deleteById(id);
////        return "redirect:/my/requests?cancelled=1";
////    }
//@PostMapping("/my/requests/{id}/cancel")
//public String cancelRequest(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
//    if (principal == null) return "redirect:/login";
//    String username = principal.getUsername();
//    boolean ok = requestService.cancelByIdIfOwner(id, username);
//    if (!ok) {
//        // можно показать флеш-сообщение об ошибке (не твоя заявка / не найдено)
//        return "redirect:/my/requests?error=not_allowed";
//    }
//    return "redirect:/my/requests?cancelled=1";
//}
//
//}
package com.example.controller;

import com.example.model.AppUser;
import com.example.model.LessonRequest;
import com.example.service.LessonRequestService;
import com.example.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class MyRequestsController {

    private final LessonRequestService requestService;
    private final UserService userService;

    public MyRequestsController(LessonRequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

//    @GetMapping("/my/requests")
//    public String myRequests(@AuthenticationPrincipal UserDetails principal, Model model) {
//        if (principal == null) return "redirect:/login";
//        String username = principal.getUsername();
//        List<LessonRequest> list = requestService.findByStudentUsername(username);
//        model.addAttribute("requests", list);
//        AppUser u = userService.findByUsername(username);
//        model.addAttribute("user", u);
//        return "users/my-requests";
//    }
@GetMapping("/my/requests")
public String myRequests(@AuthenticationPrincipal UserDetails principal, Model model) {
    if (principal == null) return "redirect:/login";
    String username = principal.getUsername();

    List<LessonRequest> list = requestService.findByStudentUsername(username);
    if (list == null) {
        list = Collections.emptyList();
    } else {
        // фильтруем null-элементы на всякий случай
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    model.addAttribute("requests", list);
    AppUser u = userService.findByUsername(username);
    model.addAttribute("user", u);
    return "users/my-requests";
}

    @PostMapping("/my/requests/{id}/cancel")
    public String cancelRequest(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
        if (principal == null) return "redirect:/login";
        requestService.cancelIfOwner(id, principal.getUsername());
        return "redirect:/my/requests?cancelled=1";
    }

}
