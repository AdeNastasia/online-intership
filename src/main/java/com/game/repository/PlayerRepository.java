package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAll();
    @Override
    Optional<Player> findById(Long id);

    @Query("select count(pc) from Player pc where " +
            "(:name is null or pc.name like concat('%',:name,'%')) and " +
            "(:title is null or pc.title like concat('%',:title,'%')) and " +
            "(:race is null or pc.race = :race) and " +
            "(:profession is null or pc.profession = :profession) and " +
            "(:after is null or pc.birthday >= :after) and " +
            "(:before is null or pc.birthday <= :before) and " +
            "(:banned is null or pc.banned = :banned) and " +
            "(:minExperience is null or pc.experience >= :minExperience) and " +
            "(:maxExperience is null or pc.experience <= :maxExperience) and " +
            "(:minLevel is null or pc.level >= :minLevel) and " +
            "(:maxLevel is null or pc.level <= :maxLevel)")

    long count(@Param("name") String name,
               @Param("title") String title,
               @Param("race") Race race,
               @Param("profession") Profession profession,
               @Param("after") Date after,
               @Param("before") Date before,
               @Param("banned") Boolean banned,
               @Param("minExperience") Integer minExperience,
               @Param("maxExperience") Integer maxExperience,
               @Param("minLevel") Integer minLevel,
               @Param("maxLevel") Integer maxLevel);

    @Query("select p from Player p where " +
            "(:name is null or p.name like concat('%',:name,'%')) and " +
            "(:title is null or p.title like concat('%',:title,'%')) and " +
            "(:race is null or p.race = :race) and " +
            "(:profession is null or p.profession = :profession) and " +
            "(:after is null or p.birthday >= :after) and " +
            "(:before is null or p.birthday <= :before) and " +
            "(:banned is null or p.banned = :banned) and " +
            "(:minExperience is null or p.experience >= :minExperience) and " +
            "(:maxExperience is null or p.experience <= :maxExperience) and " +
            "(:minLevel is null or p.level >= :minLevel) and " +
            "(:maxLevel is null or p.level <= :maxLevel)")
    Page<Player> findAllBy(
            @Param("name") String name,
            @Param("title") String title,
            @Param("race") Race race,
            @Param("profession") Profession profession,
            @Param("after") Date after,
            @Param("before") Date before,
            @Param("banned") Boolean banned,
            @Param("minExperience") Integer minExperience,
            @Param("maxExperience") Integer maxExperience,
            @Param("minLevel") Integer minLevel,
            @Param("maxLevel") Integer maxLevel,
            Pageable pageable);

    @Override
    <S extends Player> S save(S entity);



    void delete(Player player);
}
