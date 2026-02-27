package com.thiagoarend.parking_spot_management_api.repository;

import com.thiagoarend.parking_spot_management_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}