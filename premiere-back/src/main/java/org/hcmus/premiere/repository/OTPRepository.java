package org.hcmus.premiere.repository;

import java.util.List;
import java.util.Optional;
import org.hcmus.premiere.model.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
  List<OTP> findByEmail(String email);
  // find the lastest otp by email
  Optional<OTP> findTopByEmailOrderByCreatedAtDesc(String email);
}
