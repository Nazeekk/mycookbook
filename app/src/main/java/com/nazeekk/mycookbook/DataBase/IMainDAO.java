package com.nazeekk.mycookbook.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nazeekk.mycookbook.Models.Recipe;

import java.util.List;

@Dao
public interface IMainDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Recipe recipe);

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    List<Recipe> getAll();

    @Query("UPDATE recipes SET title = :title, recipe = :recipe WHERE ID = :ID")
    void update(int ID, String title, String recipe);

    @Delete
    void delete (Recipe recipe);

    @Query("UPDATE recipes SET pinned = :pin WHERE ID = :ID")
    void pin(int ID, boolean pin);
}
