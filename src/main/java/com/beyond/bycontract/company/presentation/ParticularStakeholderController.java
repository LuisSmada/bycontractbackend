package com.beyond.bycontract.company.presentation;

import com.beyond.bycontract.company.application.dto.ParticularStakeholderResponse;
import com.beyond.bycontract.company.application.service.ParticularStakeholderService;
import com.beyond.bycontract.company.presentation.dto.CreateParticularStakeholderRequest;
import com.beyond.bycontract.company.presentation.dto.UpdateParticularStakeholderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/particular-stakeholders")
public class ParticularStakeholderController {
    private final ParticularStakeholderService service;

    @Autowired
    public ParticularStakeholderController(ParticularStakeholderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticularStakeholderResponse create(@RequestBody CreateParticularStakeholderRequest request) {
        return service.create(request.toCommand());
    }

    @GetMapping
    public Page<ParticularStakeholderResponse> getAll(
            // @PageableDefault définit des valeurs par défaut si le client (Next.js) n'envoie pas les paramètres
            @PageableDefault(size = 20, sort = "lastName") Pageable pageable
    ) {
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ParticularStakeholderResponse getById(@PathVariable UUID id) {
        return service.getById(id);
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
    public void delete(@PathVariable UUID id) {
        service.deleteById(id);
    }

}
