package com.alf.auth.keystore.sync;

import com.alf.auth.keystore.manager.DatabaseKeyManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class KeySyncListener implements MessageListener {
    private final DatabaseKeyManager manager;
    public KeySyncListener(DatabaseKeyManager manager) { this.manager =
            manager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try { manager.loadFromDb(); } catch (Exception e) { /* log */ }
    }
}
