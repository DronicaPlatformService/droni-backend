package droni.backend.api.expert.controller;

import droni.backend.api.expert.dto.PilotProfile;
import droni.backend.api.expert.service.ExpertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/expert", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "expert", description = "드로니 전문가 API")
public class ExpertController  {
    private final ExpertService expertService;

    @GetMapping("/home/popular-expert")
    @Operation(summary = "홈 화면 인기 조종사 api")
    public List<PilotProfile> getPopularPilot() {
        return expertService.getPopularExpert();
    }
}
