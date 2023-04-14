package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text")
    private String text;

    @Column(name = "item_id")
    private long itemId;

    @Column(name = "author")
    private String authorName;

    @Column(name = "created")
    private LocalDateTime created;

    public Comment(String text, long itemId, String authorName, LocalDateTime created) {
        this.text = text;
        this.itemId = itemId;
        this.authorName = authorName;
        this.created = created;
    }
}
