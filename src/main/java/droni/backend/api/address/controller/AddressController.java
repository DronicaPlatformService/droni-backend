package droni.backend.api.address.controller;

import droni.backend.api.address.dto.AddressResponse;
import droni.backend.api.address.dto.AddressSaveRequest;
import droni.backend.api.address.service.AddressService;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.global.exception.DroniNotFoundException;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Address", description = "주소지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;
    private final DroniUserRepository droniUserRepository;

    @Operation(summary = "주소지 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponse saveAddress(
            @AuthenticationPrincipal OAuth2UserPrincipal userPrincipal,
            @RequestBody AddressSaveRequest request) {
        DroniUser user = droniUserRepository.findByOauthId(userPrincipal.getOAuth2Id())
                .orElseThrow(() -> new DroniNotFoundException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
        return AddressResponse.from(addressService.saveAddress(user.getUserId(), request));
    }

    @Operation(summary = "주소지 목록 조회")
    @GetMapping
    public List<AddressResponse> getUserAddresses(
            @AuthenticationPrincipal OAuth2UserPrincipal userPrincipal) {
        DroniUser user = droniUserRepository.findByOauthId(userPrincipal.getOAuth2Id())
                .orElseThrow(() -> new DroniNotFoundException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
        return addressService.getUserAddresses(user.getUserId()).stream()
                .map(AddressResponse::from)
                .toList();
    }
}
