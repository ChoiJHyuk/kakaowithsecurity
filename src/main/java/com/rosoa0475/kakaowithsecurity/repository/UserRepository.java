package com.rosoa0475.kakaowithsecurity.repository;

import com.rosoa0475.kakaowithsecurity.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
