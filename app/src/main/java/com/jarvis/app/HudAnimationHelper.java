package com.jarvis.app;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class HudAnimationHelper {

    private final View hudCircle;
    private final ImageButton micButton;
    private Animation rotateAnim;
    private Animation pulseAnim;

    public HudAnimationHelper(View hudCircle, ImageButton micButton) {
        this.hudCircle = hudCircle;
        this.micButton = micButton;
    }

    public void startListening() {
        stopAllAnimations();
        pulseAnim = AnimationUtils.loadAnimation(micButton.getContext(), R.anim.pulse_mic);
        micButton.startAnimation(pulseAnim);
        rotateAnim = AnimationUtils.loadAnimation(hudCircle.getContext(), R.anim.rotate_circle);
        hudCircle.startAnimation(rotateAnim);
    }

    public void stopListening() {
        if (pulseAnim != null) {
            micButton.clearAnimation();
            pulseAnim = null;
        }
    }

    public void startThinking() {
        stopAllAnimations();
        rotateAnim = AnimationUtils.loadAnimation(hudCircle.getContext(), R.anim.rotate_circle);
        hudCircle.startAnimation(rotateAnim);
    }

    public void stopThinking() {
        if (rotateAnim != null) {
            hudCircle.clearAnimation();
            rotateAnim = null;
        }
    }

    private void stopAllAnimations() {
        hudCircle.clearAnimation();
        micButton.clearAnimation();
        rotateAnim = null;
        pulseAnim = null;
    }
}
