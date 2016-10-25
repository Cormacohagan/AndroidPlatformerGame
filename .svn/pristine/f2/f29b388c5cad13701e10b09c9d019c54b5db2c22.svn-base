package com.gamedev.techtronic.lunargame.Level.Triggers;

import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

// sits in game world as an invisible object. if player touches this trigger,
// the game screen's dialogue box should receive this trigger's dialogue ID
// so that the dialogue box can retrieve the appropriate text & info from txt/dialogueText

public class DialogueTrigger extends Trigger {

    private String dialogueID;

    public DialogueTrigger (float x, float y, Bitmap bitmap, GameScreen gameScreen, String animationName, Boolean isLooping, String dialogueID) {
        super(x, y, bitmap, gameScreen, animationName, isLooping);
        this.dialogueID = dialogueID;
    }

    public String getDialogueID() {
        return dialogueID;
    }
}
