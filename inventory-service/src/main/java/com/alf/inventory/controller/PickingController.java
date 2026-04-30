package com.alf.inventory.controller;

import com.alf.inventory.dto.AssignPickerRequest;
import com.alf.inventory.dto.PickLineRequest;
import com.alf.inventory.entity.Picking;
import com.alf.inventory.services.PickingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pickings")
public class PickingController {

    private final PickingService service;

    @PostMapping("/from-move/{moveId}")
    public Picking create(@PathVariable Long moveId) {
        return service.createFromMove(moveId);
    }

    @PostMapping("/{id}/assign")
    public void assign(
            @PathVariable Long id,
            @RequestBody AssignPickerRequest request
    ) {
        service.assignPicker(id, request.userId());
    }

    @PostMapping("/{id}/start")
    public void start(@PathVariable Long id) {
        service.startPicking(id);
    }

    @PostMapping("/{id}/pick-line")
    public void pickLine(
            @PathVariable Long id,
            @RequestBody PickLineRequest request
    ) {
        service.pickLine(id, request);
    }

    @PostMapping("/{id}/complete")
    public void complete(@PathVariable Long id) {
        service.completePicking(id);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        service.cancelPicking(id);
    }






}
