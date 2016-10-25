package com.gamedev.techtronic.lunargame.AI;


import android.util.Log;

import com.gamedev.techtronic.lunargame.Character.NPC;

import java.util.ArrayList;

/**
 * Scheduler allows certain tasks to be performed in regular intervals described by the parameter
 * "interval", as in interval=10 means it is executed once every 10 frames. This is useful for tasks that
 * don't require to be run every frame as they would use excessive amounts of cpu time for no benefit.
 * Main use as of writing is to manage the AI, path finding in particular which can be very excessive
 * if calculated every frame.
 */
public class Scheduler {

    private ArrayList<BehaviorRecord> behaviors;
    private long frame;

    public Scheduler() {
        behaviors = new ArrayList<>();
        frame=0;
    }

    public void addNPCBehaviors(NPC npc, int interval){
        BehaviorRecord record = new BehaviorRecord();
        record.npc = npc;
        record.frequency = interval;

        behaviors.add(record);

        // Balance the phase values for each behavior
        // Phase helps offset on which frame each activity is run
        for (int i = 0; i < behaviors.size(); i++) {
            behaviors.get(i).phase = i ;
        }

    }

    public void run(){

        frame++;

        for (BehaviorRecord behavior: behaviors){
            if ((frame + behavior.phase) % (behavior.frequency) == 0){
                // run AI behaviors here
                behavior.npc.seekPlayer();
//                Log.d("Scheduler","running on frame: "+String.valueOf(frame));
            }
        }
    }

    private class BehaviorRecord{
        private NPC npc;
        private int frequency;
        private int phase;
    }
}
