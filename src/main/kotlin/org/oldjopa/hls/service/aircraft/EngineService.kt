package org.oldjopa.hls.service.aircraft

import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.model.feature.Engine
import org.oldjopa.hls.repository.feature.EngineRepository
import org.springframework.stereotype.Service

@Service
class EngineService(private val repo: EngineRepository) {
    fun get(id: Long) = repo.findById(id).orElseThrow { NotFoundException("Engine $id not found") }
    fun save(engine: Engine) = repo.save(engine)
}