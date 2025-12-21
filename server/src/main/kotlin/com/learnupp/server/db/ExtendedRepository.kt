package com.learnupp.server.db

import com.learnupp.domain.model.MessageCategory
import com.learnupp.domain.model.MessageThread
import com.learnupp.domain.model.MessageThreadType
import com.learnupp.domain.model.earnings.EarningsSummary
import com.learnupp.domain.model.earnings.EarningsTransaction
import com.learnupp.domain.model.payments.PaymentMethod
import com.learnupp.domain.model.payments.PaymentMethodType
import com.learnupp.domain.model.settings.LanguageOption
import com.learnupp.domain.model.settings.NotificationGroup
import com.learnupp.domain.model.settings.NotificationSetting
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ExtendedRepository(private val db: Database) {
    fun getEarningsSummary(): EarningsSummary = transaction(db) {
        val txs = EarningsTransactionsTable.selectAll().map { it.toEarningsTx() }
        val totalEarned = txs.sumOf { it.amount }
        val thisMonth = txs.getOrNull(0)?.amount ?: 0.0
        val lastMonth = txs.getOrNull(1)?.amount ?: 0.0
        EarningsSummary(
            totalEarned = totalEarned,
            thisMonth = thisMonth,
            lastMonth = lastMonth,
            transactions = txs
        )
    }

    fun getMessages(): List<MessageCategory> = transaction(db) {
        val categories = MessageCategoriesTable.selectAll().map { it.toMessageCategory() }
        val threadsByCategory = MessageThreadsTable.selectAll()
            .groupBy { it[MessageThreadsTable.categoryId] }
            .mapValues { entry -> entry.value.map { row -> row.toMessageThread() } }
        categories.map { cat ->
            cat.copy(threads = threadsByCategory[cat.id] ?: emptyList())
        }
    }

    fun getNotifications(): List<NotificationSetting> = transaction(db) {
        NotificationSettingsTable.selectAll().map { it.toNotificationSetting() }
    }

    fun updateNotification(id: String, enabled: Boolean): List<NotificationSetting> = transaction(db) {
        NotificationSettingsTable.update({ NotificationSettingsTable.id eq id }) {
            it[NotificationSettingsTable.enabled] = enabled
        }
        NotificationSettingsTable.selectAll().map { it.toNotificationSetting() }
    }

    fun getPayments(): List<PaymentMethod> = transaction(db) {
        PaymentMethodsTable.selectAll().map { it.toPaymentMethod() }
    }

    fun getLanguages(): List<LanguageOption> = transaction(db) {
        LanguageOptionsTable.selectAll().map { it.toLanguage() }
    }
}

private fun ResultRow.toEarningsTx() = EarningsTransaction(
    id = this[EarningsTransactionsTable.id],
    title = this[EarningsTransactionsTable.title],
    date = this[EarningsTransactionsTable.date],
    amount = this[EarningsTransactionsTable.amount],
)

private fun ResultRow.toMessageCategory(): MessageCategory =
    MessageCategory(
        id = this[MessageCategoriesTable.id],
        title = this[MessageCategoriesTable.title],
        description = this[MessageCategoriesTable.description],
        iconText = this[MessageCategoriesTable.iconText],
        iconColorHex = this[MessageCategoriesTable.iconColorHex],
        threads = emptyList()
    )

private fun ResultRow.toMessageThread(): MessageThread =
    MessageThread(
        id = this[MessageThreadsTable.id],
        title = this[MessageThreadsTable.title],
        subtitle = this[MessageThreadsTable.subtitle],
        lastMessageSnippet = this[MessageThreadsTable.lastMessageSnippet],
        timestamp = this[MessageThreadsTable.timestamp],
        isUnread = this[MessageThreadsTable.isUnread],
        unreadCount = this[MessageThreadsTable.unreadCount],
        avatarUrl = this[MessageThreadsTable.avatarUrl],
        memberCount = this[MessageThreadsTable.memberCount],
        type = MessageThreadType.valueOf(this[MessageThreadsTable.type])
    )

private fun ResultRow.toNotificationSetting(): NotificationSetting =
    NotificationSetting(
        id = this[NotificationSettingsTable.id],
        title = this[NotificationSettingsTable.title],
        description = this[NotificationSettingsTable.description],
        enabled = this[NotificationSettingsTable.enabled],
        group = NotificationGroup.valueOf(this[NotificationSettingsTable.group])
    )

private fun ResultRow.toPaymentMethod(): PaymentMethod =
    PaymentMethod(
        id = this[PaymentMethodsTable.id],
        label = this[PaymentMethodsTable.label],
        subtitle = this[PaymentMethodsTable.subtitle],
        maskedNumber = this[PaymentMethodsTable.maskedNumber],
        type = PaymentMethodType.valueOf(this[PaymentMethodsTable.type]),
        active = this[PaymentMethodsTable.active],
        primary = this[PaymentMethodsTable.primary],
    )

private fun ResultRow.toLanguage(): LanguageOption =
    LanguageOption(
        code = this[LanguageOptionsTable.code],
        name = this[LanguageOptionsTable.name],
        flagEmoji = this[LanguageOptionsTable.flagEmoji],
        selected = this[LanguageOptionsTable.selected],
    )


