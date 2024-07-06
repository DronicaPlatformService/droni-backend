package droni.backend.api.article.dto;

import droni.backend.global.exception.DroniBadRequestException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum ArticleTarget {
    ALL,
    USER,
    EXPERT;

    public static ArticleTarget fromString(String target) {
        return Arrays.stream(ArticleTarget.values())
                .filter(a -> a.name().equalsIgnoreCase(target))
                .findFirst()
                .orElseThrow(() -> new DroniBadRequestException(HttpStatus.BAD_REQUEST, String.format("Target '%s' not found", target)));

    }
}
