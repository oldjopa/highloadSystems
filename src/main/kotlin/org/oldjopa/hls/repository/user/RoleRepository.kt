package org.oldjopa.hls.repository.user

import org.oldjopa.hls.model.user.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, String>