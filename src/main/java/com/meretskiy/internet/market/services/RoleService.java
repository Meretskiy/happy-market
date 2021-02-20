package com.meretskiy.internet.market.services;

import com.meretskiy.internet.market.model.Role;
import com.meretskiy.internet.market.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRoleForNewUser() {
        return roleRepository.getOne(1L);
    }
}
