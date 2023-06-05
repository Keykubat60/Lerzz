package de.lexuna.lerzz.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lexuna.lerzz.model.Card;
import de.lexuna.lerzz.model.Quiz;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.QuizService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class QuizRestController {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private QuizService service;
    @Autowired
    private DeckService deckService;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketController socketController;

    @PostMapping("/card")
    @ResponseBody
    public String startQuiz(@RequestBody String payload) throws JsonProcessingException {
        String quizId = (payload.substring(payload.lastIndexOf("/")+1)).replace("}","").replace("\"","");
        QuizService.QuizDTO quizDTO = service.toDTO(service.getQuiz(quizId));
        DeckService.McCardDTO cardDto = deckService.asDTO(service.getQuiz(quizId).getQuestions().get(0));
        quizDTO.setStarted(true);
        Map<String, Object> map= new HashMap<>();
        map.put("card", cardDto);
        return objectMapper.writeValueAsString(map);
    }

    @PostMapping("/next")
    @ResponseBody
    public String next(@RequestBody String payload, Principal principal) throws JsonProcessingException {
        Map<String, Object> map = objectMapper.readValue(payload, Map.class);
        String deckId = (String) map.get("deckId");
        String quizId = (String) map.get("quizId");
        int cardId = (int) map.get("cardId");
//        int questionNr = Integer.parseInt((String) map.get("questionNr"));
        int solution = (int) map.get("solution");
        Quiz quiz = service.getQuiz(quizId);

        DeckService.McCardDTO card = service.next(userService.findUserByEmail(principal.getName()), cardId, solution, quiz);

//        DeckService.McCardDTO card = deckService.asDTO(service.getQuiz(quizId).getQuestions().get(questionNr));
        Map<String, Object> response= new HashMap<>();
        response.put("card", card);
        response.put("lastCard", quiz.isLastCard(card));
//        response.put("questionNr", questionNr+1);
        return objectMapper.writeValueAsString(response);
    }

    @PostMapping("/end")
    @ResponseBody
    public String end(@RequestBody String payload, Principal principal) throws JsonProcessingException {
        Map<String, Object> map = objectMapper.readValue(payload, Map.class);
        String quizId = (String) map.get("quizId");
        int cardId = (int) map.get("cardId");
        int solution = (int) map.get("solution");
        Quiz quiz = service.getQuiz(quizId);
        service.end(userService.findUserByEmail(principal.getName()), cardId, solution, quiz);
        return "";
    }
}
