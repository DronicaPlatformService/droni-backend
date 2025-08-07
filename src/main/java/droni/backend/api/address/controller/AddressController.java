package droni.backend.api.address.controller;

import droni.backend.api.address.dto.AddressResponse;
import droni.backend.api.address.dto.AddressSaveRequest;
import droni.backend.api.address.service.AddressService;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Address", description = "주소지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "주소지 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponse saveAddress(
            @AuthenticationPrincipal OAuth2UserPrincipal userPrincipal,
            @RequestBody AddressSaveRequest request
    ) {
        return AddressResponse.from(addressService.saveAddress(userPrincipal, request));
    }

    @Operation(summary = "주소지 목록 조회")
    @GetMapping
    public List<AddressResponse> getUserAddresses(
            @AuthenticationPrincipal OAuth2UserPrincipal userPrincipal
    ) {
        return addressService.getUserAddresses(userPrincipal).stream()
                .map(AddressResponse::from)
                .collect(Collectors.toList());
    }
}
