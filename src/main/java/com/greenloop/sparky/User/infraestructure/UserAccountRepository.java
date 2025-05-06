package com.greenloop.sparky.User.infraestructure;

import com.greenloop.sparky.User.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    List<UserAccount> findByEmpresaId(Long empresaId);
    Optional<UserAccount> findByIdAndEmpresaId(Long id, Long empresaId);
    UserAccount findByEmail(String email);
}
