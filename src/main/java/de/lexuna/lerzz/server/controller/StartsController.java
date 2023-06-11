package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.Quiz;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class to initiate a quiz with a method to return the quiz statistics
 */
@Controller
public class StartsController {

    @Autowired
    private QuizService service;
    @Autowired
    private DeckService deckService;

    /**
     * Method to return the quiz statistics
     *
     * @param deckId the ID of the deck with the questions of the quiz
     * @param quizId the ID of the quiz
     * @param principal the principal object representing the current user
     * @param model the model object with the data for the view
     * @return the view name for displaying the quiz results
     */
    @GetMapping("/deck/{deckId}/quiz/{quizId}/stats")
    public String getStats(@PathVariable("deckId") String deckId, @PathVariable("quizId") String quizId, Principal principal, Model model) {
        Quiz quiz = service.getQuiz(quizId);
        quiz.finish(principal.getName());
        Quiz.Stats stats = quiz.getStats();
        List<Quiz.Answer> answers = quiz.getAnswers().stream().filter(a -> a.getUserId().equals(principal.getName())).collect(Collectors.toList());
        model.addAttribute("stats", stats);
        model.addAttribute("time", stats.getTime(principal.getName()));
        model.addAttribute("answers", answers);
        model.addAttribute("deckId", deckId);
        model.addAttribute("cards", deckService.cardsAsDTOs(quiz.getQuestions()));
        return "quiz_results";
    }
}
