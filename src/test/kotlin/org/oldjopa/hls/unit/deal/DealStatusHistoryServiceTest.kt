package org.oldjopa.hls.unit.deal

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.oldjopa.hls.model.deal.Deal
import org.oldjopa.hls.model.deal.DealStatus
import org.oldjopa.hls.model.deal.DealStatusHistory
import org.oldjopa.hls.model.user.User
import org.oldjopa.hls.repository.deal.DealStatusHistoryRepository
import org.oldjopa.hls.service.deal.DealStatusHistoryService
import java.time.Instant

class DealStatusHistoryServiceTest {

    private val repo = mockk<DealStatusHistoryRepository>()
    private val service = DealStatusHistoryService(repo)

    @Test
    fun `listByDealId should return mapped DTOs`() {
        // Arrange
        val dummyUser = mockk<User>(relaxed = true)
        val deal = Deal(
            dealNumber = "D1",
            aircraft = mockk(relaxed = true),
            buyer = dummyUser,
            seller = dummyUser,
            status = DealStatus("NEW", "New", "", 1, false)
        )
        val history1 = DealStatusHistory(
            deal = deal,
            status = deal.status,
            changedBy = dummyUser,
            comment = "c1",
            changedAt = Instant.now()
        )
        val history2 = DealStatusHistory(
            deal = deal,
            status = deal.status,
            changedBy = dummyUser,
            comment = "c2",
            changedAt = Instant.now()
        )
        every { repo.findByDealIdOrderByChangedAtAsc(1) } returns listOf(history1, history2)

        // Act
        val result = service.listByDealId(1)

        // Assert
        assertThat(result.map { it.comment }).containsExactly("c1", "c2")
    }

    @Test
    fun `save should call repository and return entity`() {
        // Arrange
        val dummyUser = mockk<User>(relaxed = true)
        val dummyDeal = mockk<Deal>(relaxed = true)
        val status = mockk<DealStatus>(relaxed = true)
        val history = DealStatusHistory(
            deal = dummyDeal,
            status = status,
            changedBy = dummyUser,
            comment = "c",
            changedAt = Instant.now()
        )
        every { repo.save(history) } returns history

        // Act
        val result = service.save(history)

        // Assert
        assertThat(result).isEqualTo(history)
        verify { repo.save(history) }
    }

}