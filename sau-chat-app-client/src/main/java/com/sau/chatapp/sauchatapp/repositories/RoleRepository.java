package com.sau.chatapp.sauchatapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sau.chatapp.sauchatapp.entities.ERole;
import com.sau.chatapp.sauchatapp.entities.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
