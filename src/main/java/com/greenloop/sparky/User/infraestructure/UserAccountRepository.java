package com.greenloop.sparky.User.infraestructure;

import com.greenloop.sparky.User.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByEmail(String email);

    List<UserAccount> findByEmpresaId(Long id);

    Optional<Object> findByIdAndEmpresaId(Long id, Long id1);
}
