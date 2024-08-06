package site.ph0en1x.task_management_sys.model.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.ph0en1x.task_management_sys.model.comment.Comment;
import site.ph0en1x.task_management_sys.model.task.Task;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

//@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
@Slf4j
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String passwordConfirmation;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "roles")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles_enum")
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> authoredTasks = new HashSet<>();

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> assignedTasks = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

}