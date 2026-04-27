package com.peter.topset.Logging.WorkoutSession.Repository;

import com.peter.topset.Logging.WorkoutSession.Entity.WorkoutSessionEntity;
import com.peter.topset.Users.Entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSessionEntity,Long> {


    List<WorkoutSessionEntity> findAllByUsers(UserEntity user);

    Optional<WorkoutSessionEntity> findByIdAndUsers(Long id,UserEntity user);

    Optional<WorkoutSessionEntity> deleteByIdAndUsers(Long id, UserEntity user);
}
