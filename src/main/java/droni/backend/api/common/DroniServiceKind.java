package droni.backend.api.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DroniServiceKind {
    INSECT_CONTROL("/insect-control");


    private final String redirectUrl;
}
