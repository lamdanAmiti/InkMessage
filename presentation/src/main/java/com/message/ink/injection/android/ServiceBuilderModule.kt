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
import com.message.ink.feature.backup.RestoreBackupService
import com.message.ink.injection.scope.ActivityScope
import com.message.ink.service.HeadlessSmsSendService
import com.message.ink.service.AutoDeleteService

@Module
abstract class ServiceBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindAutoDeleteService(): AutoDeleteService

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindHeadlessSmsSendService(): HeadlessSmsSendService

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindRestoreBackupService(): RestoreBackupService

}
