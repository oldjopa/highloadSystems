package org.oldjopa.hls.repository

import org.oldjopa.hls.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {}