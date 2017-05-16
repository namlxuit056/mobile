package com.opensourceassignment.singkaraoke;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kernel Dang on 12/16/2015.
 */
public class SongAdapter extends ArrayAdapter<Song> {
    private static  ArrayList<Song> _allSongs = new ArrayList<>();
    private static ArrayList<Song> _songs = new ArrayList<>();
    private static KaraokeSongDBHelper _karaokeSongDBHelper;
    private int _resID;
    private Context _context;

    public SongAdapter(Context context,int resID) {

        super(context, resID, _songs);

        _karaokeSongDBHelper = KaraokeSongDBHelper.getInstance(context);
        _allSongs = _karaokeSongDBHelper.getAllSong();

        _context = context;
        _resID = resID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridLayout songView = null;
        final Song song = getItem(position);

        if(convertView == null){
            songView = new GridLayout(getContext());
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(_resID,songView);
        }
        else{
            songView = (GridLayout)convertView;
        }

        TextView songTitle = (TextView)songView.findViewById(R.id.song_title);
        TextView songLyricPreview = (TextView)songView.findViewById(R.id.song_lyric_preview);
        ImageView markFavoriteSong = (ImageView)songView.findViewById(R.id.mark_favorite_song);
        markFavoriteSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                song.setFavorite(!song.getFavorite());
                notifyDataSetChanged();
                _karaokeSongDBHelper.markFavoriteSong(song.getSongId(),song.getFavorite());
            }
        });

        songTitle.setText(song.getTitle());
        songLyricPreview.setText(_karaokeSongDBHelper.getLyricPreview(song.getSongId()));

        if (song.getFavorite() == true){
            markFavoriteSong.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_not_like));
        }
        else{
            markFavoriteSong.setImageDrawable(_context.getResources().getDrawable(R.drawable.ic_like));
        }

        return songView;
    }

    public void loadAllSongs(){
        _songs.clear();
        _songs.addAll(_allSongs);
        notifyDataSetChanged();
    }

    public void loadFavoriteSongs(){
        _songs.clear();
        for (Song song : _allSongs) {
            if(song.getFavorite())
                _songs.add(song);
        }
        notifyDataSetChanged();
    }

    public void findSongs(String keyword) {
        for (int i = 0; i < _songs.size(); ++i) {
            String title = toNormalString(_songs.get(i).getTitle());
            if(!title.contains(toNormalString(keyword))){
                _songs.remove(i);
                --i;
            }
        }
        notifyDataSetChanged();
    }

    public String toNormalString(String string){
        char[] a = {'á', 'à', 'ả', 'ạ', 'â', 'ấ', 'ầ', 'ẩ', 'ậ', 'ă', 'ắ', 'ằ', 'ẳ', 'ặ'};
        char[] e = {'é', 'è', 'ẻ', 'ẹ', 'ê', 'ế', 'ề', 'ể', 'ệ'};
        char[] o = {'ó', 'ò', 'ỏ', 'ọ', 'ô', 'ố', 'ồ', 'ổ', 'ộ', 'ơ', 'ớ', 'ờ', 'ở', 'ợ'};
        char[] i = {'í', 'ì', 'ỉ', 'ị'};
        char[] u = {'ú', 'ù', 'ủ', 'ụ', 'ư', 'ứ', 'ử', 'ự'};

        char[] letters = string.toLowerCase().toCharArray();
        boolean isReplaced;
        for (int index = 0; index < letters.length; index++) {
            isReplaced = false;
            for (int index2 = 0;letters[index] != 'a' && !isReplaced && index2 < a.length; index2++) {
                if (letters[index] == a[index2]) {
                    letters[index] = 'a';
                    isReplaced = true;
                    break;
                }
            }

            for (int index2 = 0;letters[index] != 'e' &&  !isReplaced && index2 < e.length; index2++) {
                if (letters[index] == e[index2]) {
                    letters[index] = 'e';
                    isReplaced = true;
                    break;
                }
            }

            for (int index2 = 0;letters[index] != 'o' &&  !isReplaced && index2 < o.length; index2++) {
                if (letters[index] == o[index2]) {
                    letters[index] = 'o';
                    isReplaced = true;
                    break;
                }
            }
            for (int index2 = 0;letters[index] != 'i' && !isReplaced && index2 < i.length; index2++) {
                if (letters[index] == i[index2]) {
                    letters[index] = 'i';
                    isReplaced = true;
                    break;
                }
            }

            for (int index2 = 0;letters[index] != 'u' &&  !isReplaced && index2 < u.length; index2++) {
                if (letters[index] == u[index2]) {
                    letters[index] = 'u';
                    isReplaced = true;
                    break;
                }
            }
            if (!isReplaced && letters[index] == 'đ')
                letters[index] = 'd';
        }
        return String.valueOf(letters);
    }

}
