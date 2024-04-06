package droni.backend.attacthfile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "file")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DroniFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private String path;
    private String name;
    private String type;
    private Integer size;
}
