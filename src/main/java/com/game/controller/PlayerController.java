package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    private final PlayerService playerService;
    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<Player>> getPlayersList() {
        List<Player> players = playerService.findAll();

        if (players.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(players, HttpStatus.OK);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getPlayersCount() {
        Integer count = 0;
        List<Player> players = playerService.findAll();
        if(players == null || players.isEmpty()) {
            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
        }
        for (Player player : players) {
            count++;
        }

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (player == null) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }
        // нужно реализовать проверку полей
        playerService.savePlayer(player);
        return new ResponseEntity<>(player, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") Long playerId) {
        if(playerId == null || playerId == 0) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }

        Player player = playerService.findById(playerId);

        if(player == null || playerId == 0) {
            return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Player>(player, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") Long playerId) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (playerId == null || playerId == 0) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }

        Player player = getPlayer(playerId).getBody();

        this.playerService.savePlayer(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable("id") Long playerId) {
        if(playerId == null || playerId == 0) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }

        Player player = playerService.findById(playerId);

        if(player == null) {
            return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
        }
        playerService.deletePlayer(playerId);
        return new ResponseEntity<Player>(player, HttpStatus.NO_CONTENT);
    }

}
