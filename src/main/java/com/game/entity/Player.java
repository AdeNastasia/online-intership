package com.game.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {
    public Player() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String title;
    @Enumerated(EnumType.STRING)
    Race race;
    @Enumerated(EnumType.STRING)
    Profession profession;
    Integer experience;
    Integer level;
    Integer untilNextLevel;
    Date birthday;
    Boolean banned;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel() {
        this.level = (int) ((Math.sqrt(2500 + (200 * this.getExperience())) - 50) / 100);
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel() {
        this.untilNextLevel = (50 * (this.getLevel() + 1) * (this.getLevel() + 2) - this.getExperience());
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public boolean doWeHaveAllNeededParams() {
        return !(
                        isNameNull() ||
                        !hasCorrectName() ||
                        isTitleNull() ||
                        !hasCorrectTitle() ||
                        this.getTitle().equals("") ||
                        isRaceNull() ||
                        this.getRace().toString().equals("") ||
                        isProfessionNull() ||
                        this.getProfession().toString().equals("") ||
                        isBirthdayNull() ||
                        !hasCorrectBirthday() ||
                        isBannedNull() ||
                        !hasCorrectExperience()
        );
    }

    public boolean isNameNull() {
        return this.getName() == null;
    }

    public boolean isTitleNull() {
        return this.getTitle() == null;
    }

    public boolean isRaceNull() {
        return this.getRace() == null;
    }

    public boolean isProfessionNull() {
        return this.getProfession() == null;
    }

    public boolean isBirthdayNull() {
        return this.getBirthday() == null;
    }

    public boolean isBannedNull() {
        return this.getBanned() == null;
    }

    public boolean isExperienceNull() {
        return this.getExperience() == null;
    }
    public boolean hasCorrectName() {
        return !this.getName().equals("") || this.getName().length() <= 12;
    }
    public boolean hasCorrectTitle() {
        return this.getTitle().length() <= 30;
    }
    public boolean hasCorrectBirthday() {
        return this.getBirthday().getYear() >= 100 && this.getBirthday().getYear() <= 200;
    }

    public boolean hasCorrectExperience() {
        return this.experience > 0 && this.experience < 10000000;
    }

    public static Race convertingRace(String race) {
        return (race == null) ? null : Race.valueOf(race);
    }

    public static Profession convertingProfession(String profession) {
        return (profession == null) ? null : Profession.valueOf(profession);
    }

    public static Date convertingAfterDate(Long afterDate) {
        return (afterDate == null) ? null : new Date(afterDate);
    }

    public static Date convertingBeforeDate(Long beforeDate) {
        return (beforeDate == null) ? null : new Date(beforeDate);
    }

}
