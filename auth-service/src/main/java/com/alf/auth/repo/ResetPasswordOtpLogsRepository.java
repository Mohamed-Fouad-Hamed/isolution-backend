package com.alf.auth.repo;

import com.alf.auth.models.ResetPasswordOtpLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordOtpLogsRepository extends JpaRepository<ResetPasswordOtpLog,String> {

}
