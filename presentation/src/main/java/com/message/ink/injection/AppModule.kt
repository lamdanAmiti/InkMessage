/*
 * Copyright (C) 2017 Moez Bhatti <moez.bhatti@gmail.com>
 *
 * This file is part of QKSMS.
 *
 * QKSMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QKSMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QKSMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.message.ink.injection

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkerFactory
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import com.message.ink.blocking.BlockingClient
import com.message.ink.blocking.BlockingManager
import com.message.ink.common.ViewModelFactory
import com.message.ink.common.util.BillingManagerImpl
import com.message.ink.common.util.NotificationManagerImpl
import com.message.ink.common.util.ShortcutManagerImpl
import com.message.ink.feature.conversationinfo.injection.ConversationInfoComponent
import com.message.ink.feature.themepicker.injection.ThemePickerComponent
import com.message.ink.listener.ContactAddedListener
import com.message.ink.listener.ContactAddedListenerImpl
import com.message.ink.manager.ActiveConversationManager
import com.message.ink.manager.ActiveConversationManagerImpl
import com.message.ink.manager.AlarmManager
import com.message.ink.manager.AlarmManagerImpl
import com.message.ink.manager.BillingManager
import com.message.ink.manager.ChangelogManager
import com.message.ink.manager.ChangelogManagerImpl
import com.message.ink.manager.KeyManager
import com.message.ink.manager.KeyManagerImpl
import com.message.ink.manager.NotificationManager
import com.message.ink.manager.PermissionManager
import com.message.ink.manager.PermissionManagerImpl
import com.message.ink.manager.RatingManager
import com.message.ink.manager.ReferralManager
import com.message.ink.manager.ReferralManagerImpl
import com.message.ink.manager.ShortcutManager
import com.message.ink.manager.WidgetManager
import com.message.ink.manager.WidgetManagerImpl
import com.message.ink.mapper.CursorToContact
import com.message.ink.mapper.CursorToContactGroup
import com.message.ink.mapper.CursorToContactGroupImpl
import com.message.ink.mapper.CursorToContactGroupMember
import com.message.ink.mapper.CursorToContactGroupMemberImpl
import com.message.ink.mapper.CursorToContactImpl
import com.message.ink.mapper.CursorToConversation
import com.message.ink.mapper.CursorToConversationImpl
import com.message.ink.mapper.CursorToMessage
import com.message.ink.mapper.CursorToMessageImpl
import com.message.ink.mapper.CursorToPart
import com.message.ink.mapper.CursorToPartImpl
import com.message.ink.mapper.CursorToRecipient
import com.message.ink.mapper.CursorToRecipientImpl
import com.message.ink.mapper.RatingManagerImpl
import com.message.ink.repository.BackupRepository
import com.message.ink.repository.BackupRepositoryImpl
import com.message.ink.repository.BlockingRepository
import com.message.ink.repository.BlockingRepositoryImpl
import com.message.ink.repository.ContactRepository
import com.message.ink.repository.ContactRepositoryImpl
import com.message.ink.repository.ConversationRepository
import com.message.ink.repository.ConversationRepositoryImpl
import com.message.ink.repository.EmojiReactionRepository
import com.message.ink.repository.EmojiReactionRepositoryImpl
import com.message.ink.repository.MessageContentFilterRepository
import com.message.ink.repository.MessageContentFilterRepositoryImpl
import com.message.ink.repository.MessageRepository
import com.message.ink.repository.MessageRepositoryImpl
import com.message.ink.repository.ScheduledMessageRepository
import com.message.ink.repository.ScheduledMessageRepositoryImpl
import com.message.ink.repository.SyncRepository
import com.message.ink.repository.SyncRepositoryImpl
import com.message.ink.worker.InjectionWorkerFactory
import javax.inject.Singleton

@Module(subcomponents = [
    ConversationInfoComponent::class,
    ThemePickerComponent::class])
class AppModule(private var application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    fun provideContentResolver(context: Context): ContentResolver = context.contentResolver

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideRxPreferences(preferences: SharedPreferences): RxSharedPreferences {
        return RxSharedPreferences.create(preferences)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    }

    @Provides
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory

    // Listener

    @Provides
    fun provideContactAddedListener(listener: ContactAddedListenerImpl): ContactAddedListener = listener

    // Manager

    @Provides
    fun provideBillingManager(manager: BillingManagerImpl): BillingManager = manager

    @Provides
    fun provideActiveConversationManager(manager: ActiveConversationManagerImpl): ActiveConversationManager = manager

    @Provides
    fun provideAlarmManager(manager: AlarmManagerImpl): AlarmManager = manager

    @Provides
    fun blockingClient(manager: BlockingManager): BlockingClient = manager

    @Provides
    fun changelogManager(manager: ChangelogManagerImpl): ChangelogManager = manager

    @Provides
    fun provideKeyManager(manager: KeyManagerImpl): KeyManager = manager

    @Provides
    fun provideNotificationsManager(manager: NotificationManagerImpl): NotificationManager = manager

    @Provides
    fun providePermissionsManager(manager: PermissionManagerImpl): PermissionManager = manager

    @Provides
    fun provideRatingManager(manager: RatingManagerImpl): RatingManager = manager

    @Provides
    fun provideShortcutManager(manager: ShortcutManagerImpl): ShortcutManager = manager

    @Provides
    fun provideReferralManager(manager: ReferralManagerImpl): ReferralManager = manager

    @Provides
    fun provideWidgetManager(manager: WidgetManagerImpl): WidgetManager = manager

    // Mapper

    @Provides
    fun provideCursorToContact(mapper: CursorToContactImpl): CursorToContact = mapper

    @Provides
    fun provideCursorToContactGroup(mapper: CursorToContactGroupImpl): CursorToContactGroup = mapper

    @Provides
    fun provideCursorToContactGroupMember(mapper: CursorToContactGroupMemberImpl): CursorToContactGroupMember = mapper

    @Provides
    fun provideCursorToConversation(mapper: CursorToConversationImpl): CursorToConversation = mapper

    @Provides
    fun provideCursorToMessage(mapper: CursorToMessageImpl): CursorToMessage = mapper

    @Provides
    fun provideCursorToPart(mapper: CursorToPartImpl): CursorToPart = mapper

    @Provides
    fun provideCursorToRecipient(mapper: CursorToRecipientImpl): CursorToRecipient = mapper

    // Repository

    @Provides
    fun provideBackupRepository(repository: BackupRepositoryImpl): BackupRepository = repository

    @Provides
    fun provideBlockingRepository(repository: BlockingRepositoryImpl): BlockingRepository = repository

    @Provides
    fun provideMessageContentFilterRepository(repository: MessageContentFilterRepositoryImpl): MessageContentFilterRepository = repository

    @Provides
    fun provideContactRepository(repository: ContactRepositoryImpl): ContactRepository = repository

    @Provides
    fun provideConversationRepository(repository: ConversationRepositoryImpl): ConversationRepository = repository

    @Provides
    fun provideMessageRepository(repository: MessageRepositoryImpl): MessageRepository = repository

    @Provides
    fun provideScheduledMessagesRepository(repository: ScheduledMessageRepositoryImpl): ScheduledMessageRepository = repository

    @Provides
    fun provideSyncRepository(repository: SyncRepositoryImpl): SyncRepository = repository

    @Provides
    fun provideEmojiReactionRepository(repository: EmojiReactionRepositoryImpl): EmojiReactionRepository = repository

    // worker factory
    @Provides
    fun provideWorkerFactory(workerFactory: InjectionWorkerFactory): WorkerFactory = workerFactory
}