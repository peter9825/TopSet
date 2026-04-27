package com.peter.topset.Users.Entity;
import com.peter.topset.Logging.WorkoutSession.Entity.WorkoutSessionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "users")

public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;


    //users -> workouts
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutSessionEntity> workouts = new ArrayList<>();

    // users <-> roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles")
    List<RoleEntity> roles = new ArrayList<>();

    public UserEntity(){}
    public UserEntity(String email, String username, String passwordHash) {
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public void addWorkout(WorkoutSessionEntity workout){
        workouts.add(workout);
        workout.setUsers(this);
    }

    public void removeWorkout(WorkoutSessionEntity workout){
        workouts.remove(workout);
        workout.setUsers(null);
    }


}
