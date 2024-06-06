package droni.backend.api.insectcontrol.dto;

import lombok.Getter;

@Getter
public enum InsectBidStatus {
    REQUESTING,
    RESERVED,
    COMPLETED
}
