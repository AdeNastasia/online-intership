package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public @ResponseBody ResponseEntity<List<Player>> getPlayersList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) String race,
            @RequestParam(value = "profession", required = false) String profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "order", defaultValue = "id") String order) {

        Race raceObject = Player.convertingRace(race);
        Profession professionObject = Player.convertingProfession(profession);
        Date afterDateObject = Player.convertingAfterDate(after);
        Date beforeDateObject = Player.convertingBeforeDate(before);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order).ascending());
        List<Player> result = playerService.getPlayerRepository().findAllBy(
                name, title, raceObject, professionObject, afterDateObject, beforeDateObject,
                banned, minExperience, maxExperience, minLevel, maxLevel, pageable).getContent();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/count")
    public @ResponseBody
    Long getPlayersCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) String race,
            @RequestParam(value = "profession", required = false) String profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel
    ) {
        Race raceObject = Player.convertingRace(race);
        Profession professionObject = Player.convertingProfession(profession);
        Date afterDateObject = Player.convertingAfterDate(after);
        Date beforeDateObject = Player.convertingBeforeDate(before);

        return playerService.getPlayerRepository().count(
                name, title, raceObject, professionObject, afterDateObject, beforeDateObject,
                banned, minExperience, maxExperience, minLevel, maxLevel);
    }


    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        if (player == null || !player.doWeHaveAllNeededParams()) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        } else {
            player.setLevel();
            player.setUntilNextLevel();
            Player playerInDB = playerService.savePlayer(player);
            return new ResponseEntity<>(playerInDB, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") Long playerId) {
        if (playerId == null || playerId == 0) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }

        boolean isPlayerExist = playerService.getPlayerRepository().findById(playerId).isPresent();

        if (isPlayerExist) {
            return new ResponseEntity<>(playerService.getPlayerRepository().findById(playerId).get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") Long playerId, @RequestBody Player player) {

        if (playerId == null || playerId <= 0) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        }

        boolean isPlayerExist = playerService.getPlayerRepository().findById(playerId).isPresent();

        if (!isPlayerExist){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Player playerInDB = playerService.getPlayerRepository().findById(playerId).get();

            if (!player.isNameNull()) {
                playerInDB.setName(player.getName());
            }

            if (!player.isTitleNull()) {
                playerInDB.setTitle(player.getTitle());
            }

            if (!player.isRaceNull()) {
                playerInDB.setRace(player.getRace());
            }

            if (!player.isProfessionNull()) {
                playerInDB.setProfession(player.getProfession());
            }

            if (!player.isBirthdayNull()) {
                if (player.hasCorrectBirthday()) {
                    playerInDB.setBirthday(player.getBirthday());
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }

            if (!player.isBannedNull()) {
                playerInDB.setBanned(player.getBanned());
            }

            if (!player.isExperienceNull()) {
                if (player.hasCorrectExperience()) {
                    playerInDB.setExperience(player.getExperience());
                } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            playerInDB.setLevel();
            playerInDB.setUntilNextLevel();

            return new ResponseEntity<>(playerInDB, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable("id") Long playerId) {
        if (playerId == null || playerId <= 0) {
            return new ResponseEntity<Player>(HttpStatus.BAD_REQUEST);
        } else {
            boolean isPlayerExist = playerService.getPlayerRepository().findById(playerId).isPresent();
            if (isPlayerExist) {
                playerService.getPlayerRepository().deleteById(playerId);
                return ResponseEntity.ok().body(HttpStatus.OK);
            } else {
                return new ResponseEntity<Player>(HttpStatus.NOT_FOUND);
            }

        }
    }
}
