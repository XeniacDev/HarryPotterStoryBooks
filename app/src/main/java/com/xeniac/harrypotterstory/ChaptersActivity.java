package com.xeniac.harrypotterstory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.xeniac.harrypotterstory.adapters.BooksAdapter;
import com.xeniac.harrypotterstory.adapters.ChaptersAdapter;
import com.xeniac.harrypotterstory.database.chaptersDataBase.ChaptersDataSource;
import com.xeniac.harrypotterstory.models.DataItemBooks;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ChaptersActivity extends AppCompatActivity {

    private ChaptersDataSource chaptersDataSource;
    private DataItemBooks book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        Toolbar toolbar = findViewById(R.id.toolbar_chapters);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled((true));

        book = Objects.requireNonNull(getIntent().getExtras()).
                getParcelable(BooksAdapter.ITEM_KEY);

        if (book == null) {
            throw new AssertionError("Null data item received!");
        } else {
            chaptersInitializer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        chaptersDataSource.open();
        chaptersRecyclerView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        chaptersDataSource.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onSupportNavigateUp();
    }

    private void chaptersInitializer() {
        chaptersDataSource = new ChaptersDataSource(this);
        chaptersDataSource.open();

        setTitle(book.getTitle());
        ImageView coverIV = findViewById(R.id.iv_chapters_cover);

        try {
            String imageFile = book.getCover();
            InputStream inputStream = getAssets().open(imageFile);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            coverIV.setImageDrawable(drawable);
            coverIV.setClipToOutline(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        chaptersRecyclerView();
    }

    private void chaptersRecyclerView() {
        ChaptersAdapter chaptersAdapter = new ChaptersAdapter(this,
                chaptersDataSource.getAllItems(book.getId(), false));
        RecyclerView chaptersRV = findViewById(R.id.rv_chapters);
        chaptersRV.setAdapter(chaptersAdapter);
    }
}