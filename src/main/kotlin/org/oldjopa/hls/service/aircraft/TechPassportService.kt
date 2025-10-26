package org.oldjopa.hls.service.aircraft

import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.model.feature.TechPassport
import org.oldjopa.hls.repository.feature.TechPassportRepository
import org.springframework.stereotype.Service

@Service
class TechPassportService(private val repo: TechPassportRepository) {
    fun get(id: Long) = repo.findById(id).orElseThrow { NotFoundException("TechPassport $id not found") }
    fun save(tp: TechPassport) = repo.save(tp)
    fun delete(id: Long) = repo.deleteById(id)
}