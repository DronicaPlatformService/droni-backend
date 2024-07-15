package droni.backend.api.expert.controller;

import backend.generated_api.ExpertApi;
import backend.generated_model.PilotProfile;
import droni.backend.api.expert.service.ExpertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExpertController implements ExpertApi {
    private final ExpertService expertService;

    @Override
    public ResponseEntity<List<PilotProfile>> getPopularPilot() {
        List<PilotProfile> popularExpert = expertService.getPopularExpert();
        return ResponseEntity.ok(popularExpert);
    }
}
