package site.ph0en1x.task_management_sys.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.ph0en1x.task_management_sys.model.comment.Comment;
import site.ph0en1x.task_management_sys.model.task.Task;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "password_confirmation", nullable = false)
    @Transient
    private String passwordConfirmation;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ElementCollection
    @CollectionTable(name = "users_roles_enum")
    @Enumerated(EnumType.STRING)
    private Set<Roles> role;

//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(name = "user_role_mapping",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> rolesSet = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> authoredTasks = new HashSet<>();

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> assignedTasks = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

}