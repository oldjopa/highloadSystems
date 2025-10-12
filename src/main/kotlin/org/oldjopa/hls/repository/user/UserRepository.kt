package org.oldjopa.hls.repository.user

import org.oldjopa.hls.model.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {}