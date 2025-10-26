package org.oldjopa.hls.repository.feature

import org.oldjopa.hls.model.feature.Feature
import org.springframework.data.jpa.repository.JpaRepository

interface FeatureRepository : JpaRepository<Feature, Long>
