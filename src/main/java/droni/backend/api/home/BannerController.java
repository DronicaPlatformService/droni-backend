package droni.backend.api.home;

import droni.backend.api.attacthfile.dto.DroniFileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
@Tag(name = "home", description = "드로니 홈 화면 banner 컨트롤러")
public class BannerController{


    @GetMapping("/banner")
    public List<DroniFileResponse> getBanner() {
        return null;
    }
}
