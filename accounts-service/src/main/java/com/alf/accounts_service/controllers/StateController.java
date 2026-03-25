package com.alf.accounts_service.controllers;

import com.alf.accounts_service.dtos.state.StateDto;
import com.alf.accounts_service.services.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;

    @GetMapping
    public ResponseEntity<List<StateDto>> getAllStates() {
        List<StateDto> states = stateService.getAllStates();
        return ResponseEntity.ok(states);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StateDto> getStateById(@PathVariable Integer id) {
        return stateService.getStateById(id)
                .map(state -> ResponseEntity.ok(state))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<List<StateDto>> getStatesByCountryId(@PathVariable Integer countryId) {
        List<StateDto> states = stateService.getStatesByCountryId(countryId);
        return ResponseEntity.ok(states);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StateDto>> searchStates(@RequestParam String name) {
        List<StateDto> states = stateService.searchStatesByName(name);
        return ResponseEntity.ok(states);
    }

    @PostMapping
    public ResponseEntity<StateDto> createState( @RequestBody StateDto stateDto) {
        try {
            StateDto createdState = stateService.createState(stateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdState);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StateDto> updateState(@PathVariable Integer id, @RequestBody StateDto stateDto) {
        try {
            StateDto updatedState = stateService.updateState(id, stateDto);
            return ResponseEntity.ok(updatedState);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Integer id) {
        try {
            stateService.deleteState(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
