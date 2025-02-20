package com.problemsolver.event.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView

fun AppCompatImageView.changeDrawable(newDrawable: Drawable?) {
    val animator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    animator.duration = 300
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            setImageDrawable(newDrawable)
            ObjectAnimator.ofFloat(this@changeDrawable, "alpha", 0f, 1f).apply {
                duration = 300
                start()
            }
        }
    })
    animator.start()
}