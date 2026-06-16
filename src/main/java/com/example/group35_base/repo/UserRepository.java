package com.example.group35_base.repo;


import com.example.group35_base.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u.xp, u.level FROM User u WHERE u.id = :id")
    Optional<Object[]> findXpAndLevelByUserId(@Param("id") Long id);

    @Query("SELECT u FROM User u ORDER BY u.level DESC, u.xp DESC")
    List<User> findAllByOrderByLevelDescXpDesc();


}
