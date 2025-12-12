/*
 * Copyright (C) 2025
 *
 * This file is part of QUIK.
 *
 * QUIK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QUIK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QUIK.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.message.ink.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.message.ink.blocking.BlockingClient
import com.message.ink.interactor.UpdateBadge
import com.message.ink.manager.ActiveConversationManager
import com.message.ink.manager.NotificationManager
import com.message.ink.manager.ShortcutManager
import com.message.ink.repository.ConversationRepository
import com.message.ink.repository.MessageRepository
import com.message.ink.repository.ScheduledMessageRepository
import com.message.ink.repository.SyncRepository
import com.message.ink.util.Preferences
import javax.inject.Inject

class InjectionWorkerFactory @Inject constructor(
    private val conversationRepo: ConversationRepository,
    private val blockingClient: BlockingClient,
    private val prefs: Preferences,
    private val messageRepo: MessageRepository,
    private val updateBadge: UpdateBadge,
    private val shortcutManager: ShortcutManager,
    private val scheduledMessageRepository: ScheduledMessageRepository,
    private val notificationManager: NotificationManager,
    private val activeConversationManager: ActiveConversationManager,
    private val syncRepo: SyncRepository,

) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val instance = Class
            .forName(workerClassName)
            .asSubclass(Worker::class.java)
            .getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
            .newInstance(appContext, workerParameters)

        when (instance) {
            is HousekeepingWorker ->
                instance.scheduledMessageRepository = scheduledMessageRepository
            is ReceiveSmsWorker -> {
                instance.conversationRepo  = conversationRepo
                instance.blockingClient = blockingClient
                instance.prefs = prefs
                instance.messageRepo = messageRepo
                instance.shortcutManager = shortcutManager
                instance.notificationManager = notificationManager
                instance.updateBadge =  updateBadge
            }
            is ReceiveMmsWorker -> {
                instance.syncRepo = syncRepo
                instance.activeConversationManager = activeConversationManager
                instance.conversationRepo = conversationRepo
                instance.blockingClient = blockingClient
                instance.prefs = prefs
                instance.messageRepo = messageRepo
                instance.shortcutManager = shortcutManager
                instance.notificationManager = notificationManager
                instance.updateBadge = updateBadge
            }
        }

        return instance
    }
}