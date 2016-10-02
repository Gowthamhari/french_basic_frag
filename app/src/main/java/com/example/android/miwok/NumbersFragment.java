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

public class NumbersFragment extends Fragment {

    private static final int fragmentPosition = 1;
    //public static final String saveIt ="savekey";

    SharedPreferences sf;

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
                        releaseMediaPlayer();

                    }
                }
            };

    private MediaPlayer.OnCompletionListener mOnCompleteListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.word_list, container, false);



        aManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("one", "un", R.drawable.number_one, R.raw.numberone));
        words.add(new Word("two", "deux", R.drawable.number_two, R.raw.numbertwo));
        words.add(new Word("three", "trois", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "quatre", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "cinq", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "six", R.drawable.number_six, R.raw.num_six));
        words.add(new Word("seven", "sept", R.drawable.number_seven, R.raw.num_seven));
        words.add(new Word("eight", "huit", R.drawable.number_eight, R.raw.num_eight));
        words.add(new Word("nine", "neuf", R.drawable.number_nine, R.raw.num_nine));
        words.add(new Word("ten", "dix", R.drawable.number_ten, R.raw.num_ten));

        words.add(new Word("eleven", "onze", R.raw.num_eleven));
        words.add(new Word("twelve", "douze", R.raw.num_twelve));
        words.add(new Word("thirteen", "treize", R.raw.num_thirteen));
        words.add(new Word("fourteen", "quatorze", R.raw.num_fourteen));
        words.add(new Word("fifteen", "quinze", R.raw.num_fifteen));
        words.add(new Word("sixteen", "seize", R.raw.num_sixteen));
        words.add(new Word("seventeen", "dix-sept", R.raw.num_seventeen));
        words.add(new Word("eighteen", "dix-huit", R.raw.num_eighteen));
        words.add(new Word("nineteen", "dix-neuf", R.raw.num_nineteen));
        words.add(new Word("twenty", "vingt", R.raw.num_twenty));

        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word word = words.get(position);

                // Request audio focus for playback
                // Request permanent focus.
                // Use the music stream.
                int result = aManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //aManager.registerMediaButtonEventReceiver(RemoteControlReceiver);
                    // Start playback.


                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(mOnCompleteListener);
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


    /*@Override
    public void onDestroy() {
        try {
            save();
            super.onDestroy();
        } catch (Exception e){

            Log.e("message", String.valueOf(e));
        }
    }

    private void save() {
        int position = fragmentPosition;
        if(sf!=null) {
            SharedPreferences.Editor editor = sf.edit();
            editor.putInt(saveIt, position);
            editor.commit();
        }
    }*/

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



