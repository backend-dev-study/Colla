package kr.kro.colla.comment.domain;

import kr.kro.colla.user.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String contents;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Comment(User user, String contents) {
        this.user = user;
        this.contents = contents;
    }

}
