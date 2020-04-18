package com.rovkinmax.decoratorusageexample

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

object MessageFactory {
    private const val DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private val fullDateFormatter = SimpleDateFormat(DATE_TIME_PATTERN, Locale.ENGLISH)
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    private val dateFormatter = SimpleDateFormat("d MMM", Locale.ENGLISH)

    private val textes = arrayOf(
            "CommonLit's text sets cover a range of subject matter including historical, cultural, and political topics. They include relevant reading passages from a variety of genres and are great for social studies and STEM teachers, or for building background knowledge or diving deeply into a literary movement in reading classes."/*,
            "Learn about the varied cultures, histories, and politics of over 1 billion people across the African continent's 50+ countries.",
            "Modernism emerged as a literary movement in response to the World Wars. It focused on the confusing change and discovery happening in Europe and America in response to war.",
            "Learn about the events leading up to America's Revolutionary War for independence from Britain and how the war was won.",
            "How much can we credit today's art, philosophy, and democracy.",
            "Hi",
            "sup",
            "Could you help me, please?",
            "Nice pic",
            "Good by",
            "Welcome",
            "Nice to meet you",
            "Me too"*/
    )

    private val images = arrayOf(
            R.drawable.a/*,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f,
            R.drawable.g,
            R.drawable.h,
            R.drawable.j,
            R.drawable.k,
            R.drawable.l,
            R.drawable.m,
            R.drawable.n*/
    )

    fun defaultMessageSet(): List<ChatMessage> {
        return listOf(
                buildMessage(
                        isMy = false,
                        time = "2020-02-01T16:07:29.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-02-01T16:08:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-02-02T11:08:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-02-02T11:09:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-02T11:09:22.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-02T11:11:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-03T12:12:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-03T12:13:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-03T12:14:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-03T13:11:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-03T14:12:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-05T12:13:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-06T12:14:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-02-06T16:07:29.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-02-06T16:08:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-02-07T11:08:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-07T12:13:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-07T12:14:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-02-07T13:11:00.000Z",
                        isImage = false
                ),

                buildMessage(
                        isMy = false,
                        time = "2020-03-01T16:07:29.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-03-01T16:08:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-03-02T11:08:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-03-02T11:09:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-02T11:09:22.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-02T11:11:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-03T12:12:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-03T12:13:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-03T12:14:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-03T13:11:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-03T14:12:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-05T12:13:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-06T12:14:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-03-06T16:07:29.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-03-06T16:08:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = false,
                        time = "2020-03-07T11:08:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-07T12:13:00.000Z",
                        isImage = true
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-07T12:14:00.000Z",
                        isImage = false
                ),
                buildMessage(
                        isMy = true,
                        time = "2020-03-07T13:11:00.000Z",
                        isImage = false
                )

        ).asReversed()
    }

    fun buildMyMessage(): ChatMessage {
        return buildMessage(
                isMy = true,
                time = fullDateFormatter.format(Date())
        )
    }

    fun buildAgentMessage(): ChatMessage {
        return buildMessage(
                isMy = false,
                time = fullDateFormatter.format(Date())
        )
    }

    private fun buildMessage(isMy: Boolean,
                             time: String,
                             isImage: Boolean = Random.nextBoolean()): ChatMessage {
        val date = timeStringToDate(time)
        val timeStampText = timeFormatter.format(date)
        val dateText = dateFormatter.format(date)

        if (isImage)
            return buildImageMessage(isMy, date, timeStampText, dateText)
        return buildTextMessage(isMy, date, timeStampText, dateText)
    }

    private fun buildImageMessage(
            isMy: Boolean,
            date: Date,
            time: String,
            dateText: String
    ): ImageMessage {
        return ImageMessage(
                id = IdGenerator.generateId().toLong(),
                author = buildAuthor(isMy),
                date = date,
                timeStampText = time,
                dateText = dateText,
                isMy = isMy,
                imageId = images[Random.nextInt(from = 0, until = images.size)]
        )
    }

    private fun buildTextMessage(
            isMy: Boolean,
            date: Date,
            time: String,
            dateText: String
    ): TextMessage {
        return TextMessage(
                id = IdGenerator.generateId().toLong(),
                author = buildAuthor(isMy),
                date = date,
                timeStampText = time,
                dateText = dateText,
                isMy = isMy,
                text = textes[Random.nextInt(from = 0, until = textes.size)]
        )
    }

    private fun buildAuthor(isMy: Boolean): Author {
        return Author(
                id = if (isMy) 0L else 1L,
                name = if (isMy) "Max" else "Agent 007",
                avatar = "https://www.gravatar.com/avatar/7f24dfc79b29dab23804693ecf3e88b2?s=64&d=identicon&r=PG"
        )
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun timeStringToDate(time: String): Date {
        return fullDateFormatter.parse(time)
    }

    object IdGenerator {
        private val nextGeneratedId = AtomicInteger(1)

        fun generateId(): Int {
            while (true) {
                val result = nextGeneratedId.get()
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                var newValue = result + 1
                if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
                if (nextGeneratedId.compareAndSet(result, newValue)) {
                    return result
                }
            }
        }
    }
}