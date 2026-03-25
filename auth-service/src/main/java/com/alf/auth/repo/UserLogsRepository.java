package com.alf.auth.repo;

import com.alf.auth.models.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogsRepository extends JpaRepository<UserLog,String> {
}
