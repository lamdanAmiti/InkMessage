/*
 * Copyright (C) 2019 Moez Bhatti <moez.bhatti@gmail.com>
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
package com.message.ink.feature.compose.part

import android.annotation.SuppressLint
import android.content.Context
import com.message.ink.R
import com.message.ink.common.Navigator
import com.message.ink.common.base.QkViewHolder
import com.message.ink.common.util.Colors
import com.message.ink.common.util.extensions.resolveThemeColor
import com.message.ink.common.util.extensions.setBackgroundTint
import com.message.ink.common.util.extensions.setTint
import com.message.ink.extensions.mapNotNull
import com.message.ink.feature.compose.BubbleUtils
import com.message.ink.model.Message
import com.message.ink.model.MmsPart
import com.message.ink.util.tryOrNull
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.mms_file_list_item.*
import javax.inject.Inject

class FileBinder @Inject constructor(colors: Colors, private val context: Context) : PartBinder() {

    @Inject lateinit var navigator: Navigator

    override val partLayout = R.layout.mms_file_list_item
    override var theme = colors.theme()

    // This is the last binder we check. If we're here, we can bind the part
    override fun canBindPart(part: MmsPart) = true

    @SuppressLint("CheckResult")
    override fun bindPart(
        holder: QkViewHolder,
        part: MmsPart,
        message: Message,
        canGroupWithPrevious: Boolean,
        canGroupWithNext: Boolean
    ) {
        BubbleUtils.getBubble(false, canGroupWithPrevious, canGroupWithNext, message.isMe())
                .let(holder.fileBackground::setBackgroundResource)

        tryOrNull(true) {
            Observable.just(part.getUri())
                .mapNotNull { uri ->
                    tryOrNull(true) {
                        context.contentResolver.openInputStream(uri)?.use { it.available() }
                    }
                }
                .map { bytes ->
                    when (bytes) {
                        in 0..999 -> "$bytes B"
                        in 1000..999999 -> "${"%.1f".format(bytes / 1000f)} KB"
                        in 1000000..9999999 -> "${"%.1f".format(bytes / 1000000f)} MB"
                        else -> "${"%.1f".format(bytes / 1000000000f)} GB"
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { size -> holder.size.text = size }

            holder.filename.text = part.getBestFilename()
        }

        if (!message.isMe()) {
            holder.fileBackground.setBackgroundTint(theme.theme)
            holder.icon.setTint(theme.textPrimary)
            holder.filename.setTextColor(theme.textPrimary)
            holder.size.setTextColor(theme.textTertiary)
        } else {
            holder.fileBackground.setBackgroundTint(holder.containerView.context.resolveThemeColor(R.attr.bubbleColor))
            holder.icon.setTint(holder.containerView.context.resolveThemeColor(android.R.attr.textColorSecondary))
            holder.filename.setTextColor(holder.containerView.context.resolveThemeColor(android.R.attr.textColorPrimary))
            holder.size.setTextColor(holder.containerView.context.resolveThemeColor(android.R.attr.textColorTertiary))
        }
    }

}