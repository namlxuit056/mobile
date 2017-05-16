package com.opensourceassignment.singkaraoke;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class DetailSongActivity extends AppCompatActivity {

    public static String SONG = "DetailSong";
    Song song;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_song);

        TextView songTitle = (TextView)findViewById(R.id.song_title);
        ToggleButton toggleButton = (ToggleButton)findViewById(R.id.mark_favorite_song);
        TextView songArtist = (TextView)findViewById(R.id.song_artist);
        TextView arirangCode = (TextView)findViewById(R.id.arirang_code);
        TextView californiaCode = (TextView)findViewById(R.id.california_code);
        TextView songLyric = (TextView)findViewById(R.id.song_lyric);

        song = (Song)getIntent().getSerializableExtra(SONG);

        songTitle.setText(song.getTitle());
        songArtist.setText(song.getArtist());
        toggleButton.setChecked(song.getFavorite());

        KaraokeSongDBHelper dbHelper = KaraokeSongDBHelper.getInstance(null);

        arirangCode.setText(dbHelper.getArirangCode(song.getSongId()));
        californiaCode.setText(dbHelper.getCaliforniaCode(song.getSongId()));
        songLyric.setText(dbHelper.getLyric(song.getSongId()));

        Button findSong = (Button)findViewById(R.id.find_song_on_youtube_button);
        findSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEARCH);
                intent.setPackage("com.google.android.youtube");
                intent.putExtra("query", song.getTitle() + " karaoke");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
