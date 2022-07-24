package com.example.blockChat.controller;

import com.example.blockChat.domain.*;
import com.example.blockChat.repos.MessageRepo;
import com.example.blockChat.repos.PublicationRepo;
import com.example.blockChat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private PublicationRepo publicationRepo;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserService userService;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model,
                           @AuthenticationPrincipal User user
    ) {
        model.put("user", user);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,
                       @RequestParam(required = false, defaultValue = "") String filter, Model model) {

        Iterable<Publication> publications = publicationRepo.findAll();
        if (filter != null && !filter.isEmpty()) {
            publications = publicationRepo.findByTag(filter);
        } else {
            publications = publicationRepo.findAll();
        }
        model.addAttribute("publications", publications);
        model.addAttribute("filter", filter);
        model.addAttribute("user", user);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Publication publication,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        publication.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ContorollerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute("publication", publication);
        } else {
            saveFile(publication, file);
            model.addAttribute("publication", null);
            publicationRepo.save(publication);
        }
        Iterable<Publication> publications = publicationRepo.findAll();
        model.addAttribute("publications", publications);
        return "main";
    }

    private void saveFile(Publication publication, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));

            publication.setFilename(resultFilename);
        }
    }

    @GetMapping("/user-publications/{user}")
    public String userpublications(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Publication publication

    ) {
        Set<Publication> publications = user.getPublications();
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("publications", publications);
        model.addAttribute("publication", publication);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("user", user);
        return "userPublications";
    }

    @PostMapping("/user-publications/{user}")
    public String updatePublications(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam(value = "id", required = false) Publication publication,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (publication == null) {
            publication = new Publication();
            publication.setAuthor(currentUser);
        }
        if (publication.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                publication.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                publication.setTag(tag);
            }
            saveFile(publication, file);
            publicationRepo.save(publication);
        }

        return "redirect:/user-publications/" + user;
    }

    @GetMapping("/publication/comment/{user}")
    public String publicationCommens(@AuthenticationPrincipal User currentUser,
                                     @PathVariable User user,
                                     Model model,
                                     @RequestParam(required = false) Publication publication
    ) {
        if (currentUser.getId().equals(publication.getAuthor().getId())) {
            publication.setLastView();
            publicationRepo.save(publication);
        }
        model.addAttribute("publication", publication);
        model.addAttribute("user", currentUser);
        Set<Message> messages = messageRepo.findByPublicationOrderById(publication);
        model.addAttribute("messages", messages);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userComment";
    }

    @PostMapping("/publication/comment/{user}")
    public String updateCommens(@AuthenticationPrincipal User currentUser,
                                @PathVariable User user,
                                @RequestParam(value = "publication") Publication publication,
                                @RequestParam(value = "text", required = false) String text
    ) throws IOException {
        if (!StringUtils.isEmpty(text)) {
            Message message = new Message();
            message.setText(text);
            message.setAuthor(currentUser);
            message.setPublication(publication);
            messageRepo.save(message);
        }
        return "redirect:/publication/comment/" + user.getId() + "?publication=" + publication.getId();
    }

    @GetMapping("/user-events/{user}")
    public String userEvents(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Publication publication
    ) {
        Long countComments = userService.getCountNewComments(user);
        model.addAttribute("user", user);
        model.addAttribute("countcomment", countComments);

        return "userEvent";
    }

    @GetMapping("/user-event/comments/{user}")
    public String userPublicationWithNewComments(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Publication publication
    ) {
        Set<IPublication> publications = userService.getPublicationsWithNewMessage(user);
        model.addAttribute("user", user);
        model.addAttribute("publications", publications);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userPublications";
    }

    @PostMapping("/user-events/{user}")
    public String updatePublications(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            @RequestParam(value = "publication", required = false) Publication publication,
            @RequestParam(value = "text", required = false) String text
    ) throws IOException {

        if (!StringUtils.isEmpty(text)) {
            Message message = new Message();
            message.setText(text);
            message.setAuthor(currentUser);
            message.setPublication(publication);
            messageRepo.save(message);
        }
        return "redirect:/user-events/" + user.getId();
    }
}