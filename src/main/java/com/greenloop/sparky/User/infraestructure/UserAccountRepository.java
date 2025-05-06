package com.greenloop.sparky.User.infraestructure;

import com.greenloop.sparky.User.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByEmail(String email);
}
