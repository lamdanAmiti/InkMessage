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
package com.message.ink.injection.android

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.message.ink.feature.backup.BackupActivity
import com.message.ink.feature.blocking.BlockingActivity
import com.message.ink.feature.compose.ComposeActivity
import com.message.ink.feature.compose.ComposeActivityModule
import com.message.ink.feature.contacts.ContactsActivity
import com.message.ink.feature.contacts.ContactsActivityModule
import com.message.ink.feature.conversationinfo.ConversationInfoActivity
import com.message.ink.feature.gallery.GalleryActivity
import com.message.ink.feature.gallery.GalleryActivityModule
import com.message.ink.feature.main.MainActivity
import com.message.ink.feature.main.MainActivityModule
import com.message.ink.feature.notificationprefs.NotificationPrefsActivity
import com.message.ink.feature.notificationprefs.NotificationPrefsActivityModule
import com.message.ink.feature.plus.PlusActivity
import com.message.ink.feature.plus.PlusActivityModule
import com.message.ink.feature.qkreply.QkReplyActivity
import com.message.ink.feature.qkreply.QkReplyActivityModule
import com.message.ink.feature.scheduled.ScheduledActivity
import com.message.ink.feature.scheduled.ScheduledActivityModule
import com.message.ink.feature.settings.SettingsActivity
import com.message.ink.injection.scope.ActivityScope

@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [PlusActivityModule::class])
    abstract fun bindPlusActivity(): PlusActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    abstract fun bindBackupActivity(): BackupActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ComposeActivityModule::class])
    abstract fun bindComposeActivity(): ComposeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ContactsActivityModule::class])
    abstract fun bindContactsActivity(): ContactsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    abstract fun bindConversationInfoActivity(): ConversationInfoActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [GalleryActivityModule::class])
    abstract fun bindGalleryActivity(): GalleryActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [NotificationPrefsActivityModule::class])
    abstract fun bindNotificationPrefsActivity(): NotificationPrefsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [QkReplyActivityModule::class])
    abstract fun bindQkReplyActivity(): QkReplyActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ScheduledActivityModule::class])
    abstract fun bindScheduledActivity(): ScheduledActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    abstract fun bindSettingsActivity(): SettingsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    abstract fun bindBlockingActivity(): BlockingActivity

}
