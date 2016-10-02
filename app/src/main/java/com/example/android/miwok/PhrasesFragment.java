package com.example.android.miwok;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.android.miwok.MainActivity.saveIt;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

    private static final int fragmentPosition = 4;

    SharedPreferences sf;


    public PhrasesFragment() {
        // Required empty public constructor
    }

    private MediaPlayer mMediaPlayer;

    private AudioManager aManager;

    AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                        // Pause playback because your Audio Focus was
                        // temporarily stolen, but will be back soon.
                        // i.e. for a phone call
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                        // Stop playback, because you lost the Audio Focus.
                        // i.e. the user started some other playback app
                        // Remember to unregister your controls/buttons here.
                        // And release the kra — Audio Focus!
                        // You’re done.

                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                        // Lower the volume, because something else is also
                        // playing audio over you.
                        // i.e. for notifications or navigation directions
                        // Depending on your audio playback, you may prefer to
                        // pause playback here instead. You do you.
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Resume playback, because you hold the Audio Focus
                        // again!
                        // i.e. the phone call ended or the nav directions
                        // are finished
                        // If you implement ducking and lower the volume, be
                        // sure to return it to normal here, as well.
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        aManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);


        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?", "Où allez-vous?", R.raw.where_are_you_going));
        words.add(new Word("What is your name?", "Comment vous appelez-vous?", R.raw.what_is_your_name));
        words.add(new Word("My name is...", "Mon nom est...", R.raw.my_name_is));
        words.add(new Word("How are you feeling?", "Comment allez-vous?", R.raw.how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "Je me sens bien.", R.raw.i_am_feeling_good));
        words.add(new Word("Are you coming?", "Viens-tu??", R.raw.are_you_coming));
        words.add(new Word("Yes, I’m coming.", "Oui j'arrive.", R.raw.yes_i_am_coming));
        words.add(new Word("I’m coming.", "J'arrive.", R.raw.i_am_coming));
        words.add(new Word("Let’s go.", "Allons-y.", R.raw.lets_go));
        words.add(new Word("Come here.", "Venez ici.",R.raw.come_here));


        WordAdapter adapter = new WordAdapter(getActivity(), words,  R.color.category_phrases);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                releaseMediaPlayer();
                Word word = words.get(position);


                int result = aManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //aManager.registerMediaButtonEventReceiver(RemoteControlReceiver);
                    // Start playback.


                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setFragmentPosition(fragmentPosition);
    }


    //java.lang.NullPointerException: Attempt to invoke interface method 'android.content.SharedPreferences$Editor android.content.SharedPreferences.edit()' on a null object reference

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            aManager.abandonAudioFocus(afChangeListener);

        }
    }

}

