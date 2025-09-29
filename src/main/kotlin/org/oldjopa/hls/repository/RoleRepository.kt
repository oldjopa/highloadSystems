package org.oldjopa.hls.repository

import org.oldjopa.hls.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, String>