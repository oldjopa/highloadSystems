package org.oldjopa.hls

import org.oldjopa.hls.integration.TestcontainersConfiguration
import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<HighloadsystemsApplication>().with(TestcontainersConfiguration::class).run(*args)
}
