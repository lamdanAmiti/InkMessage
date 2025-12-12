package com.message.ink.feature.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.message.ink.R
import com.message.ink.feature.main.MainActivity

class SplashActivity : AppCompatActivity() {

    companion object {
        private var hasShownSplash = false
    }

    private val topMessages = listOf(
        "Crunching...",
        "Opening...",
        "Loading...",
        "Sorting...",
        "Digging...",
        "Kriching..."
    )

    private val bottomMessages = listOf(
        "This won't happen again",
        "Hold on to your messages",
        "I know you have FOMO now",
        "Oh no, I lied last time",
        "Your messages miss you too",
        "Almost there, probably",
        "Counting your unread texts",
        "Waking up the ducks",
        "Unraveling the Talmud",
        "Bribing the servers",
        "Stretching the pixels",
        "Warming up the emojis",
        "Teaching SMS to MMS",
        "Polishing the bubbles"
    )

    private val handler = Handler(Looper.getMainLooper())
    private var bottomTextRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Skip splash if already shown (warm start)
        if (hasShownSplash) {
            goToMain()
            return
        }

        hasShownSplash = true
        setContentView(R.layout.splash_activity)

        // Set random top text
        findViewById<TextView>(R.id.topText).text = topMessages.random()

        // Set initial random bottom text
        val bottomTextView = findViewById<TextView>(R.id.bottomText)
        bottomTextView.text = bottomMessages.random()

        // Start dot animations
        startDotAnimations()

        // Change bottom text every 3 seconds
        bottomTextRunnable = object : Runnable {
            override fun run() {
                bottomTextView.text = bottomMessages.random()
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(bottomTextRunnable!!, 3000)

        // Go to main activity after a short delay
        handler.postDelayed({
            goToMain()
        }, 2000)
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun startDotAnimations() {
        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)

        val duration = 400L
        val bounceHeight = -30f

        fun createBounceAnimator(view: View, startDelay: Long): AnimatorSet {
            val up = ObjectAnimator.ofFloat(view, "translationY", 0f, bounceHeight).apply {
                this.duration = duration / 2
                interpolator = AccelerateDecelerateInterpolator()
            }
            val down = ObjectAnimator.ofFloat(view, "translationY", bounceHeight, 0f).apply {
                this.duration = duration / 2
                interpolator = AccelerateDecelerateInterpolator()
            }
            return AnimatorSet().apply {
                playSequentially(up, down)
                this.startDelay = startDelay
            }
        }

        fun animateDots() {
            val set = AnimatorSet()
            set.playTogether(
                createBounceAnimator(dot1, 0),
                createBounceAnimator(dot2, 150),
                createBounceAnimator(dot3, 300)
            )
            set.addListener(object : android.animation.Animator.AnimatorListener {
                override fun onAnimationStart(animation: android.animation.Animator) {}
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    if (!isFinishing) {
                        handler.postDelayed({ animateDots() }, 200)
                    }
                }
                override fun onAnimationCancel(animation: android.animation.Animator) {}
                override fun onAnimationRepeat(animation: android.animation.Animator) {}
            })
            set.start()
        }

        animateDots()
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomTextRunnable?.let { handler.removeCallbacks(it) }
        handler.removeCallbacksAndMessages(null)
    }
}
