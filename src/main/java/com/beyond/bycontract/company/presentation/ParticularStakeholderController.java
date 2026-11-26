package com.beyond.bycontract.company.presentation;

import com.beyond.bycontract.company.application.dto.ParticularStakeholderResponse;
import com.beyond.bycontract.company.application.service.ParticularStakeholderService;
import com.beyond.bycontract.company.presentation.dto.CreateParticularStakeholderRequest;
import com.beyond.bycontract.company.presentation.dto.UpdateParticularStakeholderRequest;
import com.beyond.bycontract.shared.utils.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/particular-stakeholders")
@PreAuthorize("isAuthenticated()")
public class ParticularStakeholderController {
    private final ParticularStakeholderService service;

    @Autowired
    public ParticularStakeholderController(ParticularStakeholderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticularStakeholderResponse create(@RequestBody CreateParticularStakeholderRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
        return service.create(request.toCommand(currentUser.getId()));
    }

    @GetMapping("/all-particulars")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticularStakeholderResponse> getAllParticularsForAdmin() {
        return service.getAll(null);
    }

    @GetMapping
    public List<ParticularStakeholderResponse> getAll(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return service.getAll(currentUser.getId());
    }

    @GetMapping("/{id}")
    public ParticularStakeholderResponse getById(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails currentUser) {
        return service.getById(id, currentUser.getId());
    }

    @PatchMapping("/{id}")
    public ParticularStakeholderResponse update(
            @PathVariable UUID id,
            @RequestBody UpdateParticularStakeholderRequest request
    ) {
        return service.updateById(id, request.toCommand());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails currentUser) {
        service.deleteById(id, currentUser.getId());
    }

}
