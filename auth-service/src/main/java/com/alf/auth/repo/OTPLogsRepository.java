package com.alf.auth.repo;

import com.alf.auth.models.OtpLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPLogsRepository extends JpaRepository<OtpLog,String> {

}
