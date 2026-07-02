package com.gestionSalon.service;


import com.gestionSalon.dto.permission.PermissionDTO;
import com.gestionSalon.mapper.PermissionMapper;
import com.gestionSalon.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public List<PermissionDTO> getAllPermissions() {

        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toDTO)
                .toList();
    }
}
