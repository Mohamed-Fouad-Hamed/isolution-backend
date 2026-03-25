package com.alf.auth.keystore.web;

import com.alf.auth.keystore.manager.DatabaseKeyManager;
import com.alf.auth.keystore.rotation.RotationService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
//@RequestMapping("/keystore/admin")
public class KeystoreAdminController {
    private final RotationService rotationService;
    private final DatabaseKeyManager manager;
    public KeystoreAdminController(RotationService rotationService,
                                   DatabaseKeyManager manager) {
        this.rotationService = rotationService; this.manager = manager; }

    @PostMapping("/rotate")
    public Map<String,String> rotate() throws Exception {
        rotationService.rotateAndPublish();
        return Map.of("status","rotated","kid", manager.current().getKeyID());
    }

    @GetMapping("/jwks.json")
    public Map<String,Object> jwks() { return
            manager.publicJwkSet().toJSONObject(); }
}