package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.CardDeck;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private DeckService service;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String home(Model model, Authentication authentication) {
        CardDeck deck = new CardDeck("", "", "", null);
        model.addAttribute(deck);
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        model.addAttribute("username", user.getUsername());
        return "dashboard";
    }

    @DeleteMapping("delete/{id}")
    public String deleteCardStack(@PathVariable("id") String deckId, Model model, Authentication authentication) {
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        service.deleteDeck(deckId);
        return "dashboard";
    }


    @ModelAttribute("allDecks")
    public List<CardDeck> populateSeedStarters() {
        return this.service.findAll();
    }

}